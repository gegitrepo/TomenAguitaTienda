package com.example.tomenaguita.ui.vendedor.pedidos

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
import com.example.tomenaguita.databinding.FragmentPedidosRecibidosBinding
import com.example.tomenaguita.ui.adapter.PedidoAdapter
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.PedidoViewModel

// Fragmento del modulo vendedor que lista todos los pedidos recibidos.
// Los pedidos se obtienen directamente desde Firestore mediante getAllPedidosForVendedor,
// ya que la base de datos Room local del vendedor esta vacia: los pedidos son creados por
// el comprador en su propio dispositivo y solo existen en Firestore.
// Permite filtrar pedidos por estado usando chips (Todos, Pendiente, Pagado, Enviado).
class PedidosRecibidosFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_pedidos_recibidos.xml
    private var _binding: FragmentPedidosRecibidosBinding? = null
    private val binding get() = _binding!!

    // Adaptador del RecyclerView de pedidos
    private lateinit var adapter: PedidoAdapter

    // ViewModel compartido a nivel de actividad para consultar pedidos
    private val viewModel: PedidoViewModel by activityViewModels()

    // Lista completa de pedidos sin filtrar, utilizada como fuente para el filtrado por estado
    private var todosPedidos: List<Pedido> = emptyList()

    // Estado del filtro activo; null significa que se muestran todos los pedidos
    private var filtroActual: String? = null   // null = todos

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidosRecibidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el RecyclerView, los chips de filtro y el observador de pedidos desde Firestore.
    // Se llama despues de que la vista ha sido creada.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Al pulsar un pedido de la lista navega a su pantalla de detalle
        adapter = PedidoAdapter { pedido ->
            findNavController().navigate(
                PedidosRecibidosFragmentDirections.actionPedidosToDetalle(pedido.id)
            )
        }
        binding.rvPedidosRecibidos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPedidosRecibidos.adapter = adapter

        setupChips()

        // Consulta pedidos desde Firestore y actualiza la lista al recibir nuevos datos
        viewModel.getAllPedidosForVendedor().observe(viewLifecycleOwner) { pedidos ->
            todosPedidos = pedidos
            aplicarFiltro()
        }
    }

    // ─── Chips de filtro ────────────────────────────────────────────────────

    // Asigna los listeners a cada chip de filtro para actualizar el estado activo y recargar la lista
    private fun setupChips() {
        binding.chipTodos.setOnClickListener    { filtroActual = null;                       aplicarFiltro() }
        binding.chipPendiente.setOnClickListener { filtroActual = EstadoPedido.PENDIENTE.valor; aplicarFiltro() }
        binding.chipPagado.setOnClickListener   { filtroActual = EstadoPedido.PAGADO.valor;    aplicarFiltro() }
        binding.chipEnviado.setOnClickListener  { filtroActual = EstadoPedido.ENVIADO.valor;   aplicarFiltro() }
    }

    // Filtra la lista de pedidos segun el estado activo en filtroActual.
    // Si filtroActual es null se muestran todos los pedidos.
    // Actualiza el adaptador y la visibilidad del mensaje de lista vacia.
    private fun aplicarFiltro() {
        val filtrados = if (filtroActual == null) {
            todosPedidos
        } else {
            todosPedidos.filter { it.estado == filtroActual }
        }

        adapter.submitList(filtrados)

        if (filtrados.isEmpty()) {
            binding.tvEmpty.visible()
            binding.rvPedidosRecibidos.gone()
        } else {
            binding.tvEmpty.gone()
            binding.rvPedidosRecibidos.visible()
        }
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
