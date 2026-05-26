package com.example.tomenaguita.ui.comprador.pago

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.databinding.FragmentPasarelaPagoBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.StripeHelper
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.CarritoViewModel
import com.example.tomenaguita.viewmodel.PedidoViewModel
import com.example.tomenaguita.viewmodel.ProductoViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/*
 * Pantalla de pago mediante Stripe PaymentSheet.
 * Recibe por navegación el número de pedido, el total y el correo del comprador.
 * Al pulsar el botón de pago, crea un PaymentIntent en el backend (StripeHelper),
 * presenta el formulario de Stripe y procesa el resultado.
 * Si el pago es exitoso, actualiza el estado del pedido a PAGADO, reduce el stock
 * de los productos comprados, vacía el carrito y navega al historial de pedidos.
 */
class PasarelaPagoFragment : Fragment() {

    private var _binding: FragmentPasarelaPagoBinding? = null
    private val binding get() = _binding!!

    // Argumentos de navegación: orderNumber, totalAmount y emailComprador
    private val args: PasarelaPagoFragmentArgs by navArgs()

    // ViewModel que gestiona el estado y las actualizaciones del pedido
    private val pedidoViewModel: PedidoViewModel by activityViewModels()

    // ViewModel que gestiona el vaciado del carrito tras el pago exitoso
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    // ViewModel que provee los productos actuales para reducir su stock tras el pago
    private val productoViewModel: ProductoViewModel by activityViewModels()

    // Client secret del PaymentIntent creado por StripeHelper; se usa para extraer el ID de transacción
    private var clientSecret: String? = null

    /*
     * PaymentSheet de Stripe inicializado de forma perezosa.
     * Registra el callback handlePaymentResult antes de onStart
     * para que Stripe pueda devolver el resultado correctamente.
     */
    private val paymentSheet: PaymentSheet by lazy {
        PaymentSheet(this) { result -> handlePaymentResult(result) }
    }

    // Infla el layout del fragmento usando ViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasarelaPagoBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Inicializa Stripe con la clave publicable, muestra el número de pedido y el total,
     * fuerza la inicialización del PaymentSheet y asigna el listener del botón de pago.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el SDK de Stripe con la clave publicable antes de presentar el formulario
        PaymentConfiguration.init(requireContext(), Constants.STRIPE_PUBLISHABLE_KEY)

        binding.tvOrderNumber.text = args.orderNumber
        binding.tvTotalPago.text = args.totalAmount.toDouble().toCOP()

        // Acceder al paymentSheet para que quede registrado antes de onStart
        paymentSheet

        binding.btnPagarMP.setOnClickListener { iniciarPago() }
    }

    // ─── Crear PaymentIntent y presentar PaymentSheet ────────────────────────

    /*
     * Llama a StripeHelper para crear un PaymentIntent con el monto del pedido.
     * Mientras espera, muestra una barra de progreso y deshabilita el botón.
     * Si tiene éxito, presenta el formulario de pago de Stripe al usuario.
     * Si falla, muestra el error y rehabilita el botón.
     */
    private fun iniciarPago() {
        binding.btnPagarMP.isEnabled = false
        binding.progressBar.visible()

        lifecycleScope.launch {
            try {
                // Crear el PaymentIntent en el backend; devuelve el client secret
                val secret = StripeHelper.createPaymentIntent(amount = args.totalAmount.toInt())
                clientSecret = secret
                binding.progressBar.gone()
                // Presentar el formulario de pago de Stripe con el nombre del comercio
                paymentSheet.presentWithPaymentIntent(
                    paymentIntentClientSecret = secret,
                    configuration = PaymentSheet.Configuration(
                        merchantDisplayName = getString(R.string.merchant_display_name)
                    )
                )
            } catch (e: Exception) {
                Log.e("PasarelaPago", "Error creando PaymentIntent: ${e.message}")
                binding.btnPagarMP.isEnabled = true
                binding.progressBar.gone()
                binding.root.showSnackbar(getString(R.string.msg_payment_start_error, e.message))
            }
        }
    }

    // ─── Resultado del PaymentSheet ──────────────────────────────────────────

    /*
     * Procesa el resultado devuelto por el formulario de Stripe.
     * Completed: pago exitoso, delega en onPagoCompletado().
     * Failed: error en el cobro, muestra mensaje y rehabilita el botón.
     * Canceled: el usuario cerró el formulario sin pagar, rehabilita el botón.
     * Consume: result (PaymentSheetResult) con el estado del cobro.
     */
    private fun handlePaymentResult(result: PaymentSheetResult) {
        when (result) {
            is PaymentSheetResult.Completed  -> onPagoCompletado()
            is PaymentSheetResult.Failed     -> {
                Log.e("PasarelaPago", "Pago fallido: ${result.error}")
                _binding?.btnPagarMP?.isEnabled = true
                _binding?.progressBar?.gone()
                _binding?.root?.showSnackbar(getString(R.string.msg_payment_failed, result.error.localizedMessage))
            }
            is PaymentSheetResult.Canceled   -> {
                // El usuario canceló el pago; no se hace ninguna acción adicional
                _binding?.btnPagarMP?.isEnabled = true
                _binding?.progressBar?.gone()
            }
        }
    }

    /*
     * Ejecuta todas las acciones post-pago cuando Stripe confirma el cobro:
     * 1. Actualiza el estado del pedido a PAGADO en Room y Firestore.
     * 2. Guarda el ID de transacción de Stripe en el documento del pedido en Firestore.
     * 3. Lee el carrito actual, reduce el stock de cada producto comprado y vacía el carrito.
     * 4. Navega al historial de pedidos y muestra un mensaje de confirmación.
     */
    private fun onPagoCompletado() {
        // Extraer el ID del PaymentIntent desde el client secret (formato: "pi_xxx_secret_yyy")
        val paymentIntentId = clientSecret?.substringBefore("_secret_") ?: ""
        val userId = SessionManager(requireContext()).getUserId()

        // 1. Actualizar estado del pedido a PAGADO (Room + Firestore via viewModelScope)
        pedidoViewModel.actualizarEstadoByOrderNumber(args.orderNumber, EstadoPedido.PAGADO.valor)

        // 2. Guardar transactionId en Firestore (fire-and-forget)
        if (paymentIntentId.isNotBlank()) {
            FirebaseFirestore.getInstance()
                .collection(Constants.FS_PEDIDOS).document(args.orderNumber)
                .update("transactionId", paymentIntentId)
        }

        // 3. Leer carrito ANTES de vaciarlo, reducir stock y luego vaciar
        lifecycleScope.launch {
            try {
                val db = AppDatabase.getInstance(requireContext())
                // first() emite inmediatamente con los datos actuales de Room
                val cartItems = db.carritoDao().getCarritoByUsuario(userId).first()
                val productos = productoViewModel.productosDisponibles.value ?: emptyList()

                // Reducir stock en Room + Firestore (viewModelScope — sobrevive navegación)
                productoViewModel.reducirStock(cartItems, productos)
            } catch (e: Exception) {
                Log.e("PasarelaPago", "Error leyendo carrito para stock: ${e.message}")
            }

            // Vaciar carrito en Room + Firestore (viewModelScope — sobrevive navegación)
            carritoViewModel.vaciarCarrito(userId)

            // 4. Navegar al historial y confirmar el pago al usuario
            _binding?.root?.showSnackbar(getString(R.string.msg_payment_success, args.orderNumber))
            if (_binding != null) findNavController().navigate(R.id.action_pago_to_historial)
        }
    }

    // Libera el binding al destruir la vista para evitar fugas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
