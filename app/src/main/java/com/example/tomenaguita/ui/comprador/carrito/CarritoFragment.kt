package com.example.tomenaguita.ui.comprador.carrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.entity.CarritoItem
import com.example.tomenaguita.databinding.FragmentCarritoBinding
import com.example.tomenaguita.ui.adapter.CarritoAdapter
import com.example.tomenaguita.ui.adapter.CarritoItemUI
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.utils.visible

class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CarritoAdapter

    private val demoItems = mutableListOf(
        CarritoItemUI(CarritoItem(1L, 3L, 1L, 2, 1500.0), "Botella personal", "300 ml"),
        CarritoItemUI(CarritoItem(2L, 3L, 3L, 1, 4000.0), "Botella familiar", "1 litro"),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        updateTotales()

        binding.btnProcederPago.setOnClickListener {
            findNavController().navigate(R.id.action_carrito_to_resumen)
        }
        binding.btnVaciarCarrito.setOnClickListener {
            demoItems.clear()
            adapter.submitList(emptyList())
            updateTotales()
            binding.root.showSnackbar(getString(R.string.msg_cart_cleared))
        }
    }

    private fun setupRecyclerView() {
        adapter = CarritoAdapter(
            onMinus = { item ->
                val idx = demoItems.indexOfFirst { it.item.id == item.id }
                if (idx >= 0 && demoItems[idx].item.cantidad > 1) {
                    demoItems[idx] = demoItems[idx].copy(item = demoItems[idx].item.copy(cantidad = demoItems[idx].item.cantidad - 1))
                    adapter.submitList(demoItems.toList())
                    updateTotales()
                }
            },
            onPlus = { item ->
                val idx = demoItems.indexOfFirst { it.item.id == item.id }
                if (idx >= 0) {
                    demoItems[idx] = demoItems[idx].copy(item = demoItems[idx].item.copy(cantidad = demoItems[idx].item.cantidad + 1))
                    adapter.submitList(demoItems.toList())
                    updateTotales()
                }
            },
            onEliminar = { item ->
                demoItems.removeAll { it.item.id == item.id }
                adapter.submitList(demoItems.toList())
                updateTotales()
            }
        )
        binding.rvCarrito.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCarrito.adapter = adapter
        adapter.submitList(demoItems.toList())
    }

    private fun updateTotales() {
        val subtotal = demoItems.sumOf { it.item.precioAlMomento * it.item.cantidad }
        binding.tvSubtotal.text = subtotal.toCOP()
        binding.tvTotal.text = subtotal.toCOP()
        if (demoItems.isEmpty()) {
            binding.tvEmptyCart.visible()
            binding.rvCarrito.gone()
        } else {
            binding.tvEmptyCart.gone()
            binding.rvCarrito.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
