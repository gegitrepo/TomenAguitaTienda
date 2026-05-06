package com.example.tomenaguita.ui.comprador.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentPasarelaPagoBinding
import com.example.tomenaguita.utils.showSnackbar

class PasarelaPagoFragment : Fragment() {

    private var _binding: FragmentPasarelaPagoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPasarelaPagoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rbEfectivo.isChecked = true

        binding.btnPagar.setOnClickListener {
            binding.root.showSnackbar(getString(R.string.msg_order_placed))
            findNavController().navigate(R.id.action_pago_to_historial)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
