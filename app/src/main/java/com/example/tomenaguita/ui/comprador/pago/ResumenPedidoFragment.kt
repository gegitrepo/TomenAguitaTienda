package com.example.tomenaguita.ui.comprador.pago

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.databinding.FragmentResumenPedidoBinding
import com.example.tomenaguita.ui.adapter.DetallePedidoAdapter
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.LocationHelper
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.CarritoViewModel
import com.example.tomenaguita.viewmodel.PedidoViewModel
import com.example.tomenaguita.viewmodel.ProductoViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/*
 * Pantalla de resumen del pedido antes del pago.
 * El comprador revisa los productos de su carrito, el total a pagar,
 * y selecciona la dirección de entrega mediante un mapa interactivo de Google Maps.
 * Al confirmar, se crea el pedido en Room y Firestore y se navega a la pasarela de pago.
 */
class ResumenPedidoFragment : Fragment() {

    private var _binding: FragmentResumenPedidoBinding? = null
    private val binding get() = _binding!!

    // ViewModel que gestiona la lectura del carrito activo del comprador
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    // ViewModel que provee los datos de los productos para construir los detalles del pedido
    private val productoViewModel: ProductoViewModel by activityViewModels()

    // ViewModel que crea el pedido y expone el pedido recién creado para navegar
    private val pedidoViewModel: PedidoViewModel by activityViewModels()

    // Referencia al mapa de Google Maps para controlar cámara y marcador
    private var googleMap: GoogleMap? = null

    // Marcador que indica la posición de entrega seleccionada por el comprador en el mapa
    private var selectedMarker: Marker? = null

    // Ítems actuales del carrito, usados al confirmar el pedido
    private var carritoItems: List<com.example.tomenaguita.data.database.entity.CarritoItem> = emptyList()

    // Job cancelable para la geocodificación inversa del centro del mapa
    private var geocodingJob: Job? = null

    /*
     * Launcher que solicita permiso de ubicación precisa.
     * Si se concede, detecta la ubicación del comprador y centra el mapa.
     * Si se deniega, muestra un mensaje informativo.
     */
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) detectarUbicacion()
            else mostrarEstado(getString(R.string.msg_location_permission_denied_map))
        }

    // Infla el layout del fragmento usando ViewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResumenPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Inicializa el MapView, configura los listeners del mapa y del formulario,
     * observa el carrito activo y el pedido creado por el ViewModel.
     * Al abrirse, intenta detectar automáticamente la ubicación del comprador.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        val userId = session.getUserId()

        // Pre-rellenar el nombre del comprador desde la sesión local
        binding.etNombre.setText(session.getUserNombre() ?: "")
        cargarTelefonoFirestore()

        /*
         * Inicializar MapView — onResume() inmediato para cargar teselas aunque
         * el Fragment no haya pasado por su propio onResume todavía.
         */
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()

        // Evita que el NestedScrollView intercepte los toques destinados al mapa
        binding.mapView.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN,
                android.view.MotionEvent.ACTION_MOVE ->
                    v.parent.requestDisallowInterceptTouchEvent(true)
                android.view.MotionEvent.ACTION_UP,
                android.view.MotionEvent.ACTION_CANCEL ->
                    v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        binding.mapView.getMapAsync { map ->
            googleMap = map
            map.uiSettings.isScrollGesturesEnabled = true
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isZoomGesturesEnabled = true
            map.uiSettings.isRotateGesturesEnabled = true

            // HARDCODED: centro inicial del mapa en Bogotá — mercado objetivo del negocio (Colombia)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(BOGOTA_DEFAULT, Constants.MAP_CITY_ZOOM))

            // Toque en el mapa: coloca o mueve el marcador y centra la cámara en esa posición
            map.setOnMapClickListener { latLng ->
                selectedMarker?.remove()
                selectedMarker = map.addMarker(MarkerOptions().position(latLng))
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            }

            // Cuando la cámara deja de moverse, geocodificar el punto central para el campo dirección
            map.setOnCameraIdleListener {
                val target = map.cameraPosition.target
                geocodearCentro(target.latitude, target.longitude)
            }
        }

        // Configurar el RecyclerView de resumen con los productos del carrito
        val detalleAdapter = DetallePedidoAdapter()
        binding.rvResumen.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResumen.adapter = detalleAdapter

        // Observar el carrito: calcular total y construir los detalles del pedido para mostrar
        carritoViewModel.getCarrito(userId).observe(viewLifecycleOwner) { items ->
            carritoItems = items
            val total = items.sumOf { it.precioAlMomento * it.cantidad }
            binding.tvSubtotal.text = total.toCOP()
            binding.tvTotal.text = total.toCOP()

            val productoMap = (productoViewModel.productosDisponibles.value ?: emptyList())
                .associateBy { it.id }
            val detalles = items.mapNotNull { item ->
                val producto = productoMap[item.productoId] ?: return@mapNotNull null
                DetallePedido(
                    pedidoId = 0L,
                    productoId = item.productoId,
                    vendedorId = producto.vendedorId,
                    nombreProducto = producto.nombre,
                    presentacion = producto.presentacion,
                    cantidad = item.cantidad,
                    precioUnitario = item.precioAlMomento,
                    subtotal = item.precioAlMomento * item.cantidad
                )
            }
            detalleAdapter.submitList(detalles)
        }

        /*
         * Cuando el pedido se crea correctamente en el ViewModel, navegar a la pasarela de pago
         * pasando el número de pedido, el total y el correo del comprador como argumentos.
         */
        pedidoViewModel.ultimoPedidoCreado.observe(viewLifecycleOwner) { pedido ->
            pedido ?: return@observe
            pedidoViewModel.clearUltimoPedido()
            binding.btnConfirmar.isEnabled = true
            val action = ResumenPedidoFragmentDirections.actionResumenToPago(
                orderNumber = pedido.orderNumber,
                totalAmount = pedido.totalPedido.toFloat(),
                emailComprador = session.getUserEmail() ?: ""
            )
            findNavController().navigate(action)
        }

        binding.btnConfirmar.setOnClickListener { confirmarPedido(session, userId) }

        // Detectar automáticamente la ubicación al abrir la pantalla
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            detectarUbicacion()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // ─── Ubicación ──────────────────────────────────────────────────────────

    /*
     * Obtiene la última ubicación conocida del dispositivo y anima la cámara del mapa
     * hacia esa posición con zoom de nivel de calle.
     * Muestra mensajes de estado durante el proceso y en caso de error.
     */
    private fun detectarUbicacion() {
        mostrarEstado(getString(R.string.msg_detecting_location))
        lifecycleScope.launch {
            try {
                val location = LocationHelper.getLastLocation(requireContext())
                if (location != null) {
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude), Constants.MAP_LOCATION_ZOOM
                        )
                    )
                } else {
                    mostrarEstado(getString(R.string.msg_location_not_found_map))
                }
            } catch (_: Exception) {
                mostrarEstado(getString(R.string.msg_location_error_map))
            }
        }
    }

    /*
     * Realiza geocodificación inversa sobre las coordenadas del centro del mapa
     * y rellena el campo de dirección con la dirección obtenida.
     * Cancela la geocodificación anterior si el mapa sigue moviéndose.
     * Consume: lat y lng (Double) con las coordenadas del centro del mapa.
     */
    private fun geocodearCentro(lat: Double, lng: Double) {
        geocodingJob?.cancel()
        geocodingJob = lifecycleScope.launch {
            mostrarEstado(getString(R.string.msg_getting_address))
            try {
                val address = LocationHelper.getAddressFromLocation(requireContext(), lat, lng)
                binding.etDireccion.setText(address)
                binding.tvEstadoUbicacion.gone()
            } catch (_: Exception) {
                binding.tvEstadoUbicacion.gone()
            }
        }
    }

    /*
     * Muestra un mensaje de estado en el indicador de ubicación (tvEstadoUbicacion).
     * Consume: mensaje (String) a mostrar.
     */
    private fun mostrarEstado(mensaje: String) {
        binding.tvEstadoUbicacion.text = mensaje
        binding.tvEstadoUbicacion.visible()
    }

    // ─── Datos de contacto ──────────────────────────────────────────────────

    /*
     * Recupera el número de teléfono del comprador desde Firestore
     * y lo pre-rellena en el campo correspondiente.
     * Usa el UID del usuario autenticado actualmente en Firebase Auth.
     */
    private fun cargarTelefonoFirestore() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        lifecycleScope.launch {
            try {
                val doc = FirebaseFirestore.getInstance()
                    .collection(Constants.FS_USUARIOS).document(uid).get().await()
                val telefono = doc.getString("telefono") ?: ""
                binding.etTelefono.setText(telefono)
            } catch (_: Exception) { }
        }
    }

    // ─── Crear pedido ────────────────────────────────────────────────────────

    /*
     * Valida que haya una dirección seleccionada y que el carrito no esté vacío,
     * construye los objetos DetallePedido a partir del carrito actual
     * y delega la creación del pedido al PedidoViewModel.
     * Deshabilita el botón durante el proceso para evitar envíos duplicados.
     * Consume: session (SessionManager) y userId (Long) del comprador.
     */
    private fun confirmarPedido(session: SessionManager, userId: Long) {
        val direccion = binding.etDireccion.text?.toString()?.trim() ?: ""
        if (direccion.isBlank()) {
            binding.root.showSnackbar(getString(R.string.msg_select_address_on_map))
            return
        }
        if (carritoItems.isEmpty()) {
            binding.root.showSnackbar(getString(R.string.msg_cart_empty_checkout))
            return
        }

        val productoMap = (productoViewModel.productosDisponibles.value ?: emptyList())
            .associateBy { it.id }
        val detalles = carritoItems.mapNotNull { item ->
            val producto = productoMap[item.productoId] ?: return@mapNotNull null
            DetallePedido(
                pedidoId = 0L,
                productoId = item.productoId,
                vendedorId = producto.vendedorId,
                nombreProducto = producto.nombre,
                presentacion = producto.presentacion,
                cantidad = item.cantidad,
                precioUnitario = item.precioAlMomento,
                subtotal = item.precioAlMomento * item.cantidad
            )
        }

        if (detalles.isEmpty()) {
            binding.root.showSnackbar(getString(R.string.msg_cart_items_error))
            return
        }

        // Deshabilitar botón para evitar múltiples pedidos mientras se procesa
        binding.btnConfirmar.isEnabled = false
        pedidoViewModel.crearPedido(userId, detalles, direccion, getString(R.string.label_payment_stripe))
    }

    // ─── Ciclo de vida del MapView ───────────────────────────────────────────

    // Delega el evento onResume al MapView para reanudar el renderizado del mapa
    override fun onResume() {
        super.onResume()
        _binding?.mapView?.onResume()
    }

    // Delega el evento onPause al MapView para pausar el renderizado y liberar recursos
    override fun onPause() {
        _binding?.mapView?.onPause()
        super.onPause()
    }

    // Notifica al MapView de baja memoria para que libere caché de teselas
    override fun onLowMemory() {
        super.onLowMemory()
        _binding?.mapView?.onLowMemory()
    }

    // Guarda el estado del MapView para restaurarlo en caso de recreación del Fragment
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.mapView?.onSaveInstanceState(outState)
    }

    // Destruye el MapView y libera el binding para evitar fugas de memoria
    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val BOGOTA_DEFAULT = com.google.android.gms.maps.model.LatLng(
            Constants.MAP_DEFAULT_LAT, Constants.MAP_DEFAULT_LNG
        )
    }
}
