package com.example.tomenaguita.ui.admin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTotalUsuarios.text = "42"
        binding.tvTotalProductos.text = "8"
        binding.tvTotalPedidos.text = "156"
        binding.tvTotalVentas.text = "$2.4M"

        binding.btnGestionUsuarios.setOnClickListener {
            findNavController().navigate(R.id.listaUsuariosFragment)
        }
        binding.btnGestionProductos.setOnClickListener {
            findNavController().navigate(R.id.gestionProductosFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
