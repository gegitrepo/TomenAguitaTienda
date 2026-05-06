package com.example.tomenaguita.ui.comprador.carrito

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tomenaguita.R

public class CarritoFragmentDirections private constructor() {
  public companion object {
    public fun actionCarritoToResumen(): NavDirections =
        ActionOnlyNavDirections(R.id.action_carrito_to_resumen)
  }
}
