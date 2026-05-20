package com.example.tomenaguita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.entity.CarritoItem
import com.example.tomenaguita.databinding.ItemCarritoBinding
import com.example.tomenaguita.utils.toCOP

data class CarritoItemUI(
    val item: CarritoItem,
    val nombreProducto: String,
    val presentacion: String,
    val imagenUrl: String? = null
)

class CarritoAdapter(
    private val onMinus: (CarritoItem) -> Unit,
    private val onPlus: (CarritoItem) -> Unit,
    private val onEliminar: (CarritoItem) -> Unit
) : ListAdapter<CarritoItemUI, CarritoAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ui: CarritoItemUI) {
            binding.tvNombre.text = ui.nombreProducto
            binding.tvPrecioUnitario.text = "${ui.item.precioAlMomento.toCOP()} c/u"
            binding.tvCantidad.text = ui.item.cantidad.toString()
            binding.tvSubtotal.text = (ui.item.precioAlMomento * ui.item.cantidad).toCOP()

            Glide.with(binding.root)
                .load(ui.imagenUrl?.takeIf { it.isNotEmpty() })
                .placeholder(R.drawable.bg_banner_placeholder)
                .error(R.drawable.bg_banner_placeholder)
                .centerCrop()
                .into(binding.ivProducto)

            binding.btnMinus.setOnClickListener { onMinus(ui.item) }
            binding.btnPlus.setOnClickListener { onPlus(ui.item) }
            binding.btnEliminar.setOnClickListener { onEliminar(ui.item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<CarritoItemUI>() {
        override fun areItemsTheSame(old: CarritoItemUI, new: CarritoItemUI) = old.item.id == new.item.id
        override fun areContentsTheSame(old: CarritoItemUI, new: CarritoItemUI) = old == new
    }
}
