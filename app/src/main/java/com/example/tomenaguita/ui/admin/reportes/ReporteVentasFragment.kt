package com.example.tomenaguita.ui.admin.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Pedido
import com.example.tomenaguita.databinding.FragmentReporteVentasBinding
import com.example.tomenaguita.ui.adapter.PedidoAdapter

class ReporteVentasFragment : Fragment() {

    private var _binding: FragmentReporteVentasBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PedidoAdapter

    private val demoVentas = listOf(
        Pedido(1L, "TA-20240501-0001", 3L, 7000.0, 0.0, 7000.0, "Calle 123 # 45-67", estado = "entregado", metodoPago = "Efectivo"),
        Pedido(2L, "TA-20240430-0012", 3L, 14500.0, 0.0, 14500.0, "Cra 7 # 89-12", estado = "entregado", metodoPago = "Tarjeta"),
        Pedido(3L, "TA-20240429-0009", 4L, 30000.0, 0.0, 30000.0, "Av 68 # 32-11", estado = "entregado", metodoPago = "Transferencia"),
        Pedido(4L, "TA-20240428-0007", 5L, 12000.0, 0.0, 12000.0, "Calle 80 # 20-30", estado = "enviado", metodoPago = "Efectivo"),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReporteVentasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvVentasDia.text = "$85.000"
        binding.tvVentasMes.text = "$2.4M"

        adapter = PedidoAdapter { }
        binding.rvUltimasVentas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUltimasVentas.adapter = adapter
        adapter.submitList(demoVentas)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
