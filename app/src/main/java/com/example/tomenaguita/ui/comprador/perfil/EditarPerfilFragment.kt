package com.example.tomenaguita.ui.comprador.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
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
        binding.etNombre.setText(getString(R.string.demo_comprador_name))
        binding.etTelefono.setText(getString(R.string.demo_phone))
        binding.etDireccion.setText(getString(R.string.demo_address))

        binding.btnGuardar.setOnClickListener {
            binding.root.showSnackbar(getString(R.string.msg_profile_updated))
            findNavController().popBackStack()
        }
        binding.btnCamara.setOnClickListener { binding.root.showSnackbar(getString(R.string.msg_open_camera)) }
        binding.btnGaleria.setOnClickListener { binding.root.showSnackbar(getString(R.string.msg_open_gallery)) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
