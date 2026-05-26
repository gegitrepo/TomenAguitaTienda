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
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentGestionProductosBinding
import com.example.tomenaguita.ui.adapter.ProductoAdapter
import com.example.tomenaguita.ui.adapter.ProductoItem
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.ProductoViewModel

// Fragmento del modulo administrador que muestra el catalogo completo de productos de la plataforma.
// El administrador puede buscar productos por nombre o presentacion, editarlos o eliminarlos.
// El boton "+" (agregar) esta oculto (showAgregarButton = false) porque la creacion y gestion
// de productos es responsabilidad de los vendedores; el admin solo supervisa y modera el catalogo.
// Al pulsar un producto se abre un menu contextual con las opciones de editar o eliminar.
class GestionProductosFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_gestion_productos.xml
    private var _binding: FragmentGestionProductosBinding? = null
    private val binding get() = _binding!!

    // Adaptador del RecyclerView que muestra las tarjetas de producto
    private lateinit var adapter: ProductoAdapter

    // ViewModel de productos compartido a nivel de actividad; provee la lista y operaciones CRUD
    private val viewModel: ProductoViewModel by activityViewModels()

    // Copia local de todos los productos; sirve como fuente para el filtro de busqueda
    private var allProductos: List<Producto> = emptyList()

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGestionProductosBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el RecyclerView con el adaptador (sin boton de agregar) y el campo de busqueda.
    // Observa los productos disponibles y aplica el filtro actual al recibir cambios.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // showAgregarButton = false oculta el boton "+" del adaptador porque los productos
        // los crean los vendedores; el admin solo edita y elimina desde esta vista
        adapter = ProductoAdapter(
            onProductoClick = { item -> mostrarMenuProducto(item) },
            onAgregarClick = { item -> mostrarMenuProducto(item) },
            showAgregarButton = false
        )
        binding.rvTodosProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTodosProductos.adapter = adapter

        // Observa los productos disponibles y re-aplica la busqueda cada vez que la lista cambia
        viewModel.productosDisponibles.observe(viewLifecycleOwner) { productos ->
            allProductos = productos
            applySearch(binding.etSearch.text?.toString() ?: "")
        }

        // Filtra la lista en tiempo real a medida que el usuario escribe en el campo de busqueda
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            applySearch(text?.toString() ?: "")
        }
    }

    // Muestra un AlertDialog con las opciones de editar o eliminar el producto seleccionado.
    // Navega a CrearEditarProductoFragment al elegir "Editar" o llama a confirmarEliminacion al elegir "Eliminar".
    // Consume: item (producto seleccionado representado como ProductoItem)
    private fun mostrarMenuProducto(item: ProductoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle(item.nombre)
            .setMessage("${item.presentacion}  ·  ${item.precio.toLong()} COP")
            .setItems(arrayOf(getString(R.string.menu_edit_product), getString(R.string.menu_delete_product))) { _, which ->
                when (which) {
                    // Opcion 0: navega al formulario de edicion pasando el ID del producto
                    0 -> findNavController().navigate(
                        GestionProductosFragmentDirections
                            .actionGestionToEditarProducto(item.id)
                    )
                    // Opcion 1: muestra dialogo de confirmacion antes de eliminar
                    1 -> confirmarEliminacion(item)
                }
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    // Muestra un AlertDialog de confirmacion antes de eliminar definitivamente el producto.
    // Al confirmar, llama al ViewModel para eliminar el producto de Room y Firestore.
    // Consume: item (producto que se desea eliminar)
    private fun confirmarEliminacion(item: ProductoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_delete_product)
            .setMessage(getString(R.string.msg_delete_product_confirm, item.nombre))
            .setPositiveButton(R.string.btn_delete) { _, _ -> viewModel.deleteProducto(item.id) }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    // Filtra la lista de productos por nombre o presentacion segun el texto de busqueda.
    // Si la consulta esta vacia muestra todos los productos.
    // Muestra u oculta el texto de lista vacia segun el resultado del filtro.
    // Consume: query (texto de busqueda ingresado por el usuario)
    private fun applySearch(query: String) {
        val lista = if (query.isBlank()) allProductos
        else allProductos.filter {
            it.nombre.contains(query, ignoreCase = true) ||
            it.presentacion.contains(query, ignoreCase = true)
        }
        val items = lista.map { ProductoItem.from(it) }
        adapter.submitList(items)
        // Muestra un mensaje de lista vacia si no hay resultados para la busqueda actual
        if (items.isEmpty()) {
            binding.tvEmpty.visible()
            binding.rvTodosProductos.gone()
        } else {
            binding.tvEmpty.gone()
            binding.rvTodosProductos.visible()
        }
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
