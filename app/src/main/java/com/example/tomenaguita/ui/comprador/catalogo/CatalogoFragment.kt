package com.example.tomenaguita.ui.comprador.catalogo

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
 * Pantalla de catálogo completo de productos disponibles.
 * A diferencia de HomeFragment, muestra todos los productos sin filtro de destacados.
 * Permite buscar en tiempo real por nombre o presentación del producto.
 * El comprador puede agregar productos al carrito directamente desde esta vista.
 * Reutiliza el layout FragmentHomeBinding del fragmento de inicio.
 */
class CatalogoFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Adaptador que muestra la lista completa de productos en el RecyclerView
    private lateinit var adapter: ProductoAdapter

    // ViewModel que provee la lista de productos disponibles desde Firestore/Room
    private val productoViewModel: ProductoViewModel by activityViewModels()

    // ViewModel que gestiona las operaciones del carrito de compras
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    // Lista completa de productos disponibles, usada para aplicar el filtro de búsqueda
    private var allProductos: List<Producto> = emptyList()

    // Infla el layout reutilizado del fragmento de inicio
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Configura el adaptador, el RecyclerView, los observadores del ViewModel
     * y el listener de búsqueda en tiempo real.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())

        // Personalizar textos del layout reutilizado para el contexto del catálogo
        binding.tvFeatured.text = getString(R.string.title_catalog)
        binding.tvEmpty.text = getString(R.string.empty_catalog_products)

        // Configurar el adaptador con los callbacks de clic en producto y en "Agregar al carrito"
        adapter = ProductoAdapter(
            onProductoClick = { item ->
                // Navegar al detalle del producto seleccionado
                findNavController().navigate(CatalogoFragmentDirections.actionCatalogoToDetalle(item.id))
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

        // Observar todos los productos disponibles y aplicar el filtro actual
        productoViewModel.productosDisponibles.observe(viewLifecycleOwner) { productos ->
            allProductos = productos
            applySearch(binding.etSearch.text?.toString() ?: "")
        }

        // Actualizar la lista cada vez que el usuario escribe en el buscador
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            applySearch(text?.toString() ?: "")
        }
    }

    /*
     * Filtra la lista de productos según el texto de búsqueda introducido.
     * Si la búsqueda está vacía, muestra todos los productos del catálogo.
     * Si hay texto, filtra por coincidencia en nombre o presentación (sin distinción de mayúsculas).
     * Muestra u oculta el mensaje de lista vacía según el resultado.
     * Consume: query (String) con el texto de búsqueda actual.
     */
    private fun applySearch(query: String) {
        val lista = if (query.isBlank()) {
            allProductos
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
