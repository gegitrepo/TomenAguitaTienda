package com.example.tomenaguita.ui.comprador.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentHomeBinding
import com.example.tomenaguita.ui.adapter.ProductoAdapter
import com.example.tomenaguita.ui.adapter.ProductoItem
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
            onProductoClick = { item ->
                val action = HomeFragmentDirections.actionHomeToDetalle(item.id)
                findNavController().navigate(action)
            },
            onAgregarClick = { item ->
                binding.root.showSnackbar(getString(R.string.msg_item_added_to_cart, item.nombre))
            }
        )
        binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductos.adapter = adapter
    }

    private fun loadDemoProducts() {
        val items = Constants.PRODUCTOS_DEMO.mapIndexed { index, (nombre, presentacion, precio) ->
            ProductoItem(
                id = (index + 1).toLong(),
                nombre = nombre,
                presentacion = presentacion,
                precio = precio,
                stock = 100 + index * 10,
                disponible = true
            )
        }
        adapter.submitList(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
