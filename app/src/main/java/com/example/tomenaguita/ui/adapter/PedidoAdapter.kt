package com.example.tomenaguita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tomenaguita.data.database.entity.Pedido
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.databinding.ItemPedidoBinding
import com.example.tomenaguita.utils.toCOP
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PedidoAdapter(
    private val onClick: (Pedido) -> Unit
) : ListAdapter<Pedido, PedidoAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemPedidoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pedido: Pedido) {
            binding.tvNumeroPedido.text = pedido.orderNumber
            binding.tvTotal.text = pedido.totalPedido.toCOP()
            binding.tvFecha.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date(pedido.createdAt))
            val estado = EstadoPedido.fromString(pedido.estado)
            binding.chipEstado.text = estado.valor.replaceFirstChar { it.uppercase() }
            binding.chipEstado.setChipBackgroundColorResource(colorForEstado(estado))
            binding.root.setOnClickListener { onClick(pedido) }
        }

        private fun colorForEstado(estado: EstadoPedido): Int = when (estado) {
            EstadoPedido.PENDIENTE -> android.R.color.holo_orange_light
            EstadoPedido.PAGADO -> android.R.color.holo_blue_light
            EstadoPedido.ENVIADO -> android.R.color.holo_purple
            EstadoPedido.ENTREGADO -> android.R.color.holo_green_light
            EstadoPedido.CANCELADO -> android.R.color.holo_red_light
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Pedido>() {
        override fun areItemsTheSame(old: Pedido, new: Pedido) = old.id == new.id
        override fun areContentsTheSame(old: Pedido, new: Pedido) = old == new
    }
}
