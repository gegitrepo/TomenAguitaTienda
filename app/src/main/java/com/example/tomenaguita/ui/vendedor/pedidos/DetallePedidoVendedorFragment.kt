package com.example.tomenaguita.ui.vendedor.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentDetallePedidoVendedorBinding
import com.example.tomenaguita.ui.adapter.DetallePedidoAdapter
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.viewmodel.PedidoViewModel
import com.example.tomenaguita.viewmodel.UsuarioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetallePedidoVendedorFragment : Fragment() {

    private var _binding: FragmentDetallePedidoVendedorBinding? = null
    private val binding get() = _binding!!
    private val args: DetallePedidoVendedorFragmentArgs by navArgs()
    private val pedidoViewModel: PedidoViewModel by activityViewModels()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var adapter: DetallePedidoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetallePedidoVendedorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DetallePedidoAdapter()
        binding.rvProductosPedido.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductosPedido.adapter = adapter

        pedidoViewModel.selectPedido(args.pedidoId)
        pedidoViewModel.cargarDetalles(args.pedidoId)

        pedidoViewModel.pedidoActual.observe(viewLifecycleOwner) { pedido ->
            pedido ?: return@observe

            // Header: número, fecha, estado
            binding.tvNumeroPedidoVendedor.text = pedido.orderNumber
            val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault())
            binding.tvFechaPedidoVendedor.text = sdf.format(Date(pedido.createdAt))
            binding.chipEstadoVendedor.text = pedido.estado.replaceFirstChar { it.uppercase() }

            // Info del comprador (dirección viene del pedido directamente)
            binding.tvDireccionComprador.text = pedido.direccionEntrega
            binding.tvTotal.text = pedido.totalPedido.toCOP()

            // Buscar datos del comprador en Room (sincronizados desde Firestore)
            usuarioViewModel.cargarUsuario(pedido.usuarioId)

            // Estado de los botones
            val esPagado = pedido.estado == "pagado"
            val esEnviado = pedido.estado == "enviado"
            binding.btnMarcarEnviado.isEnabled = esPagado
            binding.btnMarcarEntregado.isEnabled = esEnviado

            binding.btnMarcarEnviado.setOnClickListener {
                pedidoViewModel.avanzarEstado(pedido.id, pedido.estado)
                binding.root.showSnackbar(getString(R.string.msg_order_shipped))
                binding.btnMarcarEnviado.isEnabled = false
            }
            binding.btnMarcarEntregado.setOnClickListener {
                pedidoViewModel.avanzarEstado(pedido.id, pedido.estado)
                binding.root.showSnackbar(getString(R.string.msg_order_delivered))
                binding.btnMarcarEntregado.isEnabled = false
            }
        }

        usuarioViewModel.usuarioActual.observe(viewLifecycleOwner) { usuario ->
            usuario ?: return@observe
            binding.tvNombreComprador.text = usuario.nombre
            binding.tvTelefonoComprador.text = usuario.telefono.ifEmpty {
                getString(R.string.demo_phone)
            }
        }

        pedidoViewModel.detallesPedido.observe(viewLifecycleOwner) { detalles ->
            adapter.submitList(detalles)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
