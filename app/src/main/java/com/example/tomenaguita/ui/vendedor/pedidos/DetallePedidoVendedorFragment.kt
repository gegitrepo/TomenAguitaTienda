package com.example.tomenaguita.ui.vendedor.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentDetallePedidoVendedorBinding
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP

class DetallePedidoVendedorFragment : Fragment() {

    private var _binding: FragmentDetallePedidoVendedorBinding? = null
    private val binding get() = _binding!!
    private val args: DetallePedidoVendedorFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetallePedidoVendedorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNombreComprador.text = getString(R.string.demo_comprador_name)
        binding.tvTelefonoComprador.text = getString(R.string.demo_phone)
        binding.tvDireccionComprador.text = getString(R.string.demo_address)
        binding.tvTotal.text = 7000.0.toCOP()

        binding.btnMarcarEnviado.setOnClickListener {
            binding.root.showSnackbar(getString(R.string.msg_order_shipped))
            binding.btnMarcarEnviado.isEnabled = false
        }
        binding.btnMarcarEntregado.setOnClickListener {
            binding.root.showSnackbar(getString(R.string.msg_order_delivered))
            binding.btnMarcarEntregado.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
