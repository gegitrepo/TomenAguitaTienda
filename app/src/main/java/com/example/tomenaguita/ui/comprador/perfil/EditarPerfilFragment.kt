package com.example.tomenaguita.ui.comprador.perfil

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentEditarPerfilBinding
import com.example.tomenaguita.utils.LocationHelper
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.StorageHelper
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.visible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditarPerfilFragment : Fragment() {

    private var _binding: FragmentEditarPerfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var session: SessionManager

    private var selectedImageUri: Uri? = null
    private var pendingCameraUri: Uri? = null
    private var deletePhoto = false
    private var currentFotoUrl: String? = null
    private var googleMap: GoogleMap? = null
    private var geocodingJob: Job? = null

    // ─── Launchers ──────────────────────────────────────────────────────────

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) abrirCamara()
            else binding.root.showSnackbar("Permiso de cámara denegado")
        }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) centrarEnUbicacion()
            else binding.root.showSnackbar("Permiso de ubicación denegado")
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            selectedImageUri = uri
            deletePhoto = false
            mostrarFotoSeleccionada(uri)
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val uri = pendingCameraUri ?: return@registerForActivityResult
            if (!success) return@registerForActivityResult
            selectedImageUri = uri
            deletePhoto = false
            mostrarFotoSeleccionada(uri)
        }

    // ─── Ciclo de vida ───────────────────────────────────────────────────────

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pendingCameraUri?.let { outState.putString(KEY_CAMERA_URI, it.toString()) }
        _binding?.mapView?.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditarPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pendingCameraUri = savedInstanceState?.getString(KEY_CAMERA_URI)?.let { Uri.parse(it) }
        session = SessionManager(requireContext())

        // MapView — onResume() inmediato para cargar teselas
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

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(BOGOTA_DEFAULT, 12f))

            map.setOnCameraIdleListener {
                val target = map.cameraPosition.target
                geocodearCentro(target.latitude, target.longitude)
            }
        }

        binding.etNombre.setText(session.getUserNombre() ?: "")
        cargarDatosFirestore()

        binding.btnCamara.setOnClickListener { solicitarCamara() }
        binding.btnGaleria.setOnClickListener { galleryLauncher.launch("image/*") }
        binding.btnBorrarFoto.setOnClickListener { borrarFoto() }
        binding.tilDireccion.setEndIconOnClickListener { solicitarCentrarUbicacion() }
        binding.btnGuardar.setOnClickListener { guardar() }
    }

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

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
        _binding = null
    }

    // ─── Foto de perfil ──────────────────────────────────────────────────────

    private fun mostrarFotoSeleccionada(uri: Uri) {
        Glide.with(this).load(uri).skipMemoryCache(true).circleCrop().into(binding.ivAvatar)
        binding.btnBorrarFoto.visible()
    }

    private fun borrarFoto() {
        if (selectedImageUri == null && currentFotoUrl.isNullOrEmpty()) {
            binding.root.showSnackbar(getString(R.string.msg_no_photo_to_delete))
            return
        }
        selectedImageUri = null
        deletePhoto = true
        Glide.with(this).load(R.drawable.ic_profile).circleCrop().into(binding.ivAvatar)
        binding.btnBorrarFoto.gone()
    }

    // ─── Cámara ──────────────────────────────────────────────────────────────

    private fun solicitarCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            abrirCamara()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun abrirCamara() {
        pendingCameraUri = StorageHelper.createImageUri(requireContext())
        cameraLauncher.launch(pendingCameraUri!!)
    }

    // ─── Ubicación ──────────────────────────────────────────────────────────

    private fun solicitarCentrarUbicacion() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
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
                val location = LocationHelper.getLastLocation(requireContext())
                if (location != null) {
                    // Animar la cámara → camera idle se dispara → geocodifica y rellena campo
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude), 15f
                        )
                    )
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
                val address = LocationHelper.getAddressFromLocation(requireContext(), lat, lng)
                binding.etDireccion.setText(address)
                binding.tvLocationStatus.gone()
            } catch (_: Exception) {
                binding.tvLocationStatus.gone()
            }
        }
    }

    // ─── Firestore ──────────────────────────────────────────────────────────

    private fun cargarDatosFirestore() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        lifecycleScope.launch {
            try {
                val doc = FirebaseFirestore.getInstance()
                    .collection("usuarios").document(uid).get().await()
                binding.etTelefono.setText(doc.getString("telefono") ?: "")
                binding.etDireccion.setText(doc.getString("direccion") ?: "")
                val url = doc.getString("fotoUrl")?.takeIf { it.isNotEmpty() }
                currentFotoUrl = url
                if (url != null) {
                    Glide.with(this@EditarPerfilFragment)
                        .load(url).circleCrop().into(binding.ivAvatar)
                    binding.btnBorrarFoto.visible()
                }
            } catch (_: Exception) { }
        }
    }

    // ─── Guardar ────────────────────────────────────────────────────────────

    private fun guardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            binding.root.showSnackbar(getString(R.string.msg_login_failed))
            return
        }

        binding.btnGuardar.isEnabled = false

        lifecycleScope.launch {
            try {
                val campos = mutableMapOf<String, Any>(
                    "nombre" to nombre,
                    "telefono" to telefono,
                    "direccion" to direccion
                )

                when {
                    selectedImageUri != null -> {
                        val fotoUrl = withContext(Dispatchers.IO) {
                            StorageHelper.uploadProfileImage(uid, selectedImageUri!!, requireContext())
                        }
                        campos["fotoUrl"] = fotoUrl
                    }
                    deletePhoto -> {
                        campos["fotoUrl"] = ""
                        currentFotoUrl?.takeIf { it.isNotEmpty() }?.let { url ->
                            try {
                                withContext(Dispatchers.IO) {
                                    FirebaseStorage.getInstance()
                                        .getReferenceFromUrl(url).delete().await()
                                }
                            } catch (_: Exception) { }
                        }
                    }
                }

                FirebaseFirestore.getInstance()
                    .collection("usuarios").document(uid)
                    .update(campos).await()

                session.saveSession(
                    session.getUserId(),
                    session.getUserRol() ?: "comprador",
                    session.getUserEmail() ?: "",
                    nombre
                )

                binding.root.showSnackbar(
                    if (deletePhoto) getString(R.string.msg_photo_deleted)
                    else getString(R.string.msg_profile_updated)
                )
                findNavController().popBackStack()

            } catch (e: Exception) {
                binding.btnGuardar.isEnabled = true
                binding.root.showSnackbar("Error al guardar: ${e.message}")
            }
        }
    }

    companion object {
        private const val KEY_CAMERA_URI = "pending_camera_uri"
        private val BOGOTA_DEFAULT = LatLng(4.7110, -74.0721)
    }
}
