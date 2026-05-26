package com.example.tomenaguita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.databinding.ItemDetallePedidoBinding
import com.example.tomenaguita.utils.toCOP

// Adaptador del RecyclerView para los ítems de detalle de un pedido.
// Muestra cada línea del pedido con el nombre del producto, presentación,
// cantidad, precio unitario y subtotal. Solo lectura; no tiene acciones de edición.
class DetallePedidoAdapter :
    ListAdapter<DetallePedido, DetallePedidoAdapter.ViewHolder>(DiffCallback()) {

    // ViewHolder que enlaza un DetallePedido con las vistas del layout item_detalle_pedido.
    inner class ViewHolder(private val binding: ItemDetallePedidoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Asigna los datos del DetallePedido a las vistas de la fila.
        // Parámetros: detalle es la línea de pedido con nombre, presentación, cantidad y precios.
        fun bind(detalle: DetallePedido) {
            binding.tvNombreProducto.text = detalle.nombreProducto
            // Combinar presentación, cantidad y precio unitario en una sola línea descriptiva
            binding.tvPresentacionCantidad.text =
                "${detalle.presentacion}  ·  ${detalle.cantidad} uds × ${detalle.precioUnitario.toCOP()}"
            binding.tvSubtotal.text = detalle.subtotal.toCOP()
        }
    }

    // Infla el layout item_detalle_pedido y crea un ViewHolder para cada línea del pedido.
    // Parámetros: parent es el ViewGroup contenedor; viewType no se usa (un solo tipo de vista).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetallePedidoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    // Vincula los datos del detalle en la posición indicada al ViewHolder correspondiente.
    // Parámetros: holder es el ViewHolder a poblar; position es el índice en la lista.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Callback de diferencias para que ListAdapter actualice solo los ítems que cambiaron.
    class DiffCallback : DiffUtil.ItemCallback<DetallePedido>() {
        // Compara por el ID local del detalle para identificar el mismo registro
        override fun areItemsTheSame(old: DetallePedido, new: DetallePedido) = old.id == new.id
        // Compara todo el contenido para detectar si algún campo del detalle cambió
        override fun areContentsTheSame(old: DetallePedido, new: DetallePedido) = old == new
    }
}
