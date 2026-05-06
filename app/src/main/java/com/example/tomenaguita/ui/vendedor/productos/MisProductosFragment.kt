package com.example.tomenaguita.ui.vendedor.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.FragmentMisProductosBinding
import com.example.tomenaguita.ui.adapter.ProductoAdapter
import com.example.tomenaguita.utils.Constants

class MisProductosFragment : Fragment() {

    private var _binding: FragmentMisProductosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMisProductosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductoAdapter(
            onProductoClick = { producto ->
                val action = MisProductosFragmentDirections.actionMisProductosToEditar(producto.id)
                findNavController().navigate(action)
            },
            onAgregarClick = {}
        )
        binding.rvMisProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMisProductos.adapter = adapter
        adapter.submitList(getDemoProductos())

        binding.fabCrearProducto.setOnClickListener {
            findNavController().navigate(MisProductosFragmentDirections.actionMisProductosToCrear())
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun getDemoProductos() = Constants.PRODUCTOS_DEMO.mapIndexed { i, (nombre, presentacion, precio) ->
        Producto(id = (i + 1).toLong(), nombre = nombre, descripcion = "Agua purificada", presentacion = presentacion, precio = precio, stock = 100, vendedorId = 2L)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
