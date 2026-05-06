package com.example.tomenaguita.ui.comprador.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentResumenPedidoBinding
import com.example.tomenaguita.utils.toCOP

class ResumenPedidoFragment : Fragment() {

    private var _binding: FragmentResumenPedidoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResumenPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subtotal = 7000.0
        binding.tvSubtotal.text = subtotal.toCOP()
        binding.tvTotal.text = subtotal.toCOP()
        binding.tvDireccion.text = "Calle 123 # 45-67, Bogotá"

        binding.btnConfirmar.setOnClickListener {
            findNavController().navigate(R.id.action_resumen_to_pago)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
