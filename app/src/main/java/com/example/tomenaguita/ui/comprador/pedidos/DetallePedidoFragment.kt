package com.example.tomenaguita.ui.comprador.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tomenaguita.databinding.FragmentDetallePedidoBinding
import com.example.tomenaguita.utils.toCOP

class DetallePedidoFragment : Fragment() {

    private var _binding: FragmentDetallePedidoBinding? = null
    private val binding get() = _binding!!
    private val args: DetallePedidoFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetallePedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNumeroPedido.text = "TA-2024050${args.pedidoId}-000${args.pedidoId}"
        binding.tvFecha.text = "01/05/2024"
        binding.tvDireccion.text = "Calle 123 # 45-67, Bogotá"
        binding.tvTotal.text = 7000.0.toCOP()
        binding.chipEstado.text = "Pendiente"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
