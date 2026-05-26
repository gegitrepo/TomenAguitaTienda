package com.example.tomenaguita.ui.admin.usuarios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Usuario
import com.example.tomenaguita.databinding.FragmentListaUsuariosBinding
import com.example.tomenaguita.ui.adapter.UsuarioAdapter
import com.example.tomenaguita.utils.gone
import com.example.tomenaguita.utils.visible
import com.example.tomenaguita.viewmodel.UsuarioViewModel

// Fragmento del modulo administrador que muestra la lista completa de usuarios registrados.
// Permite buscar usuarios por nombre, email o rol mediante un campo de busqueda en tiempo real.
// Al pulsar un usuario navega a EditarUsuarioFragment para ver y modificar sus datos.
// Incluye un boton flotante (FAB) para navegar a CrearUsuarioFragment y registrar nuevos usuarios.
class ListaUsuariosFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_lista_usuarios.xml
    private var _binding: FragmentListaUsuariosBinding? = null
    private val binding get() = _binding!!

    // Adaptador del RecyclerView que muestra las tarjetas de cada usuario
    private lateinit var adapter: UsuarioAdapter

    // ViewModel de usuarios compartido a nivel de actividad; provee la lista sincronizada desde Firestore
    private val viewModel: UsuarioViewModel by activityViewModels()

    // Copia local de todos los usuarios; sirve como fuente para el filtro de busqueda
    private var allUsuarios: List<Usuario> = emptyList()

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListaUsuariosBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el RecyclerView, el campo de busqueda y el FAB de creacion de usuario.
    // Observa el LiveData de todos los usuarios y aplica el filtro actual al recibir cambios.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura el adaptador con un callback que navega al detalle del usuario al pulsarlo
        adapter = UsuarioAdapter { usuario ->
            val action = ListaUsuariosFragmentDirections.actionListaToEditar(usuario.id)
            findNavController().navigate(action)
        }
        binding.rvUsuarios.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsuarios.adapter = adapter

        // Observa todos los usuarios y re-aplica la busqueda cada vez que la lista cambia
        viewModel.todosLosUsuarios.observe(viewLifecycleOwner) { usuarios ->
            allUsuarios = usuarios
            applySearch(binding.etSearch.text?.toString() ?: "")
        }

        // Filtra la lista en tiempo real a medida que el usuario escribe en el campo de busqueda
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            applySearch(text?.toString() ?: "")
        }

        // Navega a la pantalla de creacion de usuario al pulsar el boton flotante
        binding.fabCrearUsuario.setOnClickListener {
            findNavController().navigate(ListaUsuariosFragmentDirections.actionListaToCrear())
        }
    }

    // Filtra la lista de usuarios por nombre, email o rol segun el texto de busqueda.
    // Si la consulta esta vacia muestra todos los usuarios.
    // Muestra u oculta el texto de lista vacia segun el resultado del filtro.
    // Consume: query (texto de busqueda ingresado por el usuario)
    private fun applySearch(query: String) {
        val lista = if (query.isBlank()) {
            allUsuarios
        } else {
            allUsuarios.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                it.email.contains(query, ignoreCase = true) ||
                it.rol.contains(query, ignoreCase = true)
            }
        }
        adapter.submitList(lista)
        // Muestra un mensaje de lista vacia si no hay resultados para la busqueda actual
        if (lista.isEmpty()) {
            binding.tvEmpty.visible()
            binding.rvUsuarios.gone()
        } else {
            binding.tvEmpty.gone()
            binding.rvUsuarios.visible()
        }
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
