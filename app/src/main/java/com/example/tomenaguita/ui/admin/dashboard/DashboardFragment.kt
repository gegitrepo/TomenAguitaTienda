package com.example.tomenaguita.ui.admin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.databinding.FragmentDashboardBinding
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.viewmodel.PedidoViewModel
import com.example.tomenaguita.viewmodel.ProductoViewModel
import com.example.tomenaguita.viewmodel.UsuarioViewModel

// Fragmento del panel de control del administrador.
// Muestra metricas globales de la plataforma: total de usuarios, productos disponibles,
// pedidos registrados, ventas acumuladas (solo pedidos pagados), pedidos pendientes y cancelados.
// Los pedidos se obtienen desde Firestore a traves de getAllPedidosForAdmin() porque la base de
// datos Room local del admin no contiene registros propios (los pedidos pertenecen a los compradores).
// Incluye accesos directos a la gestion de usuarios y productos.
class DashboardFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_dashboard.xml
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // ViewModel de usuarios compartido a nivel de actividad; provee el total de usuarios registrados
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()

    // ViewModel de productos compartido a nivel de actividad; provee el total de productos disponibles
    private val productoViewModel: ProductoViewModel by activityViewModels()

    // ViewModel de pedidos compartido a nivel de actividad; provee las metricas de pedidos y ventas
    private val pedidoViewModel: PedidoViewModel by activityViewModels()

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Registra los observadores de los ViewModels para actualizar las tarjetas de metricas
    // y configura los botones de acceso rapido a la gestion de usuarios y productos.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa la lista completa de usuarios y muestra su cantidad total en pantalla
        usuarioViewModel.todosLosUsuarios.observe(viewLifecycleOwner) { usuarios ->
            binding.tvTotalUsuarios.text = usuarios.size.toString()
        }

        // Observa los productos disponibles y muestra su cantidad total en pantalla
        productoViewModel.productosDisponibles.observe(viewLifecycleOwner) { productos ->
            binding.tvTotalProductos.text = productos.size.toString()
        }

        // Consultar pedidos desde Firestore para que el Room del admin no esté vacío
        // Calcula y muestra: total de pedidos, ventas acumuladas (solo pagados),
        // cantidad de pedidos pendientes y cantidad de pedidos cancelados
        pedidoViewModel.getAllPedidosForAdmin().observe(viewLifecycleOwner) { pedidos ->
            binding.tvTotalPedidos.text = pedidos.size.toString()

            // Solo los pedidos pagados cuentan como ventas
            val pagados = pedidos.filter { it.estado == EstadoPedido.PAGADO.valor }
            binding.tvTotalVentas.text = pagados.sumOf { it.totalPedido }.toCOP()

            // Métricas adicionales
            val pendientesCount = pedidos.count { it.estado == EstadoPedido.PENDIENTE.valor }
            val canceladosCount = pedidos.count { it.estado == EstadoPedido.CANCELADO.valor }
            binding.tvPedidosPendientes.text = pendientesCount.toString()
            binding.tvPedidosCancelados.text = canceladosCount.toString()
        }

        // Navega al listado de usuarios al pulsar el boton de acceso rapido
        binding.btnGestionUsuarios.setOnClickListener {
            findNavController().navigate(R.id.listaUsuariosFragment)
        }

        // Navega a la gestion de productos al pulsar el boton de acceso rapido
        binding.btnGestionProductos.setOnClickListener {
            findNavController().navigate(R.id.gestionProductosFragment)
        }
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
