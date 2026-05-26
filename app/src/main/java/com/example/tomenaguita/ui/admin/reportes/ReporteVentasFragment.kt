package com.example.tomenaguita.ui.admin.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Pedido
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.databinding.FragmentReporteVentasBinding
import com.example.tomenaguita.ui.adapter.PedidoAdapter
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.viewmodel.PedidoViewModel
import java.util.Calendar

// Fragmento del modulo administrador que muestra el reporte de ventas de la plataforma.
// Obtiene todos los pedidos desde Firestore via getAllPedidosForAdmin() porque la base de datos
// Room local del admin no contiene registros propios (los pedidos pertenecen a los compradores).
// Solo los pedidos con estado "pagado" se contabilizan como ventas en las metricas monetarias.
// Permite filtrar la lista de pedidos por estado mediante chips: todos, pagados, pendientes, cancelados.
// Muestra hasta Constants.MAX_REPORT_ITEMS pedidos en el listado para evitar listas demasiado largas.
class ReporteVentasFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_reporte_ventas.xml
    private var _binding: FragmentReporteVentasBinding? = null
    private val binding get() = _binding!!

    // Adaptador del RecyclerView que lista los pedidos filtrados
    private lateinit var adapter: PedidoAdapter

    // ViewModel de pedidos compartido a nivel de actividad; expone getAllPedidosForAdmin()
    private val viewModel: PedidoViewModel by activityViewModels()

    // Copia local de todos los pedidos recibidos de Firestore; base para los filtros por estado
    private var todosPedidos: List<Pedido> = emptyList()

    // Estado del filtro activo; null = todos, o el valor de EstadoPedido activo
    private var filtroActual: String? = null

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReporteVentasBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el RecyclerView, los chips de filtro y el observador de pedidos desde Firestore.
    // Al recibir la lista de pedidos calcula las metricas y aplica el filtro activo.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adaptador sin listener de clic (la vista de reporte es solo de lectura)
        adapter = PedidoAdapter { }
        binding.rvUltimasVentas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUltimasVentas.adapter = adapter

        // Configura los chips de filtro por estado
        setupChips()

        // Consultar desde Firestore para que el Room del admin no esté vacío
        // Al recibir los pedidos calcula las metricas del periodo y aplica el filtro actual
        viewModel.getAllPedidosForAdmin().observe(viewLifecycleOwner) { pedidos ->
            todosPedidos = pedidos
            calcularMetricas(pedidos)
            aplicarFiltro()
        }
    }

    // Asigna listeners a cada chip para cambiar el filtro activo y re-renderizar la lista.
    // Los chips disponibles son: todos, pagados, pendientes y cancelados.
    private fun setupChips() {
        binding.chipTodos.setOnClickListener      { filtroActual = null;                          aplicarFiltro() }
        binding.chipPagados.setOnClickListener    { filtroActual = EstadoPedido.PAGADO.valor;    aplicarFiltro() }
        binding.chipPendientes.setOnClickListener { filtroActual = EstadoPedido.PENDIENTE.valor; aplicarFiltro() }
        binding.chipCancelados.setOnClickListener { filtroActual = EstadoPedido.CANCELADO.valor; aplicarFiltro() }
    }

    // Calcula y muestra las metricas de ventas a partir de la lista completa de pedidos.
    // Solo los pedidos con estado "pagado" se suman en las ventas del dia, del mes y totales.
    // Los pedidos pendientes y cancelados se muestran como conteos, no como montos monetarios.
    // Consume: pedidos (lista completa de pedidos obtenida desde Firestore)
    private fun calcularMetricas(pedidos: List<Pedido>) {
        val hoy = Calendar.getInstance()

        // Separa los pedidos por estado para facilitar los calculos
        val pagados    = pedidos.filter { it.estado == EstadoPedido.PAGADO.valor }
        val pendientes = pedidos.filter { it.estado == EstadoPedido.PENDIENTE.valor }
        val cancelados = pedidos.filter { it.estado == EstadoPedido.CANCELADO.valor }

        // Ventas del día — solo pagados
        // Suma los totales de los pedidos pagados cuya fecha coincide con el dia de hoy
        val ventasDia = pagados
            .filter { pedido ->
                val cal = Calendar.getInstance().apply { timeInMillis = pedido.createdAt }
                cal.get(Calendar.DAY_OF_YEAR) == hoy.get(Calendar.DAY_OF_YEAR) &&
                cal.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)
            }
            .sumOf { it.totalPedido }

        // Ventas del mes — solo pagados
        // Suma los totales de los pedidos pagados del mes y anio actuales
        val ventasMes = pagados
            .filter { pedido ->
                val cal = Calendar.getInstance().apply { timeInMillis = pedido.createdAt }
                cal.get(Calendar.MONTH) == hoy.get(Calendar.MONTH) &&
                cal.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)
            }
            .sumOf { it.totalPedido }

        // Ventas totales — solo pagados
        // Suma todos los totales de pedidos pagados sin importar la fecha
        val ventasTotales = pagados.sumOf { it.totalPedido }

        // Actualiza las tarjetas de metricas en la interfaz con los valores calculados
        binding.tvVentasDia.text       = ventasDia.toCOP()
        binding.tvVentasMes.text       = ventasMes.toCOP()
        binding.tvVentasTotales.text   = ventasTotales.toCOP()
        binding.tvVentasPendientes.text = pendientes.size.toString()
        binding.tvVentasCanceladas.text = cancelados.size.toString()
    }

    // Aplica el filtro activo a la lista completa de pedidos y actualiza el RecyclerView.
    // Limita el numero de pedidos mostrados a Constants.MAX_REPORT_ITEMS para no sobrecargar la vista.
    // Si filtroActual es "todos", muestra todos los pedidos sin filtrar por estado.
    private fun aplicarFiltro() {
        val filtrados = if (filtroActual == null) todosPedidos
        else todosPedidos.filter { it.estado == filtroActual }
        // Muestra solo los primeros MAX_REPORT_ITEMS pedidos del resultado filtrado
        adapter.submitList(filtrados.take(Constants.MAX_REPORT_ITEMS))
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
