package com.example.tomenaguita.ui.comprador.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.entity.CarritoItem
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.databinding.FragmentDetallePedidoBinding
import com.example.tomenaguita.ui.adapter.DetallePedidoAdapter
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.CarritoViewModel
import com.example.tomenaguita.viewmodel.PedidoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetallePedidoFragment : Fragment() {

    private var _binding: FragmentDetallePedidoBinding? = null
    private val binding get() = _binding!!
    private val args: DetallePedidoFragmentArgs by navArgs()
    private val viewModel: PedidoViewModel by activityViewModels()
    private val carritoViewModel: CarritoViewModel by activityViewModels()
    private lateinit var adapter: DetallePedidoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetallePedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DetallePedidoAdapter()
        binding.rvProductosPedido.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductosPedido.adapter = adapter

        viewModel.selectPedido(args.pedidoId)
        viewModel.cargarDetalles(args.pedidoId)

        viewModel.pedidoActual.observe(viewLifecycleOwner) { pedido ->
            pedido ?: return@observe

            binding.tvNumeroPedido.text = pedido.orderNumber

            val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault())
            binding.tvFecha.text = sdf.format(Date(pedido.createdAt))

            binding.tvDireccion.text = pedido.direccionEntrega
            binding.tvTotal.text = pedido.totalPedido.toCOP()

            val estado = EstadoPedido.fromString(pedido.estado)
            binding.chipEstado.text = pedido.estado.replaceFirstChar { it.uppercase() }

            if (estado == EstadoPedido.PENDIENTE) {
                binding.btnCompletarPedido.visible()
                binding.btnCancelarPedido.visible()

                binding.btnCompletarPedido.setOnClickListener {
                    cargarEnCarritoYNavegar(pedido.usuarioId)
                }

                binding.btnCancelarPedido.setOnClickListener {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Cancelar pedido")
                        .setMessage("¿Deseas cancelar el pedido ${pedido.orderNumber}? Esta acción no se puede deshacer.")
                        .setPositiveButton("Sí, cancelar") { _, _ ->
                            viewModel.cancelarPedido(pedido.id)
                            findNavController().popBackStack()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            } else {
                binding.btnCompletarPedido.gone()
                binding.btnCancelarPedido.gone()
            }
        }

        viewModel.detallesPedido.observe(viewLifecycleOwner) { detalles ->
            adapter.submitList(detalles)
        }
    }

    private fun cargarEnCarritoYNavegar(usuarioId: Long) {
        val detalles = viewModel.detallesPedido.value
        if (detalles.isNullOrEmpty()) {
            binding.root.showSnackbar("No se encontraron productos en este pedido")
            return
        }

        carritoViewModel.vaciarCarrito(usuarioId)
        detalles.forEach { detalle ->
            carritoViewModel.agregarItem(
                CarritoItem(
                    usuarioId = usuarioId,
                    productoId = detalle.productoId,
                    cantidad = detalle.cantidad,
                    precioAlMomento = detalle.precioUnitario
                )
            )
        }

        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottomNavComprador)
            .selectedItemId = R.id.carritoFragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
