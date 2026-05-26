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

/*
 * Pantalla de detalle de un pedido individual.
 * Muestra el número de pedido, la fecha, la dirección de entrega, el estado actual
 * y la lista de productos incluidos con sus cantidades y subtotales.
 * Si el pedido está en estado PENDIENTE, ofrece dos acciones:
 *   - "Completar pedido": carga los productos del pedido al carrito para pagar de nuevo.
 *   - "Cancelar pedido": muestra un diálogo de confirmación y cancela el pedido.
 */
class DetallePedidoFragment : Fragment() {

    private var _binding: FragmentDetallePedidoBinding? = null
    private val binding get() = _binding!!

    // Argumentos de navegación que contienen el ID del pedido a mostrar
    private val args: DetallePedidoFragmentArgs by navArgs()

    // ViewModel que provee los datos del pedido y sus detalles
    private val viewModel: PedidoViewModel by activityViewModels()

    // ViewModel que gestiona el carrito, usado para la función "Completar pedido"
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    // Adaptador que muestra la lista de productos dentro del pedido
    private lateinit var adapter: DetallePedidoAdapter

    // Infla el layout del fragmento usando ViewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetallePedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Configura el adaptador, solicita al ViewModel los datos del pedido y sus detalles,
     * y vincula los observadores para poblar la vista y mostrar las acciones disponibles.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DetallePedidoAdapter()
        binding.rvProductosPedido.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductosPedido.adapter = adapter

        // Cargar el pedido cabecera y la lista de detalles por el ID recibido
        viewModel.selectPedido(args.pedidoId)
        viewModel.cargarDetalles(args.pedidoId)

        // Observar el pedido cabecera para poblar los campos de la vista
        viewModel.pedidoActual.observe(viewLifecycleOwner) { pedido ->
            pedido ?: return@observe

            binding.tvNumeroPedido.text = pedido.orderNumber

            // Formatear la marca de tiempo en milisegundos a un formato legible
            val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault())
            binding.tvFecha.text = sdf.format(Date(pedido.createdAt))

            binding.tvDireccion.text = pedido.direccionEntrega
            binding.tvTotal.text = pedido.totalPedido.toCOP()

            val estado = EstadoPedido.fromString(pedido.estado)
            // Mostrar el estado con la primera letra en mayúscula
            binding.chipEstado.text = pedido.estado.replaceFirstChar { it.uppercase() }

            // Mostrar los botones de acción solo si el pedido está pendiente de pago
            if (estado == EstadoPedido.PENDIENTE) {
                binding.btnCompletarPedido.visible()
                binding.btnCancelarPedido.visible()

                // Cargar los productos del pedido al carrito y navegar a la pestaña de carrito
                binding.btnCompletarPedido.setOnClickListener {
                    cargarEnCarritoYNavegar(pedido.usuarioId)
                }

                binding.btnCancelarPedido.setOnClickListener {
                    // Confirmar la cancelación con un diálogo antes de ejecutarla
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
                // Pedido ya pagado, enviado o entregado: ocultar los botones de acción
                binding.btnCompletarPedido.gone()
                binding.btnCancelarPedido.gone()
            }
        }

        // Observar la lista de detalles del pedido y actualizarla en el adaptador
        viewModel.detallesPedido.observe(viewLifecycleOwner) { detalles ->
            adapter.submitList(detalles)
        }
    }

    /*
     * Vacía el carrito actual del usuario, agrega todos los productos del pedido
     * como nuevos ítems de carrito y navega a la pestaña del carrito en la barra inferior.
     * Permite al comprador reintentar el pago de un pedido pendiente.
     * Consume: usuarioId (Long) del comprador propietario del pedido.
     */
    private fun cargarEnCarritoYNavegar(usuarioId: Long) {
        val detalles = viewModel.detallesPedido.value
        if (detalles.isNullOrEmpty()) {
            binding.root.showSnackbar("No se encontraron productos en este pedido")
            return
        }

        // Vaciar el carrito antes de volver a cargarlo con los productos del pedido
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

        // Seleccionar la pestaña del carrito en la barra de navegación inferior
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottomNavComprador)
            .selectedItemId = R.id.carritoFragment
    }

    // Libera el binding al destruir la vista para evitar fugas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
