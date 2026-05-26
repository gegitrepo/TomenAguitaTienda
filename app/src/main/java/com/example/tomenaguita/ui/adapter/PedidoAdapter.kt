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

// Adaptador del RecyclerView para el historial de pedidos.
// Muestra número de pedido, fecha, total y el estado con un chip de color.
// Usado tanto en la vista de comprador como en la de vendedor y administrador.
class PedidoAdapter(
    private val onClick: (Pedido) -> Unit
) : ListAdapter<Pedido, PedidoAdapter.ViewHolder>(DiffCallback()) {

    // ViewHolder que enlaza un Pedido con las vistas del layout item_pedido.
    inner class ViewHolder(private val binding: ItemPedidoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Asigna los datos del Pedido a las vistas y registra el listener de clic.
        // Parámetros: pedido es la entidad de Room con todos los datos del pedido.
        fun bind(pedido: Pedido) {
            binding.tvNumeroPedido.text = pedido.orderNumber
            binding.tvTotal.text = pedido.totalPedido.toCOP()
            // Formatear la fecha de creación almacenada como timestamp en milisegundos
            binding.tvFecha.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date(pedido.createdAt))
            val estado = EstadoPedido.fromString(pedido.estado)
            binding.chipEstado.text = estado.valor.replaceFirstChar { it.uppercase() }
            // Asignar color del chip según el estado actual del pedido
            binding.chipEstado.setChipBackgroundColorResource(colorForEstado(estado))
            binding.root.setOnClickListener { onClick(pedido) }
        }

        // Devuelve el recurso de color correspondiente a cada estado del pedido.
        // Parámetros: estado es el valor de EstadoPedido del pedido.
        // Devuelve el ID del recurso de color de Android para el chip.
        private fun colorForEstado(estado: EstadoPedido): Int = when (estado) {
            EstadoPedido.PENDIENTE -> android.R.color.holo_orange_light
            EstadoPedido.PAGADO -> android.R.color.holo_blue_light
            EstadoPedido.ENVIADO -> android.R.color.holo_purple
            EstadoPedido.ENTREGADO -> android.R.color.holo_green_light
            EstadoPedido.CANCELADO -> android.R.color.holo_red_light
        }
    }

    // Infla el layout item_pedido y crea un ViewHolder para cada celda del RecyclerView.
    // Parámetros: parent es el ViewGroup contenedor; viewType no se usa (un solo tipo de vista).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Vincula los datos del pedido en la posición indicada al ViewHolder correspondiente.
    // Parámetros: holder es el ViewHolder a poblar; position es el índice en la lista.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Callback de diferencias para que ListAdapter actualice solo los ítems que cambiaron.
    class DiffCallback : DiffUtil.ItemCallback<Pedido>() {
        // Compara por el ID local del pedido para identificar el mismo registro
        override fun areItemsTheSame(old: Pedido, new: Pedido) = old.id == new.id
        // Compara todo el contenido para detectar cambios de estado u otros campos
        override fun areContentsTheSame(old: Pedido, new: Pedido) = old == new
    }
}
