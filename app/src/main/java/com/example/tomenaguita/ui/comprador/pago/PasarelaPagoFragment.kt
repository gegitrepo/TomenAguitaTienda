package com.example.tomenaguita.ui.comprador.pago

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tomenaguita.ui.comprador.CompradorMainActivity
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tomenaguita.R
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.databinding.FragmentPasarelaPagoBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.MercadoPagoHelper
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.CarritoViewModel
import com.example.tomenaguita.viewmodel.PedidoViewModel
import kotlinx.coroutines.launch

class PasarelaPagoFragment : Fragment() {

    private var _binding: FragmentPasarelaPagoBinding? = null
    private val binding get() = _binding!!

    private val args: PasarelaPagoFragmentArgs by navArgs()
    private val pedidoViewModel: PedidoViewModel by activityViewModels()
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPasarelaPagoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvOrderNumber.text = args.orderNumber
        binding.tvTotalPago.text = args.totalAmount.toDouble().toCOP()

        setupWebView()

        binding.btnPagarMP.setOnClickListener {
            iniciarPagoMP()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    return when {
                        url.contains(Constants.MP_URL_SUCCESS) -> { handleSuccess(url); true }
                        url.contains(Constants.MP_URL_FAILURE) -> { handleFailure(); true }
                        url.contains(Constants.MP_URL_PENDING) -> { handlePending(); true }
                        else -> false
                    }
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressBar.gone()
                }
            }
        }
    }

    private fun iniciarPagoMP() {
        binding.btnPagarMP.isEnabled = false
        binding.progressBar.visible()

        lifecycleScope.launch {
            try {
                val checkoutUrl = MercadoPagoHelper.crearPreferencia(
                    accessToken = Constants.MP_SANDBOX_ACCESS_TOKEN,
                    titulo = "Pedido TomenAgüita ${args.orderNumber}",
                    cantidad = 1,
                    precioUnitario = args.totalAmount.toDouble(),
                    emailComprador = args.emailComprador,
                    referenciaExterna = args.orderNumber
                )
                // Mostrar WebView, ocultar contenido y ocultar BottomNav para pantalla completa
                binding.containerContent.gone()
                binding.webView.visible()
                binding.progressBar.visible()
                ocultarBottomNav(true)
                binding.webView.loadUrl(checkoutUrl)
            } catch (e: Exception) {
                binding.btnPagarMP.isEnabled = true
                binding.progressBar.gone()
                binding.root.showSnackbar("Error al conectar con Mercado Pago: ${e.message}")
            }
        }
    }

    private fun handleSuccess(url: String) {
        // Actualizar estado del pedido a PAGADO
        pedidoViewModel.actualizarEstadoByOrderNumber(
            args.orderNumber,
            EstadoPedido.PAGADO.valor
        )

        // Limpiar carrito
        val session = SessionManager(requireContext())
        carritoViewModel.vaciarCarrito(session.getUserId())

        ocultarBottomNav(false)
        binding.root.showSnackbar("¡Pago exitoso! Tu pedido ${args.orderNumber} está confirmado.")
        findNavController().navigate(R.id.action_pago_to_historial)
    }

    private fun handleFailure() {
        ocultarBottomNav(false)
        binding.webView.gone()
        binding.containerContent.visible()
        binding.btnPagarMP.isEnabled = true
        binding.progressBar.gone()
        binding.root.showSnackbar("El pago fue rechazado. Verifica los datos de la tarjeta e intenta de nuevo.")
    }

    private fun handlePending() {
        pedidoViewModel.actualizarEstadoByOrderNumber(args.orderNumber, "procesando")
        val session = SessionManager(requireContext())
        carritoViewModel.vaciarCarrito(session.getUserId())
        ocultarBottomNav(false)
        binding.root.showSnackbar("Tu pago está siendo procesado. Te notificaremos cuando se confirme.")
        findNavController().navigate(R.id.action_pago_to_historial)
    }

    /** Oculta o muestra el BottomNav de CompradorMainActivity durante el checkout. */
    private fun ocultarBottomNav(ocultar: Boolean) {
        try {
            val activity = requireActivity() as? CompradorMainActivity ?: return
            val bottomNav = activity.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                com.example.tomenaguita.R.id.bottomNavComprador
            )
            bottomNav?.visibility = if (ocultar) View.GONE else View.VISIBLE
        } catch (_: Exception) { }
    }

    override fun onDestroyView() {
        ocultarBottomNav(false) // Restaurar BottomNav si se destruye el fragment
        binding.webView.destroy()
        super.onDestroyView()
        _binding = null
    }
}
