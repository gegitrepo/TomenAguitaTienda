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

// Modelo de presentación que combina un CarritoItem de Room con los datos del producto
// necesarios para mostrar en la vista del carrito (nombre, presentación e imagen).
data class CarritoItemUI(
    val item: CarritoItem,
    val nombreProducto: String,
    // Texto descriptivo del envase (ej: "Botella 600ml")
    val presentacion: String,
    // URL de la imagen en Firebase Storage; puede ser nula si el producto no tiene imagen
    val imagenUrl: String? = null
)

// Adaptador del RecyclerView para el carrito de compras.
// Recibe tres lambdas para disminuir cantidad, aumentar cantidad y eliminar el ítem.
class CarritoAdapter(
    private val onMinus: (CarritoItem) -> Unit,
    private val onPlus: (CarritoItem) -> Unit,
    private val onEliminar: (CarritoItem) -> Unit
) : ListAdapter<CarritoItemUI, CarritoAdapter.ViewHolder>(DiffCallback()) {

    // ViewHolder que enlaza un CarritoItemUI con las vistas del layout item_carrito.
    inner class ViewHolder(private val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Asigna los datos del CarritoItemUI a las vistas y registra los listeners de los botones.
        // Parámetros: ui es el modelo de presentación con el ítem y los datos del producto.
        fun bind(ui: CarritoItemUI) {
            binding.tvNombre.text = ui.nombreProducto
            binding.tvPrecioUnitario.text = "${ui.item.precioAlMomento.toCOP()} c/u"
            binding.tvCantidad.text = ui.item.cantidad.toString()
            binding.tvSubtotal.text = (ui.item.precioAlMomento * ui.item.cantidad).toCOP()

            // Cargar imagen del producto con Glide; si no hay URL o falla, muestra el placeholder
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

    // Infla el layout item_carrito y crea un ViewHolder para cada celda del RecyclerView.
    // Parámetros: parent es el ViewGroup contenedor; viewType no se usa (un solo tipo de vista).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Vincula los datos del ítem en la posición indicada al ViewHolder correspondiente.
    // Parámetros: holder es el ViewHolder a poblar; position es el índice en la lista.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Callback de diferencias para que ListAdapter actualice solo los ítems que cambiaron.
    class DiffCallback : DiffUtil.ItemCallback<CarritoItemUI>() {
        // Compara por el ID del CarritoItem para identificar el mismo ítem
        override fun areItemsTheSame(old: CarritoItemUI, new: CarritoItemUI) = old.item.id == new.item.id
        // Compara todo el contenido del CarritoItemUI para detectar cambios en cantidad o precio
        override fun areContentsTheSame(old: CarritoItemUI, new: CarritoItemUI) = old == new
    }
}
