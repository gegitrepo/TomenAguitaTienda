package com.example.tomenaguita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tomenaguita.data.database.entity.Usuario
import com.example.tomenaguita.data.model.Rol
import com.example.tomenaguita.databinding.ItemUsuarioBinding

// Adaptador del RecyclerView para la lista de usuarios del panel de administración.
// Muestra nombre, email, rol y estado (activo/inactivo) de cada usuario con chips de color.
class UsuarioAdapter(
    private val onClick: (Usuario) -> Unit
) : ListAdapter<Usuario, UsuarioAdapter.ViewHolder>(DiffCallback()) {

    // ViewHolder que enlaza un Usuario con las vistas del layout item_usuario.
    inner class ViewHolder(private val binding: ItemUsuarioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Asigna los datos del Usuario a las vistas y registra el listener de clic.
        // Parámetros: usuario es la entidad de Room con nombre, email, rol y estado.
        fun bind(usuario: Usuario) {
            binding.tvNombre.text = usuario.nombre
            binding.tvEmail.text = usuario.email
            // Convertir el String del rol al enum Rol y capitalizar para mostrarlo al usuario
            binding.chipRol.text = Rol.fromString(usuario.rol).valor.replaceFirstChar { it.uppercase() }
            // Room almacena activo como Int (1 = activo, 0 = inactivo); mostrar texto legible
            binding.chipActivo.text = if (usuario.activo == 1) "Activo" else "Inactivo"
            // Color verde para activos, rojo para inactivos
            binding.chipActivo.setChipBackgroundColorResource(
                if (usuario.activo == 1) android.R.color.holo_green_light
                else android.R.color.holo_red_light
            )
            binding.root.setOnClickListener { onClick(usuario) }
        }
    }

    // Infla el layout item_usuario y crea un ViewHolder para cada celda del RecyclerView.
    // Parámetros: parent es el ViewGroup contenedor; viewType no se usa (un solo tipo de vista).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Vincula los datos del usuario en la posición indicada al ViewHolder correspondiente.
    // Parámetros: holder es el ViewHolder a poblar; position es el índice en la lista.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Callback de diferencias para que ListAdapter actualice solo los ítems que cambiaron.
    class DiffCallback : DiffUtil.ItemCallback<Usuario>() {
        // Compara por el ID local del usuario para identificar el mismo registro
        override fun areItemsTheSame(old: Usuario, new: Usuario) = old.id == new.id
        // Compara todo el contenido para detectar cambios de nombre, rol o estado
        override fun areContentsTheSame(old: Usuario, new: Usuario) = old == new
    }
}
