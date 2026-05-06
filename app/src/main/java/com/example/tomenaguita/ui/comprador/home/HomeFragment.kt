package com.example.tomenaguita.ui.comprador.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.FragmentHomeBinding
import com.example.tomenaguita.ui.adapter.ProductoAdapter
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.showSnackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadDemoProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductoAdapter(
            onProductoClick = { producto ->
                val action = HomeFragmentDirections.actionHomeToDetalle(producto.id)
                findNavController().navigate(action)
            },
            onAgregarClick = { producto ->
                binding.root.showSnackbar("${producto.nombre} agregado al carrito")
            }
        )
        binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductos.adapter = adapter
    }

    private fun loadDemoProducts() {
        val productos = Constants.PRODUCTOS_DEMO.mapIndexed { index, (nombre, presentacion, precio) ->
            Producto(
                id = (index + 1).toLong(),
                nombre = nombre,
                descripcion = "Agua purificada $nombre. Ideal para hidratación diaria.",
                presentacion = presentacion,
                precio = precio,
                stock = 100 + index * 10,
                vendedorId = 2L
            )
        }
        adapter.submitList(productos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
