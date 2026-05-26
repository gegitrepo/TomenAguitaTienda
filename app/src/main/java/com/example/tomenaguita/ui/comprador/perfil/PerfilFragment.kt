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
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.ui.auth.LoginActivity
import com.bumptech.glide.Glide
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.showSnackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/*
 * Pantalla de perfil del comprador.
 * Muestra los datos del usuario: nombre, correo, teléfono, dirección y foto de perfil.
 * Los datos de nombre y correo se obtienen de la sesión local (SessionManager),
 * mientras que teléfono, dirección y foto de perfil se cargan desde Firestore en tiempo real.
 * Desde aquí el comprador puede navegar a editar su perfil o cerrar sesión.
 */
class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    // Infla el layout del fragmento usando ViewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Rellena los campos de la vista con los datos de sesión y, si hay un usuario autenticado,
     * actualiza teléfono, dirección y foto cargándolos desde Firestore.
     * Asigna los listeners de los botones de editar perfil y cerrar sesión.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireContext())

        // Mostrar nombre y correo desde la caché local de sesión
        binding.tvNombre.text = session.getUserNombre() ?: getString(R.string.demo_comprador_name)
        binding.tvEmail.text = session.getUserEmail() ?: getString(R.string.demo_comprador_email)

        // Valores de demostración mientras se cargan los datos reales desde Firestore
        binding.tvTelefono.text = getString(R.string.demo_phone)
        binding.tvDireccion.text = getString(R.string.demo_address)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            // Cargar datos actualizados desde Firestore y reemplazar los valores de demostración
            lifecycleScope.launch {
                try {
                    val doc = FirebaseFirestore.getInstance()
                        .collection(Constants.FS_USUARIOS).document(uid).get().await()

                    // Actualizar teléfono solo si existe y no está vacío
                    doc.getString("telefono")?.takeIf { it.isNotEmpty() }?.let {
                        binding.tvTelefono.text = it
                    }
                    // Actualizar dirección solo si existe y no está vacía
                    doc.getString("direccion")?.takeIf { it.isNotEmpty() }?.let {
                        binding.tvDireccion.text = it
                    }
                    // Cargar foto de perfil circular si existe una URL válida
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

        // Navegar a la pantalla de edición de perfil
        binding.btnEditarPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_perfil_to_editar)
        }

        binding.btnLogout.setOnClickListener {
            val userId = session.getUserId()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            /*
             * Limpiar el carrito del usuario activo antes de cerrar sesión,
             * para que la siguiente cuenta que inicie sesión en este dispositivo
             * comience con el carrito vacío.
             */
            lifecycleScope.launch {
                try {
                    AppDatabase.getInstance(requireContext())
                        .carritoDao().vaciarCarrito(userId)
                } catch (_: Exception) { }

                // Limpiar carrito en Firestore (best-effort, sin bloquear el logout)
                userUid?.let { uid ->
                    try { FirestoreCarritoRepository().vaciar(uid) } catch (_: Exception) { }
                }

                // Cerrar sesión en Firebase y limpiar la sesión local
                FirebaseAuth.getInstance().signOut()
                session.clearSession()

                // Navegar a LoginActivity limpiando el back stack para evitar regresar
                startActivity(Intent(requireActivity(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
        }
    }

    // Libera el binding al destruir la vista para evitar fugas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
