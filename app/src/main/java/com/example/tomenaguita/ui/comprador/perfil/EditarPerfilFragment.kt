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
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.LocationHelper
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.StorageHelper
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.visible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/*
 * Pantalla de edición del perfil del comprador.
 * Permite modificar el nombre, teléfono y dirección de entrega del usuario.
 * La dirección se puede actualizar mediante un mapa interactivo de Google Maps
 * con geocodificación inversa automática del centro del mapa.
 * La foto de perfil se puede cambiar tomando una foto con la cámara,
 * seleccionando una imagen de la galería o eliminando la foto actual.
 * Los cambios se guardan en Firestore y, si aplica, en Firebase Storage.
 */
class EditarPerfilFragment : Fragment() {

    private var _binding: FragmentEditarPerfilBinding? = null
    private val binding get() = _binding!!

    // Gestor de sesión local para leer y actualizar el nombre del usuario
    private lateinit var session: SessionManager

    // URI de la imagen seleccionada desde galería o cámara, null si no se ha elegido ninguna
    private var selectedImageUri: Uri? = null

    // URI temporal donde la cámara escribe la foto; se guarda en el estado del Fragment
    private var pendingCameraUri: Uri? = null

    // Indica si el usuario quiere eliminar la foto de perfil actual
    private var deletePhoto = false

    // URL actual de la foto de perfil en Firebase Storage, usada para eliminarla si se borra
    private var currentFotoUrl: String? = null

    // Referencia al mapa de Google Maps para controlar cámara y marcador
    private var googleMap: GoogleMap? = null

    // Marcador que indica la posición de entrega seleccionada por el usuario en el mapa
    private var selectedMarker: Marker? = null

    // Job cancelable para la geocodificación inversa del centro del mapa
    private var geocodingJob: Job? = null

    // ─── Launchers ──────────────────────────────────────────────────────────

    /*
     * Launcher que solicita permiso de cámara.
     * Si se concede, abre la cámara; si se deniega, muestra un aviso.
     */
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) abrirCamara()
            else binding.root.showSnackbar(getString(R.string.msg_camera_permission_denied))
        }

    /*
     * Launcher que solicita permiso de ubicación precisa.
     * Si se concede, centra el mapa en la ubicación actual; si se deniega, muestra un aviso.
     */
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) centrarEnUbicacion()
            else binding.root.showSnackbar(getString(R.string.msg_location_permission_denied))
        }

    /*
     * Launcher que abre el selector de imágenes de la galería.
     * Al recibir una URI válida, la asigna y muestra la imagen seleccionada.
     */
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            selectedImageUri = uri
            deletePhoto = false
            mostrarFotoSeleccionada(uri)
        }

    /*
     * Launcher que captura una foto con la cámara y la guarda en pendingCameraUri.
     * Si la captura es exitosa, asigna la URI y muestra la imagen resultante.
     */
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val uri = pendingCameraUri ?: return@registerForActivityResult
            if (!success) return@registerForActivityResult
            selectedImageUri = uri
            deletePhoto = false
            mostrarFotoSeleccionada(uri)
        }

    // ─── Ciclo de vida ───────────────────────────────────────────────────────

    /*
     * Guarda la URI de la cámara pendiente y el estado del MapView
     * para restaurarlos si el sistema recrea el Fragment.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pendingCameraUri?.let { outState.putString(KEY_CAMERA_URI, it.toString()) }
        _binding?.mapView?.onSaveInstanceState(outState)
    }

    // Infla el layout del fragmento usando ViewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditarPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Restaura la URI de cámara si existía, inicializa el MapView, configura el mapa,
     * pre-rellena los campos con los datos de sesión y carga los datos actualizados desde Firestore.
     * Asigna los listeners de todos los botones de la pantalla.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pendingCameraUri = savedInstanceState?.getString(KEY_CAMERA_URI)?.let { Uri.parse(it) }
        session = SessionManager(requireContext())

        // MapView — onResume() inmediato para cargar teselas sin esperar al ciclo del Fragment
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

            // Cuando la cámara deja de moverse, geocodificar el centro para actualizar la dirección
            map.setOnCameraIdleListener {
                val target = map.cameraPosition.target
                geocodearCentro(target.latitude, target.longitude)
            }
        }

        // Pre-rellenar el nombre desde la sesión local antes de la llamada a Firestore
        binding.etNombre.setText(session.getUserNombre() ?: "")
        cargarDatosFirestore()

        binding.btnCamara.setOnClickListener { solicitarCamara() }
        binding.btnGaleria.setOnClickListener { galleryLauncher.launch("image/*") }
        binding.btnBorrarFoto.setOnClickListener { borrarFoto() }
        // El icono final del campo de dirección dispara la detección de ubicación
        binding.tilDireccion.setEndIconOnClickListener { solicitarCentrarUbicacion() }
        binding.btnGuardar.setOnClickListener { guardar() }
    }

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

    // Destruye el MapView y libera el binding para evitar fugas de memoria
    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
        _binding = null
    }

    // ─── Foto de perfil ──────────────────────────────────────────────────────

    /*
     * Muestra la imagen seleccionada (galería o cámara) en el avatar circular
     * y hace visible el botón de borrar foto.
     * Consume: uri (Uri) de la imagen a previsualizar.
     */
    private fun mostrarFotoSeleccionada(uri: Uri) {
        Glide.with(this).load(uri).skipMemoryCache(true).circleCrop().into(binding.ivAvatar)
        binding.btnBorrarFoto.visible()
    }

    /*
     * Marca la foto para eliminación, restablece el avatar al icono por defecto
     * y oculta el botón de borrar.
     * No realiza la eliminación en Firebase Storage hasta que el usuario pulse "Guardar".
     */
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

    /*
     * Verifica si el permiso de cámara está concedido antes de abrir la cámara.
     * Si no lo está, lanza el launcher de solicitud de permiso.
     */
    private fun solicitarCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            abrirCamara()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    /*
     * Crea una URI de destino para la foto mediante StorageHelper y lanza la cámara.
     * La URI se almacena en pendingCameraUri para recuperarla si el sistema recrea el Fragment.
     */
    private fun abrirCamara() {
        pendingCameraUri = StorageHelper.createImageUri(requireContext())
        cameraLauncher.launch(pendingCameraUri!!)
    }

    // ─── Ubicación ──────────────────────────────────────────────────────────

    /*
     * Verifica si el permiso de ubicación precisa está concedido antes de centrar el mapa.
     * Si no lo está, lanza el launcher de solicitud de permiso.
     */
    private fun solicitarCentrarUbicacion() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            centrarEnUbicacion()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /*
     * Obtiene la última ubicación conocida y anima la cámara del mapa hacia esa posición.
     * Al terminar el movimiento, camera idle dispara geocodearCentro para rellenar la dirección.
     * Muestra mensajes de estado durante el proceso y en caso de error.
     */
    private fun centrarEnUbicacion() {
        binding.tvLocationStatus.text = getString(R.string.msg_detecting_location)
        binding.tvLocationStatus.visible()
        lifecycleScope.launch {
            try {
                val location = LocationHelper.getLastLocation(requireContext())
                if (location != null) {
                    // Animar la cámara; camera idle se dispara al detenerse y geocodifica
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude), Constants.MAP_LOCATION_ZOOM
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

    /*
     * Realiza geocodificación inversa sobre las coordenadas del centro del mapa
     * y actualiza el campo de dirección con la dirección obtenida.
     * Cancela la geocodificación anterior si el mapa sigue en movimiento.
     * Consume: lat y lng (Double) con las coordenadas del centro del mapa.
     */
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

    /*
     * Carga desde Firestore el teléfono, la dirección y la URL de foto de perfil del usuario.
     * Pre-rellena los campos con los valores obtenidos y muestra la foto si existe.
     * Usa el UID del usuario autenticado actualmente en Firebase Auth.
     */
    private fun cargarDatosFirestore() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        lifecycleScope.launch {
            try {
                val doc = FirebaseFirestore.getInstance()
                    .collection(Constants.FS_USUARIOS).document(uid).get().await()
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

    /*
     * Recoge los valores actuales de los campos, sube la nueva foto a Firebase Storage
     * si se seleccionó una (o elimina la existente si se marcó borrado), actualiza el
     * documento del usuario en Firestore y refresca el nombre en la sesión local.
     * Deshabilita el botón durante el proceso para evitar guardados duplicados.
     * Al finalizar correctamente, muestra una confirmación y regresa a la pantalla anterior.
     */
    private fun guardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            binding.root.showSnackbar(getString(R.string.msg_login_failed))
            return
        }

        // Deshabilitar el botón para evitar múltiples guardados simultáneos
        binding.btnGuardar.isEnabled = false

        lifecycleScope.launch {
            try {
                // Mapa base de campos a actualizar en Firestore
                val campos = mutableMapOf<String, Any>(
                    "nombre" to nombre,
                    "telefono" to telefono,
                    "direccion" to direccion
                )

                when {
                    selectedImageUri != null -> {
                        // Subir la nueva imagen a Firebase Storage y obtener la URL pública
                        val fotoUrl = withContext(Dispatchers.IO) {
                            StorageHelper.uploadProfileImage(uid, selectedImageUri!!, requireContext())
                        }
                        campos["fotoUrl"] = fotoUrl
                    }
                    deletePhoto -> {
                        // Limpiar el campo en Firestore y eliminar el archivo en Storage
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

                // Actualizar el documento del usuario en Firestore con todos los campos
                FirebaseFirestore.getInstance()
                    .collection(Constants.FS_USUARIOS).document(uid)
                    .update(campos).await()

                // Refrescar el nombre en la sesión local para que PerfilFragment lo muestre actualizado
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
                binding.root.showSnackbar(getString(R.string.msg_image_upload_error, e.message))
            }
        }
    }

    companion object {
        // Clave usada para persistir la URI de la cámara en el Bundle del estado del Fragment
        private const val KEY_CAMERA_URI = "pending_camera_uri"

        /*
         * HARDCODED: coordenadas del centro de Bogotá como posición inicial del mapa.
         * Valor geográfico fijo justificado por el mercado objetivo del negocio (Colombia).
         */
        private val BOGOTA_DEFAULT = LatLng(4.7110, -74.0721)
    }
}
