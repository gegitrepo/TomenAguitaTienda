package com.example.tomenaguita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.databinding.ItemDetallePedidoBinding
import com.example.tomenaguita.utils.toCOP

class DetallePedidoAdapter :
    ListAdapter<DetallePedido, DetallePedidoAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemDetallePedidoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detalle: DetallePedido) {
            binding.tvNombreProducto.text = detalle.nombreProducto
            binding.tvPresentacionCantidad.text =
                "${detalle.presentacion}  ·  ${detalle.cantidad} uds × ${detalle.precioUnitario.toCOP()}"
            binding.tvSubtotal.text = detalle.subtotal.toCOP()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetallePedidoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<DetallePedido>() {
        override fun areItemsTheSame(old: DetallePedido, new: DetallePedido) = old.id == new.id
        override fun areContentsTheSame(old: DetallePedido, new: DetallePedido) = old == new
    }
}
