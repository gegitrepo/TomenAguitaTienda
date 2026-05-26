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
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.viewmodel.PedidoViewModel
import com.example.tomenaguita.viewmodel.UsuarioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Fragmento del modulo vendedor que muestra el detalle completo de un pedido recibido.
// Presenta el numero de pedido, fecha, estado, datos del comprador, productos incluidos y total.
// Permite al vendedor avanzar el estado del pedido: de "pagado" a "enviado" y de "enviado" a "entregado".
class DetallePedidoVendedorFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_detalle_pedido_vendedor.xml
    private var _binding: FragmentDetallePedidoVendedorBinding? = null
    private val binding get() = _binding!!

    // Argumentos de navegacion que contienen el ID del pedido a mostrar
    private val args: DetallePedidoVendedorFragmentArgs by navArgs()

    // ViewModel de pedidos compartido a nivel de actividad
    private val pedidoViewModel: PedidoViewModel by activityViewModels()

    // ViewModel de usuarios compartido a nivel de actividad, usado para cargar datos del comprador
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()

    // Adaptador del RecyclerView que lista los productos del pedido
    private lateinit var adapter: DetallePedidoAdapter

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetallePedidoVendedorBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el RecyclerView, carga el pedido y sus detalles, y registra los observadores
    // para mostrar la informacion del pedido, del comprador y los botones de avance de estado.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura el RecyclerView con el adaptador de lineas de detalle del pedido
        adapter = DetallePedidoAdapter()
        binding.rvProductosPedido.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductosPedido.adapter = adapter

        // Selecciona el pedido activo y carga sus lineas de detalle en el ViewModel
        pedidoViewModel.selectPedido(args.pedidoId)
        pedidoViewModel.cargarDetalles(args.pedidoId)

        // Observa el pedido activo y puebla el encabezado con numero, fecha, estado, direccion y total
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

            // El boton "Marcar como enviado" solo esta activo si el pedido esta en estado pagado
            val esPagado = pedido.estado == EstadoPedido.PAGADO.valor
            // El boton "Marcar como entregado" solo esta activo si el pedido esta en estado enviado
            val esEnviado = pedido.estado == EstadoPedido.ENVIADO.valor
            binding.btnMarcarEnviado.isEnabled = esPagado
            binding.btnMarcarEntregado.isEnabled = esEnviado

            // Avanza el estado del pedido a "enviado" y deshabilita el boton para evitar doble clic
            binding.btnMarcarEnviado.setOnClickListener {
                pedidoViewModel.avanzarEstado(pedido.id, pedido.estado)
                binding.root.showSnackbar(getString(R.string.msg_order_shipped))
                binding.btnMarcarEnviado.isEnabled = false
            }
            // Avanza el estado del pedido a "entregado" y deshabilita el boton para evitar doble clic
            binding.btnMarcarEntregado.setOnClickListener {
                pedidoViewModel.avanzarEstado(pedido.id, pedido.estado)
                binding.root.showSnackbar(getString(R.string.msg_order_delivered))
                binding.btnMarcarEntregado.isEnabled = false
            }
        }

        // Observa el usuario comprador y muestra su nombre y telefono; usa valor de demo si no tiene telefono registrado
        usuarioViewModel.usuarioActual.observe(viewLifecycleOwner) { usuario ->
            usuario ?: return@observe
            binding.tvNombreComprador.text = usuario.nombre
            binding.tvTelefonoComprador.text = usuario.telefono.ifEmpty {
                getString(R.string.demo_phone)
            }
        }

        // Observa los detalles del pedido (lineas de producto) y los entrega al adaptador
        pedidoViewModel.detallesPedido.observe(viewLifecycleOwner) { detalles ->
            adapter.submitList(detalles)
        }
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
