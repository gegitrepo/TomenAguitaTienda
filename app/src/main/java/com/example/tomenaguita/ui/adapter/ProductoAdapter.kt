package com.example.tomenaguita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tomenaguita.databinding.ItemProductoBinding
import com.example.tomenaguita.utils.toCOP

class ProductoAdapter(
    private val onProductoClick: (ProductoItem) -> Unit,
    private val onAgregarClick: (ProductoItem) -> Unit
) : ListAdapter<ProductoItem, ProductoAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductoItem) {
            binding.tvNombre.text = item.nombre
            binding.tvPresentacion.text = item.presentacion
            binding.tvPrecio.text = item.precio.toCOP()
            binding.root.setOnClickListener { onProductoClick(item) }
            binding.btnAgregar.setOnClickListener { onAgregarClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductoItem>() {
        override fun areItemsTheSame(old: ProductoItem, new: ProductoItem) = old.id == new.id
        override fun areContentsTheSame(old: ProductoItem, new: ProductoItem) = old == new
    }
}
