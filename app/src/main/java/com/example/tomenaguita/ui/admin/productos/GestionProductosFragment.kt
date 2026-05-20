package com.example.tomenaguita.ui.admin.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.FragmentGestionProductosBinding
import com.example.tomenaguita.ui.adapter.ProductoAdapter
import com.example.tomenaguita.ui.adapter.ProductoItem
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.ProductoViewModel

class GestionProductosFragment : Fragment() {

    private var _binding: FragmentGestionProductosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductoAdapter
    private val viewModel: ProductoViewModel by activityViewModels()

    private var allProductos: List<Producto> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGestionProductosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductoAdapter(
            onProductoClick = { item -> mostrarMenuProducto(item) },
            onAgregarClick = { item -> mostrarMenuProducto(item) }
        )
        binding.rvTodosProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTodosProductos.adapter = adapter

        viewModel.productosDisponibles.observe(viewLifecycleOwner) { productos ->
            allProductos = productos
            applySearch(binding.etSearch.text?.toString() ?: "")
        }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            applySearch(text?.toString() ?: "")
        }
    }

    private fun mostrarMenuProducto(item: ProductoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle(item.nombre)
            .setMessage("${item.presentacion}  ·  ${item.precio.toLong()} COP")
            .setItems(arrayOf("✏  Editar", "🗑  Eliminar")) { _, which ->
                when (which) {
                    0 -> findNavController().navigate(
                        GestionProductosFragmentDirections
                            .actionGestionToEditarProducto(item.id)
                    )
                    1 -> confirmarEliminacion(item)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminacion(item: ProductoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar producto")
            .setMessage("¿Eliminar \"${item.nombre}\"? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> viewModel.deleteProducto(item.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun applySearch(query: String) {
        val lista = if (query.isBlank()) allProductos
        else allProductos.filter {
            it.nombre.contains(query, ignoreCase = true) ||
            it.presentacion.contains(query, ignoreCase = true)
        }
        val items = lista.map { ProductoItem.from(it) }
        adapter.submitList(items)
        if (items.isEmpty()) {
            binding.tvEmpty.visible()
            binding.rvTodosProductos.gone()
        } else {
            binding.tvEmpty.gone()
            binding.rvTodosProductos.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
