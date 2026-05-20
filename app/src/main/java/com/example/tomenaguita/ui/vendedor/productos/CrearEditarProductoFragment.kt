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

class CrearEditarProductoFragment : Fragment() {

    private var _binding: FragmentCrearEditarProductoBinding? = null
    private val binding get() = _binding!!
    private val args: CrearEditarProductoFragmentArgs by navArgs()
    private val isEditing get() = args.productoId != -1L
    private val viewModel: com.example.tomenaguita.viewmodel.ProductoViewModel by activityViewModels()

    private var selectedImageUri: Uri? = null
    private var pendingCameraUri: Uri? = null

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) abrirCamara()
            else binding.root.showSnackbar("Permiso de cámara denegado")
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            selectedImageUri = uri
            Glide.with(this).load(uri).centerCrop().into(binding.ivFotoProducto)
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val uri = pendingCameraUri ?: return@registerForActivityResult
            if (!success) return@registerForActivityResult
            selectedImageUri = uri
            Glide.with(this).load(uri).skipMemoryCache(true).centerCrop().into(binding.ivFotoProducto)
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pendingCameraUri?.let { outState.putString(KEY_CAMERA_URI, it.toString()) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCrearEditarProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pendingCameraUri = savedInstanceState?.getString(KEY_CAMERA_URI)?.let { Uri.parse(it) }

        val presentacionesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Constants.PRESENTACIONES
        )
        binding.acPresentacion.setAdapter(presentacionesAdapter)

        if (isEditing) viewModel.selectProducto(args.productoId)

        viewModel.productoSeleccionado.observe(viewLifecycleOwner) { producto ->
            producto ?: return@observe
            binding.etNombre.setText(producto.nombre)
            binding.etDescripcion.setText(producto.descripcion)
            binding.etPrecio.setText(producto.precio.toInt().toString())
            binding.etStock.setText(producto.stock.toString())
            binding.acPresentacion.setText(producto.presentacion, false)
            binding.switchDisponible.isChecked = producto.disponible == 1
            producto.imagenUrl?.takeIf { it.isNotEmpty() }?.let { url ->
                Glide.with(this).load(url).centerCrop().placeholder(R.drawable.bg_banner_placeholder).into(binding.ivFotoProducto)
            }
        }

        viewModel.operationResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe  // Ignorar el null inicial o el reset post-consumo
            viewModel.clearOperationResult()  // Consumir el evento para evitar re-entrega
            binding.btnGuardar.isEnabled = true
            result.fold(
                onSuccess = {
                    val msg = if (isEditing) R.string.msg_product_updated else R.string.msg_product_created
                    binding.root.showSnackbar(getString(msg))
                    findNavController().popBackStack()
                },
                onFailure = { binding.root.showSnackbar(it.message ?: getString(R.string.error_field_required)) }
            )
        }

        binding.btnCamara.setOnClickListener { solicitarCamara() }
        binding.btnGaleria.setOnClickListener { galleryLauncher.launch("image/*") }
        binding.btnGuardar.setOnClickListener { guardar() }
    }

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

    private fun guardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val precio = binding.etPrecio.text.toString().toDoubleOrNull() ?: 0.0
        val stock = binding.etStock.text.toString().toIntOrNull() ?: 0
        val presentacion = binding.acPresentacion.text.toString().trim()
        val disponible = if (binding.switchDisponible.isChecked) 1 else 0

        binding.tilNombre.error = null
        if (nombre.isBlank()) {
            binding.tilNombre.error = getString(R.string.error_field_required)
            return
        }

        val session = SessionManager(requireContext())
        val productoActual = viewModel.productoSeleccionado.value
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

        binding.btnGuardar.isEnabled = false

        val uri = selectedImageUri
        if (uri != null) {
            lifecycleScope.launch {
                try {
                    val imageId = productoActual?.firestoreDocId ?: UUID.randomUUID().toString()
                    // IO: openInputStream y transferencia del stream no deben bloquear Main
                    val imageUrl = withContext(Dispatchers.IO) {
                        StorageHelper.uploadProductImage(imageId, uri, requireContext())
                    }
                    viewModel.saveProducto(productoBase.copy(imagenUrl = imageUrl))
                } catch (e: Exception) {
                    binding.btnGuardar.isEnabled = true
                    binding.root.showSnackbar("Error al subir imagen: ${e.message}")
                }
            }
        } else {
            viewModel.saveProducto(productoBase)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_CAMERA_URI = "pending_camera_uri"
    }
}
