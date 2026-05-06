package com.example.tomenaguita.ui.admin.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.FragmentGestionProductosBinding
import com.example.tomenaguita.ui.adapter.ProductoAdapter
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.showSnackbar

class GestionProductosFragment : Fragment() {

    private var _binding: FragmentGestionProductosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGestionProductosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductoAdapter(
            onProductoClick = { binding.root.showSnackbar("Editar: ${it.nombre}") },
            onAgregarClick = { binding.root.showSnackbar("Eliminar: ${it.nombre}") }
        )
        binding.rvTodosProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTodosProductos.adapter = adapter
        adapter.submitList(getProductosDemo())
    }

    private fun getProductosDemo() = Constants.PRODUCTOS_DEMO.mapIndexed { i, (nombre, presentacion, precio) ->
        Producto(id = (i + 1).toLong(), nombre = nombre, descripcion = "Agua purificada", presentacion = presentacion, precio = precio, stock = 100 + i * 10, vendedorId = 2L)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
