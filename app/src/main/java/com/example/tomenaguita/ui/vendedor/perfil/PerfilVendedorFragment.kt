package com.example.tomenaguita.ui.vendedor.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.FragmentPerfilVendedorBinding
import com.example.tomenaguita.ui.auth.LoginActivity
import com.example.tomenaguita.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PerfilVendedorFragment : Fragment() {

    private var _binding: FragmentPerfilVendedorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilVendedorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireContext())
        binding.tvNombre.text = session.getUserNombre() ?: getString(R.string.demo_admin_name)
        binding.tvEmail.text = session.getUserEmail() ?: getString(R.string.demo_admin_email)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            lifecycleScope.launch {
                try {
                    val doc = FirebaseFirestore.getInstance()
                        .collection("usuarios").document(uid).get().await()
                    doc.getString("fotoUrl")?.takeIf { it.isNotEmpty() }?.let { url ->
                        Glide.with(this@PerfilVendedorFragment)
                            .load(url)
                            .circleCrop()
                            .placeholder(R.drawable.ic_profile)
                            .into(binding.ivAvatar)
                    }
                } catch (_: Exception) { }
            }
        }

        binding.btnEditarPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_perfilVendedor_to_editarPerfil)
        }
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            session.clearSession()
            startActivity(Intent(requireActivity(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
