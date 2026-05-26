package com.example.tomenaguita.ui.admin.usuarios

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentCrearUsuarioBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.LocationHelper
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.isValidColombian
import com.example.tomenaguita.utils.isValidEmail
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.UsuarioViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// Fragmento del modulo administrador para registrar un nuevo usuario en la plataforma.
// Permite ingresar nombre, email, telefono, contrasena y rol del nuevo usuario.
// Incluye un MapView de Google Maps para seleccionar la direccion del usuario de forma visual:
//   - Al desplazar el mapa, el geocodificador inverso convierte las coordenadas en una direccion textual.
//   - El icono de ubicacion del campo de direccion detecta la posicion actual del dispositivo.
// Valida los campos antes de llamar al ViewModel para crear el usuario en Firebase Auth y Firestore.
class CrearUsuarioFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_crear_usuario.xml
    private var _binding: FragmentCrearUsuarioBinding? = null
    private val binding get() = _binding!!

    // ViewModel de usuarios compartido a nivel de actividad; expone crearUsuario() y operationResult
    private val viewModel: UsuarioViewModel by activityViewModels()

    // Referencia al mapa de Google Maps; se inicializa de forma asincrona en getMapAsync
    private var googleMap: GoogleMap? = null

    // Marcador colocado en el mapa al tocar una ubicacion
    private var selectedMarker: Marker? = null

    // Job del geocodificador inverso; se cancela antes de iniciar una nueva geocodificacion
    // para evitar que respuestas tardias sobreescriban el resultado mas reciente
    private var geocodingJob: Job? = null

    // Launcher para solicitar el permiso de ubicacion precisa al usuario
    // Si se concede, detecta la ubicacion actual; si se deniega, muestra un mensaje informativo
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) detectarUbicacion()
            else binding.root.showSnackbar(getString(R.string.msg_location_permission_denied))
        }

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCrearUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el desplegable de roles, el MapView, el observador de resultado de operacion
    // y los listeners del icono de ubicacion y del boton de creacion.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura el adaptador del campo de rol con las opciones disponibles en mayuscula inicial
        val rolesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Constants.ROLES.map { it.replaceFirstChar { c -> c.uppercase() } }
        )
        binding.acRol.setAdapter(rolesAdapter)
        // El rol predeterminado al abrir el formulario es "comprador"
        binding.acRol.setText(getString(R.string.rol_comprador), false)

        // Inicializa el MapView pasandole el savedInstanceState para preservar el estado del mapa
        setupMapView(savedInstanceState)

        // Observa el resultado de la operacion de creacion de usuario (exito o error)
        // Consume el evento para evitar re-entregas tras rotacion de pantalla
        viewModel.operationResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            viewModel.clearOperationResult()
            binding.btnCrearUsuario.isEnabled = true
            result.fold(
                onSuccess = {
                    // Muestra el nombre del usuario creado y regresa al listado
                    binding.root.showSnackbar(
                        getString(R.string.msg_user_created, binding.etNombre.text.toString().trim())
                    )
                    findNavController().popBackStack()
                },
                onFailure = {
                    binding.root.showSnackbar(it.message ?: getString(R.string.error_field_required))
                }
            )
        }

        // El icono al final del campo de direccion solicita el permiso de ubicacion y centra el mapa
        binding.tilDireccion.setEndIconOnClickListener { solicitarUbicacion() }

        // Valida los campos y llama al ViewModel para crear el usuario
        binding.btnCrearUsuario.setOnClickListener { crearUsuario() }
    }

    // ─── Mapa ────────────────────────────────────────────────────────────────

    // Inicializa el MapView con el ciclo de vida adecuado y configura la camara inicial,
    // los gestos de desplazamiento, el listener de toque para colocar marcadores
    // y el listener de camara inactiva para geocodificar la posicion central.
    // Consume: savedInstanceState para restaurar el estado interno del MapView
    private fun setupMapView(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()

        // Evita que el scroll del fragment padre capture los toques dentro del mapa
        binding.mapView.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN,
                android.view.MotionEvent.ACTION_MOVE -> v.parent.requestDisallowInterceptTouchEvent(true)
                android.view.MotionEvent.ACTION_UP,
                android.view.MotionEvent.ACTION_CANCEL -> v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        binding.mapView.getMapAsync { map ->
            googleMap = map
            // Habilita los gestos de desplazamiento y zoom en el mapa
            map.uiSettings.isScrollGesturesEnabled = true
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isZoomGesturesEnabled = true

            // Centra el mapa en Bogota como ubicacion inicial predeterminada
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(BOGOTA_DEFAULT, Constants.MAP_CITY_ZOOM))

            // Al tocar el mapa, coloca un marcador en la posicion pulsada y anima la camara hacia ella
            map.setOnMapClickListener { latLng ->
                selectedMarker?.remove()
                selectedMarker = map.addMarker(MarkerOptions().position(latLng))
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            }

            // Cuando la camara termina de moverse, geocodifica la posicion central y actualiza el campo de direccion
            map.setOnCameraIdleListener {
                val target = map.cameraPosition.target
                geocodearDireccion(target.latitude, target.longitude)
            }
        }
    }

    // Verifica si el permiso de ubicacion esta concedido antes de detectar la posicion.
    // Si no esta concedido, lanza el launcher de solicitud de permiso.
    private fun solicitarUbicacion() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            detectarUbicacion()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Obtiene la ultima ubicacion conocida del dispositivo y centra el mapa en ella.
    // Muestra un mensaje de estado mientras detecta la ubicacion.
    // Si no se puede obtener la ubicacion, muestra un mensaje de error.
    private fun detectarUbicacion() {
        binding.tvLocationStatus.text = getString(R.string.msg_detecting_location)
        binding.tvLocationStatus.visible()
        lifecycleScope.launch {
            val location = LocationHelper.getLastLocation(requireContext())
            if (location != null) {
                googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(location.latitude, location.longitude), Constants.MAP_LOCATION_ZOOM
                    )
                )
            } else {
                binding.tvLocationStatus.text = getString(R.string.msg_location_error)
            }
        }
    }

    // Convierte coordenadas geograficas en una direccion textual usando geocodificacion inversa.
    // Cancela el Job anterior para evitar condiciones de carrera si el usuario mueve el mapa rapidamente.
    // Al obtener la direccion, la escribe en el campo etDireccion y oculta el estado de ubicacion.
    // Consume: lat y lng (coordenadas del centro actual de la camara del mapa)
    private fun geocodearDireccion(lat: Double, lng: Double) {
        geocodingJob?.cancel()
        geocodingJob = lifecycleScope.launch {
            try {
                val address = LocationHelper.getAddressFromLocation(requireContext(), lat, lng)
                binding.etDireccion.setText(address)
                binding.tvLocationStatus.gone()
            } catch (_: Exception) {
                binding.tvLocationStatus.gone()
            }
        }
    }

    // ─── Crear usuario ───────────────────────────────────────────────────────

    // Lee y valida los campos del formulario antes de llamar al ViewModel para crear el usuario.
    // Valida: nombre con al menos 3 caracteres, email con formato valido,
    //         telefono colombiano valido y contrasena de al menos 8 caracteres.
    // Si todas las validaciones pasan, deshabilita el boton y llama a viewModel.crearUsuario().
    private fun crearUsuario() {
        val nombre    = binding.etNombre.text.toString().trim()
        val email     = binding.etEmail.text.toString().trim()
        val telefono  = binding.etTelefono.text.toString().trim()
        val password  = binding.etPassword.text.toString()
        val rol       = binding.acRol.text.toString().lowercase()
        val direccion = binding.etDireccion.text.toString().trim()

        // Limpia los errores previos de todos los campos antes de validar
        binding.tilNombre.error   = null
        binding.tilEmail.error    = null
        binding.tilTelefono.error = null
        binding.tilPassword.error = null

        if (nombre.length < 3)       { binding.tilNombre.error   = getString(R.string.error_name_short);     return }
        if (!email.isValidEmail())   { binding.tilEmail.error    = getString(R.string.error_email_invalid);  return }
        if (!telefono.isValidColombian()) { binding.tilTelefono.error = getString(R.string.error_phone_invalid); return }
        if (password.length < 8)    { binding.tilPassword.error = getString(R.string.error_password_short); return }

        // Deshabilita el boton para evitar multiples envios mientras se procesa la operacion
        binding.btnCrearUsuario.isEnabled = false
        viewModel.crearUsuario(nombre, email, telefono, password, rol, direccion)
    }

    // ─── Ciclo de vida del MapView ───────────────────────────────────────────
    // El MapView tiene su propio ciclo de vida que debe delegarse manualmente desde el Fragment.

    // Persiste el estado interno del MapView (posicion de camara, zoom, etc.) ante rotaciones
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.mapView?.onSaveInstanceState(outState)
    }

    // Reanuda el MapView cuando el fragment vuelve al primer plano
    override fun onResume() {
        super.onResume()
        _binding?.mapView?.onResume()
    }

    // Pausa el MapView cuando el fragment pasa al segundo plano
    override fun onPause() {
        _binding?.mapView?.onPause()
        super.onPause()
    }

    // Notifica al MapView sobre condiciones de memoria baja para que libere recursos de tiles
    override fun onLowMemory() {
        super.onLowMemory()
        _binding?.mapView?.onLowMemory()
    }

    // Destruye el MapView y libera la referencia al binding para evitar fugas de memoria
    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Coordenadas del centro de Bogota usadas como posicion inicial del mapa
        private val BOGOTA_DEFAULT = LatLng(4.7110, -74.0721)
    }
}
