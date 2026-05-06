package com.example.tomenaguita.ui.comprador.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Pedido
import com.example.tomenaguita.databinding.FragmentHistorialPedidosBinding
import com.example.tomenaguita.ui.adapter.PedidoAdapter

class HistorialPedidosFragment : Fragment() {

    private var _binding: FragmentHistorialPedidosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PedidoAdapter

    private val demoPedidos = listOf(
        Pedido(1L, "TA-20240501-0001", 3L, 7000.0, 0.0, 7000.0, "Calle 123 # 45-67, Bogotá", estado = "pendiente", metodoPago = "Efectivo"),
        Pedido(2L, "TA-20240430-0012", 3L, 14500.0, 0.0, 14500.0, "Calle 123 # 45-67, Bogotá", estado = "pagado", metodoPago = "Tarjeta"),
        Pedido(3L, "TA-20240428-0008", 3L, 30000.0, 0.0, 30000.0, "Calle 123 # 45-67, Bogotá", estado = "entregado", metodoPago = "Efectivo"),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistorialPedidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PedidoAdapter { pedido ->
            val action = HistorialPedidosFragmentDirections.actionHistorialToDetalle(pedido.id)
            findNavController().navigate(action)
        }
        binding.rvPedidos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPedidos.adapter = adapter
        adapter.submitList(demoPedidos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
