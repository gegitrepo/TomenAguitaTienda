package com.example.tomenaguita.ui.comprador.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Pedido
import com.example.tomenaguita.databinding.FragmentHistorialPedidosBinding
import com.example.tomenaguita.ui.adapter.PedidoAdapter
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.PedidoViewModel

class HistorialPedidosFragment : Fragment() {

    private var _binding: FragmentHistorialPedidosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PedidoAdapter
    private val viewModel: PedidoViewModel by activityViewModels()

    private var allPedidos: List<Pedido> = emptyList()

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

        val session = SessionManager(requireContext())
        viewModel.getPedidosByUsuario(session.getUserId())
            .observe(viewLifecycleOwner) { pedidos ->
                allPedidos = pedidos
                applyFilter()
            }

        binding.chipGroupFiltros.setOnCheckedStateChangeListener { _, _ -> applyFilter() }
    }

    private fun applyFilter() {
        val filtered = when {
            binding.chipPendiente.isChecked  -> allPedidos.filter { it.estado == "pendiente" }
            binding.chipPagado.isChecked     -> allPedidos.filter { it.estado == "pagado" }
            binding.chipEnviado.isChecked    -> allPedidos.filter { it.estado == "enviado" }
            binding.chipEntregado.isChecked  -> allPedidos.filter { it.estado == "entregado" }
            // "Todos" excluye los cancelados — los pedidos cancelados desaparecen del historial
            else -> allPedidos.filter { it.estado != "cancelado" }
        }
        adapter.submitList(filtered)
        if (filtered.isEmpty()) {
            binding.tvEmpty.visible()
            binding.rvPedidos.gone()
        } else {
            binding.tvEmpty.gone()
            binding.rvPedidos.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
