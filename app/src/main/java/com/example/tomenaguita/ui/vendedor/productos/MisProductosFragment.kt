package com.example.tomenaguita.ui.vendedor.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.databinding.FragmentMisProductosBinding
import com.example.tomenaguita.ui.adapter.ProductoAdapter
import com.example.tomenaguita.ui.adapter.ProductoItem
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.ProductoViewModel

class MisProductosFragment : Fragment() {

    private var _binding: FragmentMisProductosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductoAdapter
    private val viewModel: ProductoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMisProductosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductoAdapter(
            onProductoClick = { item ->
                // Clic en la card → editar directamente
                findNavController().navigate(
                    MisProductosFragmentDirections.actionMisProductosToEditar(item.id)
                )
            },
            onAgregarClick = { item ->
                // Botón "+" → menú contextual con opciones Editar y Eliminar
                mostrarMenuProducto(item)
            }
        )
        binding.rvMisProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMisProductos.adapter = adapter

        val session = SessionManager(requireContext())
        viewModel.getProductosByVendedor(session.getUserId())
            .observe(viewLifecycleOwner) { productos ->
                val items = productos.map { ProductoItem.from(it) }
                adapter.submitList(items)
                if (items.isEmpty()) {
                    binding.tvEmpty.visible()
                    binding.rvMisProductos.gone()
                } else {
                    binding.tvEmpty.gone()
                    binding.rvMisProductos.visible()
                }
            }

        binding.fabCrearProducto.setOnClickListener {
            findNavController().navigate(MisProductosFragmentDirections.actionMisProductosToCrear())
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun mostrarMenuProducto(item: ProductoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle(item.nombre)
            .setItems(arrayOf("✏  Editar", "🗑  Eliminar")) { _, which ->
                when (which) {
                    0 -> findNavController().navigate(
                        MisProductosFragmentDirections.actionMisProductosToEditar(item.id)
                    )
                    1 -> confirmarEliminacion(item)
                }
            }
            .show()
    }

    private fun confirmarEliminacion(item: ProductoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar producto")
            .setMessage("¿Estás seguro de que deseas eliminar \"${item.nombre}\"? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> viewModel.deleteProducto(item.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
