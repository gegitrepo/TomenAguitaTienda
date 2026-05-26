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

/*
 * Pantalla de inicio del comprador.
 * Muestra una selección aleatoria de hasta 4 productos destacados cuando no hay búsqueda activa.
 * Permite buscar productos por nombre o presentación en tiempo real.
 * El comprador puede agregar cualquier producto directamente al carrito desde esta pantalla.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Adaptador que muestra la lista de productos en el RecyclerView
    private lateinit var adapter: ProductoAdapter

    // ViewModel que provee la lista de productos disponibles desde Firestore/Room
    private val productoViewModel: ProductoViewModel by activityViewModels()

    // ViewModel que gestiona las operaciones del carrito de compras
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    // Lista completa de productos disponibles, usada para filtrar en búsquedas
    private var allProductos: List<Producto> = emptyList()

    // Subconjunto aleatorio de productos que se muestra en la sección de destacados
    private var currentFeatured: List<Producto> = emptyList()

    // Bandera que evita regenerar los destacados cada vez que el LiveData emite
    private var destacadosSeleccionados = false

    // Infla el layout del fragmento usando ViewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Configura el adaptador, el RecyclerView, los observadores del ViewModel
     * y el listener del campo de búsqueda en tiempo real.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        binding.tvFeatured.text = getString(R.string.label_featured_products)

        // Configurar el adaptador con los callbacks de clic en producto y en "Agregar al carrito"
        adapter = ProductoAdapter(
            onProductoClick = { item ->
                // Navegar al detalle del producto seleccionado
                findNavController().navigate(HomeFragmentDirections.actionHomeToDetalle(item.id))
            },
            onAgregarClick = { item ->
                // Crear un ítem de carrito con cantidad 1 al precio actual del producto
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

        // Observar la lista de productos disponibles y seleccionar destacados la primera vez
        productoViewModel.productosDisponibles.observe(viewLifecycleOwner) { productos ->
            allProductos = productos
            if (!destacadosSeleccionados && productos.isNotEmpty()) {
                destacadosSeleccionados = true
                // Seleccionar 4 productos aleatorios como destacados (solo una vez por sesión)
                currentFeatured = productos.shuffled().take(4)
            }
            applySearch(binding.etSearch.text?.toString() ?: "")
        }

        // Actualizar la lista cada vez que el usuario escribe en el buscador
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            applySearch(text?.toString() ?: "")
        }
    }

    /*
     * Filtra la lista de productos según el texto de búsqueda introducido.
     * Si la búsqueda está vacía, muestra los productos destacados.
     * Si hay texto, busca por nombre o presentación en todos los productos disponibles.
     * Muestra u oculta el mensaje de lista vacía según el resultado del filtro.
     * Consume: query (String) con el texto de búsqueda actual.
     */
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

    // Libera el binding al destruir la vista para evitar fugas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
