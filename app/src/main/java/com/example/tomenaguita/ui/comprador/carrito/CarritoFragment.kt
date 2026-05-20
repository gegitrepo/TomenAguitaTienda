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

class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CarritoAdapter

    private val carritoViewModel: CarritoViewModel by activityViewModels()
    private val productoViewModel: ProductoViewModel by activityViewModels()

    private var productoMap: Map<Long, Producto> = emptyMap()
    // Almacén local para que rebuildUI() no necesite crear un nuevo LiveData
    private var carritoItems: List<CarritoItem> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

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

        carritoViewModel.total.observe(viewLifecycleOwner) { total ->
            binding.tvSubtotal.text = total.toCOP()
            binding.tvTotal.text = total.toCOP()
        }

        binding.btnProcederPago.setOnClickListener {
            findNavController().navigate(R.id.action_carrito_to_resumen)
        }

        binding.btnVaciarCarrito.setOnClickListener {
            carritoViewModel.vaciarCarrito(userId)
            binding.root.showSnackbar(getString(R.string.msg_cart_cleared))
        }
    }

    private fun setupAdapter() {
        adapter = CarritoAdapter(
            onMinus = { item -> carritoViewModel.actualizarCantidad(item, item.cantidad - 1) },
            onPlus = { item -> carritoViewModel.actualizarCantidad(item, item.cantidad + 1) },
            onEliminar = { item -> carritoViewModel.eliminarItem(item.id) }
        )
        binding.rvCarrito.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCarrito.adapter = adapter
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
