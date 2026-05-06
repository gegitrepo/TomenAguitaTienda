package com.example.tomenaguita.ui.admin.usuarios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomenaguita.data.database.entity.Usuario
import com.example.tomenaguita.databinding.FragmentListaUsuariosBinding
import com.example.tomenaguita.ui.adapter.UsuarioAdapter

class ListaUsuariosFragment : Fragment() {

    private var _binding: FragmentListaUsuariosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UsuarioAdapter

    private val demoUsuarios = listOf(
        Usuario(1L, "Administrador", "admin@tomenaguita.com", "hash", "3001111111", "administrador"),
        Usuario(2L, "Vendedor Demo", "vendedor@tomenaguita.com", "hash", "3002222222", "vendedor"),
        Usuario(3L, "Carlos Comprador", "comprador@tomenaguita.com", "hash", "3003333333", "comprador"),
        Usuario(4L, "María González", "maria@gmail.com", "hash", "3004444444", "comprador"),
        Usuario(5L, "Juan Pérez", "juan@gmail.com", "hash", "3005555555", "comprador"),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListaUsuariosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UsuarioAdapter { usuario ->
            val action = ListaUsuariosFragmentDirections.actionListaToEditar(usuario.id)
            findNavController().navigate(action)
        }
        binding.rvUsuarios.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsuarios.adapter = adapter
        adapter.submitList(demoUsuarios)

        binding.fabCrearUsuario.setOnClickListener {
            findNavController().navigate(ListaUsuariosFragmentDirections.actionListaToCrear())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
