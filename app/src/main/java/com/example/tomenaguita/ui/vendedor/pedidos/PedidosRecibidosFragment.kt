package com.example.tomenaguita.ui.vendedor.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Pedido
import com.example.tomenaguita.databinding.FragmentPedidosRecibidosBinding
import com.example.tomenaguita.ui.adapter.PedidoAdapter

class PedidosRecibidosFragment : Fragment() {

    private var _binding: FragmentPedidosRecibidosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PedidoAdapter

    private val demoPedidos = listOf(
        Pedido(1L, "TA-20240501-0001", 3L, 7000.0, 0.0, 7000.0, "Calle 123 # 45-67, Bogotá", estado = "pagado", metodoPago = "Efectivo"),
        Pedido(2L, "TA-20240430-0012", 3L, 14500.0, 0.0, 14500.0, "Cra 7 # 89-12, Bogotá", estado = "pendiente", metodoPago = "Tarjeta"),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPedidosRecibidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PedidoAdapter { pedido ->
            val action = PedidosRecibidosFragmentDirections.actionPedidosToDetalle(pedido.id)
            findNavController().navigate(action)
        }
        binding.rvPedidosRecibidos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPedidosRecibidos.adapter = adapter
        adapter.submitList(demoPedidos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
