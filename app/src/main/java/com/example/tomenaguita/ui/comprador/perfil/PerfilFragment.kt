package com.example.tomenaguita.ui.comprador.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentPerfilBinding
import com.example.tomenaguita.ui.auth.LoginActivity
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.showSnackbar

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireContext())
        binding.tvNombre.text = session.getUserNombre() ?: "Carlos Comprador"
        binding.tvEmail.text = session.getUserEmail() ?: "comprador@tomenaguita.com"
        binding.tvTelefono.text = "3001234567"
        binding.tvDireccion.text = "Calle 123 # 45-67, Bogotá"

        binding.btnEditarPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_perfil_to_editar)
        }

        binding.btnLogout.setOnClickListener {
            session.clearSession()
            startActivity(Intent(requireActivity(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
