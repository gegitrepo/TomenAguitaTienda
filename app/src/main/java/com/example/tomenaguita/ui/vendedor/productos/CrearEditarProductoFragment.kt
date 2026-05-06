package com.example.tomenaguita.ui.vendedor.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tomenaguita.databinding.FragmentCrearEditarProductoBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.showSnackbar

class CrearEditarProductoFragment : Fragment() {

    private var _binding: FragmentCrearEditarProductoBinding? = null
    private val binding get() = _binding!!
    private val args: CrearEditarProductoFragmentArgs by navArgs()
    private val isEditing get() = args.productoId != -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCrearEditarProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val presentacionesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, Constants.PRESENTACIONES)
        binding.acPresentacion.setAdapter(presentacionesAdapter)

        if (isEditing) {
            val idx = (args.productoId - 1).toInt().coerceIn(0, Constants.PRODUCTOS_DEMO.size - 1)
            val (nombre, presentacion, precio) = Constants.PRODUCTOS_DEMO[idx]
            binding.etNombre.setText(nombre)
            binding.etDescripcion.setText("Agua purificada de alta calidad")
            binding.etPrecio.setText(precio.toInt().toString())
            binding.etStock.setText("100")
            binding.acPresentacion.setText(presentacion, false)
        }

        binding.btnGuardar.setOnClickListener {
            if (binding.etNombre.text.isNullOrBlank()) {
                binding.tilNombre.error = "Obligatorio"
                return@setOnClickListener
            }
            binding.root.showSnackbar(if (isEditing) "Producto actualizado" else "Producto creado")
            findNavController().popBackStack()
        }

        binding.btnCamara.setOnClickListener { binding.root.showSnackbar("Abrir cámara") }
        binding.btnGaleria.setOnClickListener { binding.root.showSnackbar("Abrir galería") }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
