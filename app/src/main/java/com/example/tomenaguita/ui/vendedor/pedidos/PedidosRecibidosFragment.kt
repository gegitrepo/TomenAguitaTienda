package com.example.tomenaguita.ui.vendedor.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.databinding.FragmentPedidosRecibidosBinding
import com.example.tomenaguita.ui.adapter.PedidoAdapter
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.PedidoViewModel

class PedidosRecibidosFragment : Fragment() {

    private var _binding: FragmentPedidosRecibidosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PedidoAdapter
    private val viewModel: PedidoViewModel by activityViewModels()

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

        val vendedorId = SessionManager(requireContext()).getUserId()
        viewModel.getPedidosByVendedor(vendedorId).observe(viewLifecycleOwner) { pedidos ->
            adapter.submitList(pedidos)
            if (pedidos.isEmpty()) {
                binding.tvEmpty.visible()
                binding.rvPedidosRecibidos.gone()
            } else {
                binding.tvEmpty.gone()
                binding.rvPedidosRecibidos.visible()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
