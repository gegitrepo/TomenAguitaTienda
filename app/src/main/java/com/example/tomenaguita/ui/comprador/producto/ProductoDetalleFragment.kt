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
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP
import com.example.tomenaguita.viewmodel.CarritoViewModel
import com.example.tomenaguita.viewmodel.ProductoViewModel

class ProductoDetalleFragment : Fragment() {

    private var _binding: FragmentProductoDetalleBinding? = null
    private val binding get() = _binding!!
    private val args: ProductoDetalleFragmentArgs by navArgs()
    private val productoViewModel: ProductoViewModel by activityViewModels()
    private val carritoViewModel: CarritoViewModel by activityViewModels()

    private var cantidad = 1
    private var productoActual: Producto? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductoDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productoViewModel.selectProducto(args.productoId)

        productoViewModel.productoSeleccionado.observe(viewLifecycleOwner) { producto ->
            producto ?: return@observe
            productoActual = producto
            bindProducto(producto)
        }

        setupCantidad()
    }

    private fun bindProducto(producto: Producto) {
        binding.tvNombre.text = producto.nombre
        binding.tvPresentacion.text = producto.presentacion
        binding.tvPrecio.text = producto.precio.toCOP()
        binding.tvDescripcion.text = producto.descripcion
            .ifBlank { getString(R.string.demo_product_description, producto.nombre, producto.presentacion) }
        binding.tvStock.text = getString(R.string.label_units_format, producto.stock)
        binding.tvCantidad.text = cantidad.toString()

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

    private fun setupCantidad() {
        binding.btnMinus.setOnClickListener {
            if (cantidad > 1) { cantidad--; binding.tvCantidad.text = cantidad.toString() }
        }
        binding.btnPlus.setOnClickListener {
            if (cantidad < 99) { cantidad++; binding.tvCantidad.text = cantidad.toString() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
