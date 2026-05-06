package com.example.tomenaguita.ui.comprador.producto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.databinding.FragmentProductoDetalleBinding
import com.example.tomenaguita.utils.Constants
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.utils.toCOP

class ProductoDetalleFragment : Fragment() {

    private var _binding: FragmentProductoDetalleBinding? = null
    private val binding get() = _binding!!
    private val args: ProductoDetalleFragmentArgs by navArgs()
    private var cantidad = 1
    private var producto: Producto? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductoDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProducto()
        setupCantidad()
    }

    private fun loadProducto() {
        val index = (args.productoId - 1).toInt().coerceIn(0, Constants.PRODUCTOS_DEMO.size - 1)
        val (nombre, presentacion, precio) = Constants.PRODUCTOS_DEMO[index]
        producto = Producto(
            id = args.productoId,
            nombre = nombre,
            descripcion = "Agua purificada $nombre de $presentacion. Proceso de purificación con filtración de 7 etapas. Sin aditivos, 100% natural.",
            presentacion = presentacion,
            precio = precio,
            stock = 150,
            vendedorId = 2L
        )
        binding.tvNombre.text = nombre
        binding.tvPresentacion.text = presentacion
        binding.tvPrecio.text = precio.toCOP()
        binding.tvDescripcion.text = producto!!.descripcion
        binding.tvStock.text = "150 unidades"
        binding.tvCantidad.text = cantidad.toString()

        binding.btnAgregarCarrito.setOnClickListener {
            binding.root.showSnackbar("$nombre × $cantidad agregado al carrito")
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
