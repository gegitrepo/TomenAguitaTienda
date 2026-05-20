package com.example.tomenaguita.ui.comprador.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.entity.CarritoItem
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.FragmentHomeBinding
import com.example.tomenaguita.ui.adapter.ProductoAdapter
import com.example.tomenaguita.ui.adapter.ProductoItem
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.CarritoViewModel
import com.example.tomenaguita.viewmodel.ProductoViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductoAdapter
    private val productoViewModel: ProductoViewModel by activityViewModels()
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    private var allProductos: List<Producto> = emptyList()
    private var currentFeatured: List<Producto> = emptyList()
    private var destacadosSeleccionados = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        binding.tvFeatured.text = getString(R.string.label_featured_products)

        adapter = ProductoAdapter(
            onProductoClick = { item ->
                findNavController().navigate(HomeFragmentDirections.actionHomeToDetalle(item.id))
            },
            onAgregarClick = { item ->
                val nuevoItem = CarritoItem(
                    usuarioId = session.getUserId(),
                    productoId = item.id,
                    cantidad = 1,
                    precioAlMomento = item.precio
                )
                carritoViewModel.agregarItem(nuevoItem)
                binding.root.showSnackbar(getString(R.string.msg_item_added_to_cart, item.nombre))
            }
        )
        binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductos.adapter = adapter

        productoViewModel.productosDisponibles.observe(viewLifecycleOwner) { productos ->
            allProductos = productos
            if (!destacadosSeleccionados && productos.isNotEmpty()) {
                destacadosSeleccionados = true
                currentFeatured = productos.shuffled().take(4)
            }
            applySearch(binding.etSearch.text?.toString() ?: "")
        }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            applySearch(text?.toString() ?: "")
        }
    }

    private fun applySearch(query: String) {
        val lista = if (query.isBlank()) {
            currentFeatured
        } else {
            allProductos.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                it.presentacion.contains(query, ignoreCase = true)
            }
        }
        val items = lista.map { ProductoItem.from(it) }
        adapter.submitList(items)
        if (items.isEmpty()) {
            binding.tvEmpty.visible()
            binding.rvProductos.gone()
        } else {
            binding.tvEmpty.gone()
            binding.rvProductos.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
