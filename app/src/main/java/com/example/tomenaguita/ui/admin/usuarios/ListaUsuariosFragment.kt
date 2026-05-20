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

class ListaUsuariosFragment : Fragment() {

    private var _binding: FragmentListaUsuariosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UsuarioAdapter
    private val viewModel: UsuarioViewModel by activityViewModels()

    private var allUsuarios: List<Usuario> = emptyList()

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

        viewModel.todosLosUsuarios.observe(viewLifecycleOwner) { usuarios ->
            allUsuarios = usuarios
            applySearch(binding.etSearch.text?.toString() ?: "")
        }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            applySearch(text?.toString() ?: "")
        }

        binding.fabCrearUsuario.setOnClickListener {
            findNavController().navigate(ListaUsuariosFragmentDirections.actionListaToCrear())
        }
    }

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
        if (lista.isEmpty()) {
            binding.tvEmpty.visible()
            binding.rvUsuarios.gone()
        } else {
            binding.tvEmpty.gone()
            binding.rvUsuarios.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
