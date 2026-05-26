package com.example.tomenaguita.ui.comprador.producto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tomenaguita.R
import com.example.tomenaguita.data.database.entity.CarritoItem
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.FragmentProductoDetalleBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.viewmodel.CarritoViewModel
import com.example.tomenaguita.viewmodel.ProductoViewModel

/*
 * Pantalla de detalle de un producto individual.
 * Recibe el ID del producto como argumento de navegación y muestra su imagen,
 * nombre, presentación, precio y descripción.
 * Permite al comprador seleccionar la cantidad deseada (entre 1 y MAX_PRODUCT_QTY)
 * y agregar el producto al carrito con esa cantidad.
 */
class ProductoDetalleFragment : Fragment() {

    private var _binding: FragmentProductoDetalleBinding? = null
    private val binding get() = _binding!!

    // Argumentos de navegación que contienen el ID del producto a mostrar
    private val args: ProductoDetalleFragmentArgs by navArgs()

    // ViewModel que provee los datos del producto seleccionado
    private val productoViewModel: ProductoViewModel by activityViewModels()

    // ViewModel que gestiona las operaciones del carrito de compras
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    // Cantidad seleccionada por el usuario; comienza en 1 y se actualiza con los botones +/-
    private var cantidad = 1

    // Referencia al producto actualmente cargado, usada para el botón de agregar al carrito
    private var productoActual: Producto? = null

    // Infla el layout del fragmento usando ViewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductoDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
     * Solicita al ViewModel que cargue el producto por su ID,
     * observa el resultado y configura los botones de cantidad.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Indicar al ViewModel qué producto mostrar usando el ID recibido por navegación
        productoViewModel.selectProducto(args.productoId)

        // Cuando el producto esté disponible, pintar todos sus datos en la vista
        productoViewModel.productoSeleccionado.observe(viewLifecycleOwner) { producto ->
            producto ?: return@observe
            productoActual = producto
            bindProducto(producto)
        }

        setupCantidad()
    }

    /*
     * Rellena todos los campos de la vista con los datos del producto recibido.
     * Carga la imagen con Glide, mostrando un placeholder si no hay URL disponible.
     * Configura el botón "Agregar al carrito" para crear un CarritoItem con la cantidad actual.
     * Consume: producto (Producto) con todos los campos del modelo.
     */
    private fun bindProducto(producto: Producto) {
        binding.tvNombre.text = producto.nombre
        binding.tvPresentacion.text = producto.presentacion
        binding.tvPrecio.text = producto.precio.toCOP()

        // Usar la descripción del producto o un texto de demostración si está vacía
        binding.tvDescripcion.text = producto.descripcion
            .ifBlank { getString(R.string.demo_product_description, producto.nombre, producto.presentacion) }
        binding.tvCantidad.text = cantidad.toString()

        // Cargar imagen desde URL; mostrar placeholder si la URL es nula o vacía
        Glide.with(this)
            .load(producto.imagenUrl?.takeIf { it.isNotEmpty() })
            .placeholder(R.drawable.bg_banner_placeholder)
            .error(R.drawable.bg_banner_placeholder)
            .centerCrop()
            .into(binding.ivProducto)

        binding.btnAgregarCarrito.setOnClickListener {
            val session = SessionManager(requireContext())
            val nuevoItem = CarritoItem(
                usuarioId = session.getUserId(),
                productoId = producto.id,
                cantidad = cantidad,
                precioAlMomento = producto.precio
            )
            carritoViewModel.agregarItem(nuevoItem)
            binding.root.showSnackbar(
                getString(R.string.msg_item_quantity_cart, producto.nombre, cantidad)
            )
        }
    }

    /*
     * Configura los botones de incremento (+) y decremento (-) de cantidad.
     * El mínimo es 1 y el máximo es Constants.MAX_PRODUCT_QTY.
     * Actualiza el TextView de cantidad tras cada pulsación.
     */
    private fun setupCantidad() {
        binding.btnMinus.setOnClickListener {
            if (cantidad > 1) { cantidad--; binding.tvCantidad.text = cantidad.toString() }
        }
        binding.btnPlus.setOnClickListener {
            if (cantidad < Constants.MAX_PRODUCT_QTY) { cantidad++; binding.tvCantidad.text = cantidad.toString() }
        }
    }

    // Libera el binding al destruir la vista para evitar fugas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
