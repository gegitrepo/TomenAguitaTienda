package com.example.tomenaguita.ui.comprador.producto

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tomenaguita.R

public class ProductoDetalleFragmentDirections private constructor() {
  public companion object {
    public fun actionDetalleToCarrito(): NavDirections =
        ActionOnlyNavDirections(R.id.action_detalle_to_carrito)
  }
}
