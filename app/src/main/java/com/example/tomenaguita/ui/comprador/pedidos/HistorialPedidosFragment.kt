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
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.databinding.FragmentHistorialPedidosBinding
import com.example.tomenaguita.ui.adapter.PedidoAdapter
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.PedidoViewModel

/*
 * Pantalla de historial de pedidos del comprador.
 * Muestra todos los pedidos realizados por el usuario autenticado,
 * con la posibilidad de filtrarlos por estado mediante un grupo de chips:
 * Pendiente, Pagado, Enviado o Entregado.
 * La vista "Todos" excluye pedidos cancelados para mantener el historial limpio.
 * Al pulsar un pedido navega al detalle completo de ese pedido.
 */
class HistorialPedidosFragment : Fragment() {

    private var _binding: FragmentHistorialPedidosBinding? = null
    private val binding get() = _binding!!

    // Adaptador que muestra la lista de pedidos y notifica el clic en cada fila
    private lateinit var adapter: PedidoAdapter

    // ViewModel que provee los pedidos del usuario desde Room y Firestore
    private val viewModel: PedidoViewModel by activityViewModels()

    // Lista completa de pedidos del usuario, usada como base para el filtro por estado
    private var allPedidos: List<Pedido> = emptyList()

    // Infla el layout del fragmento usando ViewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistorialPedidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Configura el adaptador, el RecyclerView y el listener del chip group de filtros.
     * Observa el LiveData de pedidos del usuario y aplica el filtro activo cada vez que cambia.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Al pulsar una fila, navegar al detalle del pedido seleccionado
        adapter = PedidoAdapter { pedido ->
            val action = HistorialPedidosFragmentDirections.actionHistorialToDetalle(pedido.id)
            findNavController().navigate(action)
        }
        binding.rvPedidos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPedidos.adapter = adapter

        val session = SessionManager(requireContext())

        // Observar los pedidos del usuario y actualizar la lista al recibir cambios
        viewModel.getPedidosByUsuario(session.getUserId())
            .observe(viewLifecycleOwner) { pedidos ->
                allPedidos = pedidos
                applyFilter()
            }

        // Re-aplicar el filtro cada vez que el usuario cambia el chip seleccionado
        binding.chipGroupFiltros.setOnCheckedStateChangeListener { _, _ -> applyFilter() }
    }

    /*
     * Filtra la lista de pedidos según el chip de estado activo en el grupo de filtros.
     * Si no hay chip seleccionado (opción "Todos"), excluye los pedidos cancelados.
     * Actualiza el adaptador y muestra u oculta el mensaje de lista vacía.
     */
    private fun applyFilter() {
        val filtered = when {
            binding.chipPendiente.isChecked  -> allPedidos.filter { it.estado == EstadoPedido.PENDIENTE.valor }
            binding.chipPagado.isChecked     -> allPedidos.filter { it.estado == EstadoPedido.PAGADO.valor }
            binding.chipEnviado.isChecked    -> allPedidos.filter { it.estado == EstadoPedido.ENVIADO.valor }
            binding.chipEntregado.isChecked  -> allPedidos.filter { it.estado == EstadoPedido.ENTREGADO.valor }
            // "Todos" excluye los cancelados — los pedidos cancelados desaparecen del historial
            else -> allPedidos.filter { it.estado != EstadoPedido.CANCELADO.valor }
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

    // Libera el binding al destruir la vista para evitar fugas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
