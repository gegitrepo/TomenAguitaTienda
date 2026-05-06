package com.example.tomenaguita.ui.admin.usuarios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.databinding.FragmentCrearUsuarioBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.isValidColombian
import com.example.tomenaguita.utils.isValidEmail
import com.example.tomenaguita.utils.showSnackbar

class CrearUsuarioFragment : Fragment() {

    private var _binding: FragmentCrearUsuarioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCrearUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rolesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, Constants.ROLES.map { it.replaceFirstChar { c -> c.uppercase() } })
        binding.acRol.setAdapter(rolesAdapter)
        binding.acRol.setText("Comprador", false)

        binding.btnCrearUsuario.setOnClickListener { crearUsuario() }
    }

    private fun crearUsuario() {
        val nombre = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val password = binding.etPassword.text.toString()

        binding.tilNombre.error = null
        binding.tilEmail.error = null
        binding.tilTelefono.error = null
        binding.tilPassword.error = null

        if (nombre.length < 3) { binding.tilNombre.error = "Mínimo 3 caracteres"; return }
        if (!email.isValidEmail()) { binding.tilEmail.error = "Correo inválido"; return }
        if (!telefono.isValidColombian()) { binding.tilTelefono.error = "Teléfono inválido"; return }
        if (password.length < 8) { binding.tilPassword.error = "Mínimo 8 caracteres"; return }

        binding.root.showSnackbar("Usuario $nombre creado")
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
