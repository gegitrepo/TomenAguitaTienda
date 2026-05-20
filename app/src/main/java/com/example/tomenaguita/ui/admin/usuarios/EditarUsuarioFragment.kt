package com.example.tomenaguita.ui.admin.usuarios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentEditarUsuarioBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.viewmodel.UsuarioViewModel

class EditarUsuarioFragment : Fragment() {

    private var _binding: FragmentEditarUsuarioBinding? = null
    private val binding get() = _binding!!
    private val args: EditarUsuarioFragmentArgs by navArgs()
    private val viewModel: UsuarioViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditarUsuarioBinding.inflate(inflater, container, false)
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

        viewModel.cargarUsuario(args.usuarioId)

        viewModel.usuarioActual.observe(viewLifecycleOwner) { usuario ->
            usuario ?: return@observe
            binding.etNombre.setText(usuario.nombre)
            binding.etEmail.setText(usuario.email)
            binding.etTelefono.setText(usuario.telefono)
            binding.acRol.setText(usuario.rol.replaceFirstChar { it.uppercase() }, false)
        }

        viewModel.operationResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe          // null = evento ya consumido, ignorar
            viewModel.clearOperationResult()  // consumir para evitar re-entrega al volver
            result.fold(
                onSuccess = { findNavController().popBackStack() },
                onFailure = { binding.root.showSnackbar(it.message ?: getString(R.string.error_field_required)) }
            )
        }

        binding.btnGuardar.setOnClickListener {
            val usuario = viewModel.usuarioActual.value ?: return@setOnClickListener
            viewModel.actualizarPerfil(usuario.copy(
                nombre = binding.etNombre.text.toString().trim(),
                telefono = binding.etTelefono.text.toString().trim(),
                rol = binding.acRol.text.toString().lowercase()
            ))
            binding.root.showSnackbar(getString(R.string.msg_user_updated))
        }

        binding.btnEliminar.setOnClickListener {
            viewModel.desactivarUsuario(args.usuarioId)
            binding.root.showSnackbar(getString(R.string.msg_user_deactivated))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
