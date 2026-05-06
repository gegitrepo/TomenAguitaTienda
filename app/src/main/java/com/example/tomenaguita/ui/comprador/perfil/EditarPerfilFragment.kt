package com.example.tomenaguita.ui.comprador.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.databinding.FragmentEditarPerfilBinding
import com.example.tomenaguita.utils.showSnackbar

class EditarPerfilFragment : Fragment() {

    private var _binding: FragmentEditarPerfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditarPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etNombre.setText("Carlos Comprador")
        binding.etTelefono.setText("3001234567")
        binding.etDireccion.setText("Calle 123 # 45-67, Bogotá")

        binding.btnGuardar.setOnClickListener {
            binding.root.showSnackbar("Perfil actualizado")
            findNavController().popBackStack()
        }
        binding.btnCamara.setOnClickListener { binding.root.showSnackbar("Abrir cámara (requiere permiso)") }
        binding.btnGaleria.setOnClickListener { binding.root.showSnackbar("Abrir galería (requiere permiso)") }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
