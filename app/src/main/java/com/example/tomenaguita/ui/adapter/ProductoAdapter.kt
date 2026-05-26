package com.example.tomenaguita.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ItemProductoBinding
import com.example.tomenaguita.utils.toCOP

// Adaptador del RecyclerView para el catálogo de productos de agua.
// Recibe dos lambdas para manejar el clic en el ítem y el clic en el botón "Agregar al carrito".
// El parámetro showAgregarButton permite ocultar el botón en pantallas de solo lectura (vendedor/admin).
class ProductoAdapter(
    private val onProductoClick: (ProductoItem) -> Unit,
    private val onAgregarClick: (ProductoItem) -> Unit,
    // Controla si se muestra el botón de agregar al carrito; por defecto está visible
    private val showAgregarButton: Boolean = true
) : ListAdapter<ProductoItem, ProductoAdapter.ViewHolder>(DiffCallback()) {

    // ViewHolder que enlaza un ProductoItem con las vistas del layout item_producto.
    inner class ViewHolder(private val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Asigna los datos del ProductoItem a las vistas y registra los listeners de clic.
        // Parámetros: item es el ProductoItem con nombre, presentación, precio e imagen.
        fun bind(item: ProductoItem) {
            binding.tvNombre.text = item.nombre
            binding.tvPresentacion.text = item.presentacion
            binding.tvPrecio.text = item.precio.toCOP()

            // Cargar imagen desde URL con Glide; si está vacía o falla, muestra el placeholder
            Glide.with(binding.root)
                .load(item.imagenUrl?.takeIf { it.isNotEmpty() })
                .placeholder(R.drawable.bg_banner_placeholder)
                .error(R.drawable.bg_banner_placeholder)
                .centerCrop()
                .into(binding.ivProducto)

            // Mostrar u ocultar el botón "Agregar" según el contexto de la pantalla
            binding.btnAgregar.visibility = if (showAgregarButton) View.VISIBLE else View.GONE
            binding.root.setOnClickListener { onProductoClick(item) }
            binding.btnAgregar.setOnClickListener { onAgregarClick(item) }
        }
    }

    // Infla el layout item_producto y crea un ViewHolder para cada celda del RecyclerView.
    // Parámetros: parent es el ViewGroup contenedor; viewType no se usa (un solo tipo de vista).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Vincula los datos del ítem en la posición indicada al ViewHolder correspondiente.
    // Parámetros: holder es el ViewHolder a poblar; position es el índice en la lista.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Callback de diferencias para que ListAdapter actualice solo los ítems que cambiaron.
    class DiffCallback : DiffUtil.ItemCallback<ProductoItem>() {
        // Compara por ID para detectar si es el mismo ítem de la lista
        override fun areItemsTheSame(old: ProductoItem, new: ProductoItem) = old.id == new.id
        // Compara todo el contenido para detectar si algún campo cambió
        override fun areContentsTheSame(old: ProductoItem, new: ProductoItem) = old == new
    }
}
