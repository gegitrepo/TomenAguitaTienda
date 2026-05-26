package com.example.tomenaguita.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityRegisterBinding
import com.example.tomenaguita.ui.comprador.CompradorMainActivity
import com.example.tomenaguita.utils.LocationHelper
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.isValidColombian
import com.example.tomenaguita.utils.isValidEmail
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.AuthViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/*
 * Pantalla de registro de nuevos usuarios compradores en TomenAgüita.
 * Muestra un formulario con nombre, correo, teléfono, contraseña y un mapa
 * interactivo donde el usuario elige su dirección de entrega por defecto.
 * Al registrarse exitosamente navega directamente a CompradorMainActivity.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    // ViewModel compartido que gestiona las operaciones de autenticación (registro)
    private val viewModel: AuthViewModel by viewModels()

    // Referencia al mapa de Google Maps para controlar la cámara y escuchar eventos
    private var googleMap: GoogleMap? = null

    // Job que permite cancelar una geocodificación en curso si el usuario mueve el mapa
    private var geocodingJob: Job? = null

    /*
     * Launcher que solicita el permiso de ubicación precisa al usuario.
     * Si se concede, centra el mapa en la ubicación actual; si se deniega, muestra un aviso.
     */
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) centrarEnUbicacion()
            else binding.root.showSnackbar("Permiso de ubicación denegado")
        }

    // ─── Ciclo de vida ───────────────────────────────────────────────────────

    /*
     * Infla el layout, inicializa el MapView, configura los gestos táctiles del mapa,
     * registra los observadores y asigna los listeners de los controles de la pantalla.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mostrar el contenedor del mapa y inicializar MapView
        binding.mapContainer.visible()
        binding.mapView.onCreate(savedInstanceState)

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
            // esta parte no supe como moverla sin romper el codigo asi que la dejo funcional aca
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(BOGOTA_DEFAULT, 700f))

            // Cada vez que el mapa se detiene de mover, geocodifica la posición central
            map.setOnCameraIdleListener {
                val target = map.cameraPosition.target
                geocodearCentro(target.latitude, target.longitude)
            }
        }

        setupObservers()
        binding.btnRegister.setOnClickListener { doRegister() }
        binding.tvLogin.setOnClickListener { finish() }
        binding.tilDireccion.setEndIconOnClickListener { solicitarCentrarUbicacion() }
    }

    // Delega el evento onResume al MapView para que el mapa siga funcionando correctamente
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    // Delega el evento onPause al MapView para liberar recursos de renderizado
    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    // Notifica al MapView de baja memoria para que libere caché de teselas
    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    // Guarda el estado del MapView para restaurarlo en caso de recreación de la Activity
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    // Destruye el MapView para evitar fugas de memoria al cerrar la Activity
    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    // ─── Observadores ────────────────────────────────────────────────────────

    /*
     * Observa el resultado del registro emitido por el ViewModel.
     * En caso de éxito navega a CompradorMainActivity limpiando el back stack.
     * En caso de error muestra un Snackbar con el mensaje de la excepción.
     */
    private fun setupObservers() {
        viewModel.registerResult.observe(this) { result ->
            binding.btnRegister.isEnabled = true
            result.fold(
                onSuccess = {
                    startActivity(Intent(this, CompradorMainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                },
                onFailure = { binding.root.showSnackbar(it.message ?: getString(R.string.error_field_required)) }
            )
        }
    }

    // ─── Ubicación ──────────────────────────────────────────────────────────

    /*
     * Verifica si el permiso de ubicación precisa ya está concedido.
     * Si lo está, centra el mapa; si no, lanza el launcher para solicitarlo.
     */
    private fun solicitarCentrarUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            centrarEnUbicacion()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /*
     * Obtiene la última ubicación conocida del dispositivo y anima la cámara del mapa
     * hacia esa posición con zoom de calle. Si no se puede obtener la ubicación,
     * muestra un mensaje de error en el indicador de estado.
     */
    private fun centrarEnUbicacion() {
        binding.tvLocationStatus.text = getString(R.string.msg_detecting_location)
        binding.tvLocationStatus.visible()
        lifecycleScope.launch {
            try {
                val location = LocationHelper.getLastLocation(this@RegisterActivity)
                if (location != null) {
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude), 15f
                        )
                    )
                    // tvLocationStatus se oculta cuando geocodearCentro termine
                } else {
                    binding.tvLocationStatus.text = getString(R.string.msg_location_error)
                }
            } catch (_: Exception) {
                binding.tvLocationStatus.text = getString(R.string.msg_location_error)
            }
        }
    }

    /*
     * Convierte las coordenadas del centro del mapa a una dirección legible usando
     * geocodificación inversa y la muestra en el campo de dirección.
     * Cancela cualquier job de geocodificación anterior antes de iniciar uno nuevo.
     * Consume: latitud y longitud como Double.
     */
    private fun geocodearCentro(lat: Double, lng: Double) {
        geocodingJob?.cancel()
        geocodingJob = lifecycleScope.launch {
            try {
                val address = LocationHelper.getAddressFromLocation(this@RegisterActivity, lat, lng)
                binding.etDireccion.setText(address)
                binding.tvLocationStatus.gone()
            } catch (_: Exception) {
                binding.tvLocationStatus.gone()
            }
        }
    }

    // ─── Registro ────────────────────────────────────────────────────────────

    /*
     * Valida todos los campos del formulario de registro y, si son correctos,
     * deshabilita el botón para evitar envíos duplicados y llama al ViewModel.
     * Campos validados: nombre (>=3 chars), email, teléfono colombiano,
     * contraseña (>=8 chars), coincidencia de contraseñas y aceptación de términos.
     */
    private fun doRegister() {
        val nombre = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // Limpiar errores previos en todos los campos antes de revalidar
        binding.tilNombre.error = null
        binding.tilEmail.error = null
        binding.tilTelefono.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null

        if (nombre.length < 3) { binding.tilNombre.error = getString(R.string.error_name_short); return }
        if (!email.isValidEmail()) { binding.tilEmail.error = getString(R.string.error_email_invalid); return }
        if (!telefono.isValidColombian()) { binding.tilTelefono.error = getString(R.string.error_phone_invalid); return }
        if (password.length < 8) { binding.tilPassword.error = getString(R.string.error_password_short); return }
        if (password != confirmPassword) { binding.tilConfirmPassword.error = getString(R.string.error_passwords_no_match); return }
        if (!binding.cbTerminos.isChecked) { binding.root.showSnackbar(getString(R.string.error_terms_required)); return }

        // Deshabilitar botón para evitar múltiples envíos mientras se procesa el registro
        binding.btnRegister.isEnabled = false
        viewModel.register(nombre, email, telefono, direccion, password)
    }

    companion object {
        /*
         * HARDCODED: coordenadas del centro de Bogotá como posición inicial del mapa.
         * Valor geográfico fijo justificado por el mercado objetivo del negocio (Colombia)
         * igual si lo muevo no carga el mapa en la app.
         */
        private val BOGOTA_DEFAULT = LatLng(4.7110, -74.0721)
    }
}
