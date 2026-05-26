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
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.ProductoViewModel

// Fragmento del modulo vendedor que muestra el catalogo completo de productos disponibles.
// Permite al vendedor crear, editar y eliminar productos.
// Nota: muestra TODOS los productos del catalogo global (productosDisponibles), no solo los del
// vendedor en sesion, porque los productos demo tienen vendedorId="demo" en Firestore y se
// comparten entre vendedores.
class MisProductosFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_mis_productos.xml
    private var _binding: FragmentMisProductosBinding? = null
    private val binding get() = _binding!!

    // Adaptador del RecyclerView de productos
    private lateinit var adapter: ProductoAdapter

    // ViewModel compartido a nivel de actividad para acceder al catalogo de productos
    private val viewModel: ProductoViewModel by activityViewModels()

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMisProductosBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el adaptador, el RecyclerView, los observadores del ViewModel y los listeners de UI.
    // Se llama despues de que la vista ha sido creada.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa el adaptador con callbacks para clic en la card y clic en el boton "+"
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

        // Observa el catalogo global de productos y actualiza la lista; muestra mensaje vacio si no hay productos.
        // Muestra todo el catálogo disponible — el vendedor puede editar precio y stock de cualquier producto
        viewModel.productosDisponibles.observe(viewLifecycleOwner) { productos ->
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

        // Navega al formulario de creacion de producto al pulsar el FAB
        binding.fabCrearProducto.setOnClickListener {
            findNavController().navigate(MisProductosFragmentDirections.actionMisProductosToCrear())
        }
        // El swipe-to-refresh solo detiene la animacion; la carga es reactiva via LiveData
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    // Muestra un dialogo contextual con las opciones Editar y Eliminar para el producto seleccionado.
    // Recibe el ProductoItem sobre el que se actuo.
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

    // Muestra un dialogo de confirmacion antes de eliminar el producto.
    // Si el usuario confirma, delega la eliminacion al ViewModel.
    // Recibe el ProductoItem que se desea eliminar.
    private fun confirmarEliminacion(item: ProductoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar producto")
            .setMessage("¿Estás seguro de que deseas eliminar \"${item.nombre}\"? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> viewModel.deleteProducto(item.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
