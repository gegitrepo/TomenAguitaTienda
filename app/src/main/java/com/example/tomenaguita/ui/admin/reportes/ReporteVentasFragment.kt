package com.example.tomenaguita.ui.admin.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.databinding.FragmentReporteVentasBinding
import com.example.tomenaguita.ui.adapter.PedidoAdapter
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.viewmodel.PedidoViewModel
import java.util.Calendar

class ReporteVentasFragment : Fragment() {

    private var _binding: FragmentReporteVentasBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PedidoAdapter
    private val viewModel: PedidoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReporteVentasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PedidoAdapter { }
        binding.rvUltimasVentas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUltimasVentas.adapter = adapter

        viewModel.getAllPedidos().observe(viewLifecycleOwner) { pedidos ->
            val hoy = Calendar.getInstance()

            val ventasDia = pedidos
                .filter { pedido ->
                    val cal = Calendar.getInstance().apply { timeInMillis = pedido.createdAt }
                    cal.get(Calendar.DAY_OF_YEAR) == hoy.get(Calendar.DAY_OF_YEAR) &&
                    cal.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)
                }
                .sumOf { it.totalPedido }

            val ventasMes = pedidos
                .filter { pedido ->
                    val cal = Calendar.getInstance().apply { timeInMillis = pedido.createdAt }
                    cal.get(Calendar.MONTH) == hoy.get(Calendar.MONTH) &&
                    cal.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)
                }
                .sumOf { it.totalPedido }

            binding.tvVentasDia.text = ventasDia.toCOP()
            binding.tvVentasMes.text = ventasMes.toCOP()

            // Mostrar pedidos recientes (máximo los últimos 20 para el reporte)
            adapter.submitList(pedidos.take(20))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
