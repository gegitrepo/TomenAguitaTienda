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
import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.databinding.FragmentResumenPedidoBinding
import com.example.tomenaguita.ui.adapter.DetallePedidoAdapter
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ResumenPedidoFragment : Fragment() {

    private var _binding: FragmentResumenPedidoBinding? = null
    private val binding get() = _binding!!

    private val carritoViewModel: CarritoViewModel by activityViewModels()
    private val productoViewModel: ProductoViewModel by activityViewModels()
    private val pedidoViewModel: PedidoViewModel by activityViewModels()

    private var googleMap: GoogleMap? = null
    private var carritoItems: List<com.example.tomenaguita.data.database.entity.CarritoItem> = emptyList()
    private var geocodingJob: Job? = null

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) detectarUbicacion()
            else mostrarEstado("Permiso denegado. Mueve el mapa para seleccionar tu dirección.")
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResumenPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        val userId = session.getUserId()

        binding.etNombre.setText(session.getUserNombre() ?: "")
        cargarTelefonoFirestore()

        // Inicializar MapView — onResume() inmediato para cargar teselas aunque
        // el Fragment no haya pasado por su propio onResume todavía.
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

            // Centro por defecto: Bogotá
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(BOGOTA_DEFAULT, 12f))

            // Cada vez que el mapa se detiene, geocodificar el centro
            map.setOnCameraIdleListener {
                val target = map.cameraPosition.target
                geocodearCentro(target.latitude, target.longitude)
            }
        }

        val detalleAdapter = DetallePedidoAdapter()
        binding.rvResumen.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResumen.adapter = detalleAdapter

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

        // Auto-detectar ubicación al abrir
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            detectarUbicacion()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // ─── Ubicación ──────────────────────────────────────────────────────────

    private fun detectarUbicacion() {
        mostrarEstado("Detectando ubicación…")
        lifecycleScope.launch {
            try {
                val location = LocationHelper.getLastLocation(requireContext())
                if (location != null) {
                    // Animar al mapa → camera idle → geocodifica y rellena el campo
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude), 15f
                        )
                    )
                    // tvEstadoUbicacion se ocultará cuando geocodearCentro termine
                } else {
                    mostrarEstado("No se detectó ubicación. Mueve el mapa para seleccionar tu dirección.")
                }
            } catch (_: Exception) {
                mostrarEstado("Error de ubicación. Mueve el mapa para seleccionar tu dirección.")
            }
        }
    }

    private fun geocodearCentro(lat: Double, lng: Double) {
        geocodingJob?.cancel()
        geocodingJob = lifecycleScope.launch {
            mostrarEstado("Obteniendo dirección…")
            try {
                val address = LocationHelper.getAddressFromLocation(requireContext(), lat, lng)
                binding.etDireccion.setText(address)
                binding.tvEstadoUbicacion.gone()
            } catch (_: Exception) {
                binding.tvEstadoUbicacion.gone()
            }
        }
    }

    private fun mostrarEstado(mensaje: String) {
        binding.tvEstadoUbicacion.text = mensaje
        binding.tvEstadoUbicacion.visible()
    }

    // ─── Datos de contacto ──────────────────────────────────────────────────

    private fun cargarTelefonoFirestore() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        lifecycleScope.launch {
            try {
                val doc = FirebaseFirestore.getInstance()
                    .collection("usuarios").document(uid).get().await()
                val telefono = doc.getString("telefono") ?: ""
                binding.etTelefono.setText(telefono)
            } catch (_: Exception) { }
        }
    }

    // ─── Crear pedido ────────────────────────────────────────────────────────

    private fun confirmarPedido(session: SessionManager, userId: Long) {
        val direccion = binding.etDireccion.text?.toString()?.trim() ?: ""
        if (direccion.isBlank()) {
            binding.root.showSnackbar("Mueve el mapa para seleccionar tu dirección de entrega")
            return
        }
        if (carritoItems.isEmpty()) {
            binding.root.showSnackbar("El carrito está vacío")
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
            binding.root.showSnackbar("No se pudieron procesar los productos del carrito")
            return
        }

        binding.btnConfirmar.isEnabled = false
        pedidoViewModel.crearPedido(userId, detalles, direccion, "Mercado Pago")
    }

    // ─── Ciclo de vida del MapView ───────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        _binding?.mapView?.onResume()
    }

    override fun onPause() {
        _binding?.mapView?.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _binding?.mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.mapView?.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val BOGOTA_DEFAULT = com.google.android.gms.maps.model.LatLng(4.7110, -74.0721)
    }
}
