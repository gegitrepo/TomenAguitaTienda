package com.example.tomenaguita.ui.admin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentDashboardBinding
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.viewmodel.PedidoViewModel
import com.example.tomenaguita.viewmodel.ProductoViewModel
import com.example.tomenaguita.viewmodel.UsuarioViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private val productoViewModel: ProductoViewModel by activityViewModels()
    private val pedidoViewModel: PedidoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usuarioViewModel.todosLosUsuarios.observe(viewLifecycleOwner) { usuarios ->
            binding.tvTotalUsuarios.text = usuarios.size.toString()
        }

        productoViewModel.productosDisponibles.observe(viewLifecycleOwner) { productos ->
            binding.tvTotalProductos.text = productos.size.toString()
        }

        pedidoViewModel.getAllPedidos().observe(viewLifecycleOwner) { pedidos ->
            binding.tvTotalPedidos.text = pedidos.size.toString()
            val totalVentas = pedidos.sumOf { it.totalPedido }
            binding.tvTotalVentas.text = totalVentas.toCOP()
        }

        binding.btnGestionUsuarios.setOnClickListener {
            findNavController().navigate(R.id.listaUsuariosFragment)
        }
        binding.btnGestionProductos.setOnClickListener {
            findNavController().navigate(R.id.gestionProductosFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
