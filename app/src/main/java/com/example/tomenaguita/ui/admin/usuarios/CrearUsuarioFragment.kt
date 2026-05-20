package com.example.tomenaguita.ui.admin.usuarios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentCrearUsuarioBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.isValidColombian
import com.example.tomenaguita.utils.isValidEmail
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.viewmodel.UsuarioViewModel

class CrearUsuarioFragment : Fragment() {

    private var _binding: FragmentCrearUsuarioBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UsuarioViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCrearUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rolesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Constants.ROLES.map { it.replaceFirstChar { c -> c.uppercase() } }
        )
        binding.acRol.setAdapter(rolesAdapter)
        binding.acRol.setText(getString(R.string.rol_comprador), false)

        viewModel.operationResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe          // null = evento ya consumido, ignorar
            viewModel.clearOperationResult()  // consumir para evitar re-entrega al volver
            binding.btnCrearUsuario.isEnabled = true
            result.fold(
                onSuccess = {
                    val nombre = binding.etNombre.text.toString().trim()
                    binding.root.showSnackbar(getString(R.string.msg_user_created, nombre))
                    findNavController().popBackStack()
                },
                onFailure = {
                    binding.root.showSnackbar(it.message ?: getString(R.string.error_field_required))
                }
            )
        }

        binding.btnCrearUsuario.setOnClickListener { crearUsuario() }
    }

    private fun crearUsuario() {
        val nombre = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val rol = binding.acRol.text.toString().lowercase()

        binding.tilNombre.error = null
        binding.tilEmail.error = null
        binding.tilTelefono.error = null
        binding.tilPassword.error = null

        if (nombre.length < 3) { binding.tilNombre.error = getString(R.string.error_name_short); return }
        if (!email.isValidEmail()) { binding.tilEmail.error = getString(R.string.error_email_invalid); return }
        if (!telefono.isValidColombian()) { binding.tilTelefono.error = getString(R.string.error_phone_invalid); return }
        if (password.length < 8) { binding.tilPassword.error = getString(R.string.error_password_short); return }

        binding.btnCrearUsuario.isEnabled = false
        viewModel.crearUsuario(nombre, email, telefono, password, rol)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
