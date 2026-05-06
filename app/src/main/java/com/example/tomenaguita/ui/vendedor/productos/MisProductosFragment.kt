package com.example.tomenaguita.ui.vendedor.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.databinding.FragmentMisProductosBinding
import com.example.tomenaguita.ui.adapter.ProductoAdapter
import com.example.tomenaguita.ui.adapter.ProductoItem
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
            onProductoClick = { item ->
                val action = MisProductosFragmentDirections.actionMisProductosToEditar(item.id)
                findNavController().navigate(action)
            },
            onAgregarClick = {}
        )
        binding.rvMisProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMisProductos.adapter = adapter
        adapter.submitList(getDemoItems())

        binding.fabCrearProducto.setOnClickListener {
            findNavController().navigate(MisProductosFragmentDirections.actionMisProductosToCrear())
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun getDemoItems() = Constants.PRODUCTOS_DEMO.mapIndexed { i, (nombre, presentacion, precio) ->
        ProductoItem(
            id = (i + 1).toLong(),
            nombre = nombre,
            presentacion = presentacion,
            precio = precio,
            stock = 100,
            disponible = true
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
