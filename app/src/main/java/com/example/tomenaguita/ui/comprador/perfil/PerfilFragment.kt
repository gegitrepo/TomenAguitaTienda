package com.example.tomenaguita.ui.comprador.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.repository.FirestoreCarritoRepository
import com.example.tomenaguita.databinding.FragmentPerfilBinding
import com.example.tomenaguita.ui.auth.LoginActivity
import com.bumptech.glide.Glide
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.showSnackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireContext())
        binding.tvNombre.text = session.getUserNombre() ?: getString(R.string.demo_comprador_name)
        binding.tvEmail.text = session.getUserEmail() ?: getString(R.string.demo_comprador_email)
        binding.tvTelefono.text = getString(R.string.demo_phone)
        binding.tvDireccion.text = getString(R.string.demo_address)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            lifecycleScope.launch {
                try {
                    val doc = FirebaseFirestore.getInstance()
                        .collection("usuarios").document(uid).get().await()
                    doc.getString("telefono")?.takeIf { it.isNotEmpty() }?.let {
                        binding.tvTelefono.text = it
                    }
                    doc.getString("direccion")?.takeIf { it.isNotEmpty() }?.let {
                        binding.tvDireccion.text = it
                    }
                    doc.getString("fotoUrl")?.takeIf { it.isNotEmpty() }?.let { url ->
                        Glide.with(this@PerfilFragment)
                            .load(url)
                            .circleCrop()
                            .placeholder(com.example.tomenaguita.R.drawable.ic_profile)
                            .into(binding.ivAvatar)
                    }
                } catch (_: Exception) { }
            }
        }

        binding.btnEditarPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_perfil_to_editar)
        }

        binding.btnLogout.setOnClickListener {
            val userId = session.getUserId()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            // Primero limpiar el carrito del usuario activo antes de cerrar sesión,
            // para que la siguiente cuenta que inicie sesión en este dispositivo
            // comience con el carrito vacío.
            lifecycleScope.launch {
                try {
                    AppDatabase.getInstance(requireContext())
                        .carritoDao().vaciarCarrito(userId)
                } catch (_: Exception) { }

                // Limpiar carrito en Firestore (best-effort)
                userUid?.let { uid ->
                    try { FirestoreCarritoRepository().vaciar(uid) } catch (_: Exception) { }
                }

                FirebaseAuth.getInstance().signOut()
                session.clearSession()

                startActivity(Intent(requireActivity(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
