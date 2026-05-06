package com.example.tomenaguita.ui.admin.usuarios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tomenaguita.databinding.FragmentEditarUsuarioBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.showSnackbar

class EditarUsuarioFragment : Fragment() {

    private var _binding: FragmentEditarUsuarioBinding? = null
    private val binding get() = _binding!!
    private val args: EditarUsuarioFragmentArgs by navArgs()

    private val demoUsuarios = mapOf(
        1L to Triple("Administrador", "admin@tomenaguita.com", "administrador"),
        2L to Triple("Vendedor Demo", "vendedor@tomenaguita.com", "vendedor"),
        3L to Triple("Carlos Comprador", "comprador@tomenaguita.com", "comprador"),
        4L to Triple("María González", "maria@gmail.com", "comprador"),
        5L to Triple("Juan Pérez", "juan@gmail.com", "comprador"),
    )

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

        demoUsuarios[args.usuarioId]?.let { (nombre, email, rol) ->
            binding.etNombre.setText(nombre)
            binding.etEmail.setText(email)
            binding.etTelefono.setText("300${args.usuarioId}000000")
            binding.acRol.setText(rol.replaceFirstChar { it.uppercase() }, false)
        }

        binding.btnGuardar.setOnClickListener {
            binding.root.showSnackbar("Usuario actualizado")
            findNavController().popBackStack()
        }

        binding.btnEliminar.setOnClickListener {
            binding.root.showSnackbar("Usuario desactivado")
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
