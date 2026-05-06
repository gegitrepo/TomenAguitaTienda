package com.example.tomenaguita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tomenaguita.data.database.entity.Usuario
import com.example.tomenaguita.data.model.Rol
import com.example.tomenaguita.databinding.ItemUsuarioBinding

class UsuarioAdapter(
    private val onClick: (Usuario) -> Unit
) : ListAdapter<Usuario, UsuarioAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemUsuarioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(usuario: Usuario) {
            binding.tvNombre.text = usuario.nombre
            binding.tvEmail.text = usuario.email
            binding.chipRol.text = Rol.fromString(usuario.rol).valor.replaceFirstChar { it.uppercase() }
            binding.chipActivo.text = if (usuario.activo == 1) "Activo" else "Inactivo"
            binding.chipActivo.setChipBackgroundColorResource(
                if (usuario.activo == 1) android.R.color.holo_green_light
                else android.R.color.holo_red_light
            )
            binding.root.setOnClickListener { onClick(usuario) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Usuario>() {
        override fun areItemsTheSame(old: Usuario, new: Usuario) = old.id == new.id
        override fun areContentsTheSame(old: Usuario, new: Usuario) = old == new
    }
}
