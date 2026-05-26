package com.example.tomenaguita.ui.comprador.carrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.entity.CarritoItem
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.FragmentCarritoBinding
import com.example.tomenaguita.ui.adapter.CarritoAdapter
import com.example.tomenaguita.ui.adapter.CarritoItemUI
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.CarritoViewModel
import com.example.tomenaguita.viewmodel.ProductoViewModel

/*
 * Pantalla del carrito de compras del comprador.
 * Muestra la lista de productos agregados con su cantidad, precio unitario y subtotal.
 * Permite aumentar o disminuir la cantidad de cada ítem, eliminar ítems individuales
 * y vaciar todo el carrito. Calcula y muestra el total en tiempo real.
 * El botón "Proceder al pago" navega hacia el resumen del pedido.
 */
class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!

    // Adaptador que muestra los ítems del carrito con controles de cantidad
    private lateinit var adapter: CarritoAdapter

    // ViewModel que gestiona las operaciones CRUD del carrito (Room + Firestore)
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    // ViewModel que provee la lista de productos para resolver nombre, presentación e imagen
    private val productoViewModel: ProductoViewModel by activityViewModels()

    // Mapa de productos indexado por ID para buscar datos al construir la UI
    private var productoMap: Map<Long, Producto> = emptyMap()

    // Almacén local de ítems del carrito para que rebuildUI() pueda combinarlos con productoMap
    private var carritoItems: List<CarritoItem> = emptyList()

    // Infla el layout del fragmento usando ViewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Configura el adaptador, observa los ítems del carrito y los productos disponibles,
     * y asigna los listeners de los botones de pago y vaciado del carrito.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        val userId = session.getUserId()

        setupAdapter()

        // Mapa de productos para resolver nombre y presentación en cada ítem del carrito
        productoViewModel.productosDisponibles.observe(viewLifecycleOwner) { productos ->
            productoMap = productos.associateBy { it.id }
            rebuildUI()
        }

        // Ítems del carrito — se almacenan localmente para que rebuildUI() los use
        carritoViewModel.getCarrito(userId).observe(viewLifecycleOwner) { items ->
            carritoItems = items
            rebuildUI()
        }

        // Actualizar los textos de subtotal y total cuando el ViewModel recalcula
        carritoViewModel.total.observe(viewLifecycleOwner) { total ->
            binding.tvSubtotal.text = total.toCOP()
            binding.tvTotal.text = total.toCOP()
        }

        // Navegar al resumen del pedido para confirmar la dirección antes de pagar
        binding.btnProcederPago.setOnClickListener {
            findNavController().navigate(R.id.action_carrito_to_resumen)
        }

        binding.btnVaciarCarrito.setOnClickListener {
            carritoViewModel.vaciarCarrito(userId)
            binding.root.showSnackbar(getString(R.string.msg_cart_cleared))
        }
    }

    /*
     * Crea y registra el CarritoAdapter con los callbacks para modificar cantidades
     * y eliminar ítems individuales. Asigna el layoutManager y el adaptador al RecyclerView.
     */
    private fun setupAdapter() {
        adapter = CarritoAdapter(
            // Disminuir cantidad en 1; el ViewModel elimina el ítem si la cantidad llega a 0
            onMinus = { item -> carritoViewModel.actualizarCantidad(item, item.cantidad - 1) },
            // Aumentar cantidad en 1
            onPlus = { item -> carritoViewModel.actualizarCantidad(item, item.cantidad + 1) },
            // Eliminar el ítem del carrito por su ID
            onEliminar = { item -> carritoViewModel.eliminarItem(item.id) }
        )
        binding.rvCarrito.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCarrito.adapter = adapter
    }

    /*
     * Construye la lista de ítems de UI combinando carritoItems con productoMap.
     * Los ítems cuyo producto no se encuentre en el mapa se descartan silenciosamente.
     * Actualiza el adaptador, recalcula el total y muestra/oculta el mensaje de carrito vacío.
     * Habilita o deshabilita el botón de pago según si hay ítems en la lista.
     */
    private fun rebuildUI() {
        val uiItems = carritoItems.mapNotNull { item ->
            val producto = productoMap[item.productoId] ?: return@mapNotNull null
            CarritoItemUI(item, producto.nombre, producto.presentacion, producto.imagenUrl)
        }
        adapter.submitList(uiItems)
        carritoViewModel.calcularTotal(carritoItems)

        if (uiItems.isEmpty()) {
            binding.tvEmptyCart.visible()
            binding.rvCarrito.gone()
            binding.btnProcederPago.isEnabled = false
        } else {
            binding.tvEmptyCart.gone()
            binding.rvCarrito.visible()
            binding.btnProcederPago.isEnabled = true
        }
    }

    // Libera el binding al destruir la vista para evitar fugas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
