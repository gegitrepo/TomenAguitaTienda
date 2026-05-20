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

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()
    private var googleMap: GoogleMap? = null
    private var geocodingJob: Job? = null

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) centrarEnUbicacion()
            else binding.root.showSnackbar("Permiso de ubicación denegado")
        }

    // ─── Ciclo de vida ───────────────────────────────────────────────────────

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

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(BOGOTA_DEFAULT, 12f))

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

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    // ─── Observadores ────────────────────────────────────────────────────────

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

    private fun solicitarCentrarUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            centrarEnUbicacion()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

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

    private fun doRegister() {
        val nombre = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

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

        binding.btnRegister.isEnabled = false
        viewModel.register(nombre, email, telefono, direccion, password)
    }

    companion object {
        private val BOGOTA_DEFAULT = LatLng(4.7110, -74.0721)
    }
}
