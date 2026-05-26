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

// Fragmento del modulo administrador que permite editar los datos de un usuario existente.
// Carga el usuario desde el ViewModel usando el ID recibido como argumento de navegacion.
// El switch "Activo" indica si el usuario esta habilitado en la plataforma.
// El boton btnGuardar guarda los cambios de nombre, telefono, rol y estado activo.
// El boton btnEliminar cambia su texto segun el estado actual del switch:
//   - Si el usuario esta activo, muestra "Desactivar usuario" y lo desactiva al pulsarlo.
//   - Si el usuario esta inactivo, muestra "Activar usuario" y lo activa al pulsarlo.
class EditarUsuarioFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_editar_usuario.xml
    private var _binding: FragmentEditarUsuarioBinding? = null
    private val binding get() = _binding!!

    // Argumentos de navegacion que contienen el ID del usuario a editar
    private val args: EditarUsuarioFragmentArgs by navArgs()

    // ViewModel de usuarios compartido a nivel de actividad; expone cargarUsuario(), actualizarPerfil() y desactivarUsuario()
    private val viewModel: UsuarioViewModel by activityViewModels()

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditarUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el desplegable de roles, carga el usuario, registra los observadores del ViewModel
    // y define los listeners de los botones de guardado y cambio de estado.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura el adaptador del campo de rol con las opciones disponibles en mayuscula inicial
        val rolesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Constants.ROLES.map { it.replaceFirstChar { c -> c.uppercase() } }
        )
        binding.acRol.setAdapter(rolesAdapter)

        // Solicita al ViewModel que cargue los datos del usuario con el ID indicado
        viewModel.cargarUsuario(args.usuarioId)

        // Observa el usuario cargado y rellena los campos del formulario con sus datos actuales
        viewModel.usuarioActual.observe(viewLifecycleOwner) { usuario ->
            usuario ?: return@observe
            binding.etNombre.setText(usuario.nombre)
            binding.etEmail.setText(usuario.email)
            binding.etTelefono.setText(usuario.telefono)
            binding.acRol.setText(usuario.rol.replaceFirstChar { it.uppercase() }, false)

            // Conectar switch con el estado activo del usuario
            binding.switchActivo.isChecked = usuario.activo == 1

            // El texto del botón refleja la acción disponible
            actualizarTextoBotonEstado(usuario.activo == 1)
        }

        // Observa el resultado de la operacion de guardado; al exito regresa al listado de usuarios
        viewModel.operationResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            viewModel.clearOperationResult()
            result.fold(
                onSuccess = { findNavController().popBackStack() },
                onFailure = { binding.root.showSnackbar(it.message ?: getString(R.string.error_field_required)) }
            )
        }

        // Guarda los cambios de nombre, telefono, rol y estado activo del usuario
        // Toma el valor actual del switch para determinar si el usuario queda activo o inactivo
        binding.btnGuardar.setOnClickListener {
            val usuario = viewModel.usuarioActual.value ?: return@setOnClickListener
            viewModel.actualizarPerfil(
                usuario.copy(
                    nombre   = binding.etNombre.text.toString().trim(),
                    telefono = binding.etTelefono.text.toString().trim(),
                    rol      = binding.acRol.text.toString().lowercase(),
                    activo   = if (binding.switchActivo.isChecked) 1 else 0
                )
            )
            binding.root.showSnackbar(getString(R.string.msg_user_updated))
        }

        // El switch actualiza en tiempo real el texto del botón de acción
        binding.switchActivo.setOnCheckedChangeListener { _, checked ->
            actualizarTextoBotonEstado(checked)
        }

        // Botón de acción: activa o desactiva según el estado actual del switch
        // Si el usuario esta activo, lo desactiva; si esta inactivo, lo activa
        binding.btnEliminar.setOnClickListener {
            val activo = binding.switchActivo.isChecked
            if (activo) {
                // Desactiva el usuario: apaga el switch y llama al metodo de desactivacion
                binding.switchActivo.isChecked = false
                viewModel.desactivarUsuario(args.usuarioId)
                binding.root.showSnackbar(getString(R.string.msg_user_deactivated))
            } else {
                // Activa el usuario: enciende el switch y guarda el perfil con activo = 1
                val usuario = viewModel.usuarioActual.value ?: return@setOnClickListener
                binding.switchActivo.isChecked = true
                viewModel.actualizarPerfil(usuario.copy(activo = 1))
                binding.root.showSnackbar(getString(R.string.msg_user_activated))
            }
        }
    }

    // Actualiza el texto del boton de accion segun si el usuario esta activo o no.
    // Consume: activo (true si el usuario esta habilitado en la plataforma)
    // Devuelve: nada; modifica directamente el texto del boton btnEliminar
    private fun actualizarTextoBotonEstado(activo: Boolean) {
        binding.btnEliminar.text = if (activo) "Desactivar usuario" else "Activar usuario"
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
