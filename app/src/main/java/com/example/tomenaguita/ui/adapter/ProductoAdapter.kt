package com.example.tomenaguita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.ItemProductoBinding
import com.example.tomenaguita.utils.toCOP

class ProductoAdapter(
    private val onProductoClick: (Producto) -> Unit,
    private val onAgregarClick: (Producto) -> Unit
) : ListAdapter<Producto, ProductoAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: Producto) {
            binding.tvNombre.text = producto.nombre
            binding.tvPresentacion.text = producto.presentacion
            binding.tvPrecio.text = producto.precio.toCOP()
            binding.root.setOnClickListener { onProductoClick(producto) }
            binding.btnAgregar.setOnClickListener { onAgregarClick(producto) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(old: Producto, new: Producto) = old.id == new.id
        override fun areContentsTheSame(old: Producto, new: Producto) = old == new
    }
}
