package com.example.tomenaguita.ui.vendedor.productos

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.FragmentCrearEditarProductoBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.StorageHelper
import com.example.tomenaguita.utils.showSnackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

// Fragmento compartido entre el vendedor y el administrador para crear o editar un producto.
// Determina el modo de operacion segun el argumento de navegacion productoId:
//   - productoId == -1L → modo creacion (producto nuevo)
//   - productoId != -1L → modo edicion (carga el producto existente)
// Permite seleccionar una imagen desde la galeria o tomarla con la camara,
// subir la imagen a Firebase Storage y guardar los datos del producto via ProductoViewModel.
class CrearEditarProductoFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_crear_editar_producto.xml
    private var _binding: FragmentCrearEditarProductoBinding? = null
    private val binding get() = _binding!!

    // Argumentos de navegacion que contienen el ID del producto (o -1L si es creacion)
    private val args: CrearEditarProductoFragmentArgs by navArgs()

    // Indica si el fragmento esta en modo edicion; es false cuando productoId es -1L
    private val isEditing get() = args.productoId != -1L

    // ViewModel compartido a nivel de actividad que gestiona las operaciones sobre productos
    private val viewModel: com.example.tomenaguita.viewmodel.ProductoViewModel by activityViewModels()

    // URI de la imagen seleccionada por el usuario (galeria o camara); null si no se selecciono ninguna
    private var selectedImageUri: Uri? = null

    // URI temporal generada antes de abrir la camara; se preserva en savedInstanceState para sobrevivir rotaciones
    private var pendingCameraUri: Uri? = null

    // Launcher para solicitar el permiso de camara al usuario
    // Si se concede, abre la camara; si se deniega, muestra un mensaje informativo
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) abrirCamara()
            else binding.root.showSnackbar(getString(R.string.msg_camera_permission_denied))
        }

    // Launcher para seleccionar una imagen de la galeria del dispositivo
    // Al obtener la URI actualiza selectedImageUri y muestra la imagen en la vista previa
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            selectedImageUri = uri
            Glide.with(this).load(uri).centerCrop().into(binding.ivFotoProducto)
        }

    // Launcher para capturar una foto con la camara del dispositivo
    // Usa pendingCameraUri como destino del archivo; si la captura fue exitosa muestra la imagen
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val uri = pendingCameraUri ?: return@registerForActivityResult
            if (!success) return@registerForActivityResult
            selectedImageUri = uri
            Glide.with(this).load(uri).skipMemoryCache(true).centerCrop().into(binding.ivFotoProducto)
        }

    // Guarda la URI pendiente de camara en el estado de la instancia para sobrevivir rotaciones de pantalla
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pendingCameraUri?.let { outState.putString(KEY_CAMERA_URI, it.toString()) }
    }

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCrearEditarProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el formulario, el desplegable de presentaciones, los observadores del ViewModel
    // y los listeners de los botones de imagen y guardado.
    // Si esta en modo edicion, solicita al ViewModel que cargue los datos del producto existente.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restaura la URI de camara pendiente si la actividad fue recreada (p.ej. por rotacion)
        pendingCameraUri = savedInstanceState?.getString(KEY_CAMERA_URI)?.let { Uri.parse(it) }

        // Configura el adaptador del campo de presentacion con la lista de opciones predefinidas
        val presentacionesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Constants.PRESENTACIONES
        )
        binding.acPresentacion.setAdapter(presentacionesAdapter)

        // En modo edicion, indica al ViewModel el ID del producto que se debe cargar
        if (isEditing) viewModel.selectProducto(args.productoId)

        // Observa el producto seleccionado y rellena los campos del formulario con sus datos
        viewModel.productoSeleccionado.observe(viewLifecycleOwner) { producto ->
            producto ?: return@observe
            binding.etNombre.setText(producto.nombre)
            binding.etDescripcion.setText(producto.descripcion)
            binding.etPrecio.setText(producto.precio.toInt().toString())
            binding.etStock.setText(producto.stock.toString())
            binding.acPresentacion.setText(producto.presentacion, false)
            binding.switchDisponible.isChecked = producto.disponible == 1
            // Carga la imagen actual del producto con Glide si existe una URL valida
            producto.imagenUrl?.takeIf { it.isNotEmpty() }?.let { url ->
                Glide.with(this).load(url).centerCrop().placeholder(R.drawable.bg_banner_placeholder).into(binding.ivFotoProducto)
            }
        }

        // Observa el resultado de la operacion de guardado (exito o error)
        // Consume el evento inmediatamente para evitar que sea re-entregado tras rotacion
        viewModel.operationResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe  // Ignorar el null inicial o el reset post-consumo
            viewModel.clearOperationResult()  // Consumir el evento para evitar re-entrega
            binding.btnGuardar.isEnabled = true
            result.fold(
                onSuccess = {
                    // Muestra mensaje de exito segun si se creo o edito el producto, y regresa al listado
                    val msg = if (isEditing) R.string.msg_product_updated else R.string.msg_product_created
                    binding.root.showSnackbar(getString(msg))
                    findNavController().popBackStack()
                },
                onFailure = { binding.root.showSnackbar(it.message ?: getString(R.string.error_field_required)) }
            )
        }

        // Registra los listeners de los botones de seleccion de imagen y guardado
        binding.btnCamara.setOnClickListener { solicitarCamara() }
        binding.btnGaleria.setOnClickListener { galleryLauncher.launch("image/*") }
        binding.btnGuardar.setOnClickListener { guardar() }
    }

    // Verifica si el permiso de camara esta concedido antes de abrirla.
    // Si no esta concedido, lanza el launcher de solicitud de permiso.
    private fun solicitarCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            abrirCamara()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Crea un URI temporal en el almacenamiento del dispositivo y lanza la camara apuntando a ese URI.
    // La URI queda almacenada en pendingCameraUri para recuperarla cuando la camara retorne el resultado.
    private fun abrirCamara() {
        pendingCameraUri = StorageHelper.createImageUri(requireContext())
        cameraLauncher.launch(pendingCameraUri!!)
    }

    // Lee los campos del formulario, valida que el nombre no este vacio y construye el objeto Producto.
    // Si el usuario selecciono una imagen nueva, la sube a Firebase Storage en un hilo de IO
    // antes de llamar a saveProducto en el ViewModel.
    // En modo edicion se preserva el vendedorId original para que el admin no reasigne el producto.
    private fun guardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val precio = binding.etPrecio.text.toString().toDoubleOrNull() ?: 0.0
        val stock = binding.etStock.text.toString().toIntOrNull() ?: 0
        val presentacion = binding.acPresentacion.text.toString().trim()
        val disponible = if (binding.switchDisponible.isChecked) 1 else 0

        // Limpia el error previo del campo nombre antes de validar
        binding.tilNombre.error = null
        if (nombre.isBlank()) {
            binding.tilNombre.error = getString(R.string.error_field_required)
            return
        }

        val session = SessionManager(requireContext())
        val productoActual = viewModel.productoSeleccionado.value

        // Construye el objeto base del producto con los datos del formulario
        val productoBase = Producto(
            id = if (isEditing) args.productoId else 0L,
            nombre = nombre,
            descripcion = descripcion,
            presentacion = presentacion,
            precio = precio,
            stock = stock,
            disponible = disponible,
            // Al editar se preserva el vendedorId original (el admin no debe reasignar el producto)
            vendedorId = if (isEditing) productoActual?.vendedorId ?: session.getUserId()
                         else session.getUserId(),
            imagenUrl = productoActual?.imagenUrl,
            firestoreDocId = productoActual?.firestoreDocId
        )

        // Deshabilita el boton para evitar multiples envios mientras se procesa la operacion
        binding.btnGuardar.isEnabled = false

        val uri = selectedImageUri
        if (uri != null) {
            // Si hay una imagen nueva seleccionada, la sube a Storage antes de guardar el producto
            lifecycleScope.launch {
                try {
                    val imageId = productoActual?.firestoreDocId ?: UUID.randomUUID().toString()
                    // IO: openInputStream y transferencia del stream no deben bloquear Main
                    val imageUrl = withContext(Dispatchers.IO) {
                        StorageHelper.uploadProductImage(imageId, uri, requireContext())
                    }
                    // Guarda el producto con la URL de la imagen recien subida
                    viewModel.saveProducto(productoBase.copy(imagenUrl = imageUrl))
                } catch (e: Exception) {
                    binding.btnGuardar.isEnabled = true
                    binding.root.showSnackbar(getString(R.string.msg_image_upload_error, e.message))
                }
            }
        } else {
            // No hay imagen nueva: guarda el producto conservando la URL anterior
            viewModel.saveProducto(productoBase)
        }
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Clave usada para persistir la URI de camara pendiente en el Bundle de estado
        private const val KEY_CAMERA_URI = "pending_camera_uri"
    }
}
