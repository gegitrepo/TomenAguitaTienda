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
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.ui.auth.LoginActivity
import com.example.tomenaguita.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Fragmento del modulo vendedor que muestra la informacion del perfil del usuario autenticado.
// Carga el nombre y email desde SessionManager y la foto de perfil desde Firestore.
// Permite navegar a la pantalla de edicion de perfil y cerrar sesion.
class PerfilVendedorFragment : Fragment() {

    // Binding para acceder a las vistas del layout fragment_perfil_vendedor.xml
    private var _binding: FragmentPerfilVendedorBinding? = null
    private val binding get() = _binding!!

    // Infla el layout del fragmento y retorna la vista raiz
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilVendedorBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Carga los datos del perfil desde SessionManager y Firestore, y configura los botones de accion.
    // Se llama despues de que la vista ha sido creada.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lee nombre y email guardados localmente en la sesion
        val session = SessionManager(requireContext())
        binding.tvNombre.text = session.getUserNombre() ?: getString(R.string.demo_admin_name)
        binding.tvEmail.text = session.getUserEmail() ?: getString(R.string.demo_admin_email)

        // Si hay usuario autenticado en Firebase, consulta Firestore para obtener la URL de la foto de perfil
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            lifecycleScope.launch {
                try {
                    // Obtiene el documento del usuario en la coleccion "usuarios" de Firestore
                    val doc = FirebaseFirestore.getInstance()
                        .collection(Constants.FS_USUARIOS).document(uid).get().await()
                    // Carga la foto de perfil con Glide si la URL existe y no esta vacia
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

        // Navega a la pantalla de edicion de perfil al pulsar el boton correspondiente
        binding.btnEditarPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_perfilVendedor_to_editarPerfil)
        }

        // Cierra la sesion de Firebase y la sesion local, luego redirige al login limpiando el back stack
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            session.clearSession()
            startActivity(Intent(requireActivity(), LoginActivity::class.java).apply {
                // Limpia el back stack para que el usuario no pueda volver a la app con el boton Atras
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    // Libera la referencia al binding para evitar fugas de memoria cuando la vista es destruida
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
