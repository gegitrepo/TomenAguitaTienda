package com.example.tomenaguita.ui.comprador.pago

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tomenaguita.R

public class ResumenPedidoFragmentDirections private constructor() {
  public companion object {
    public fun actionResumenToPago(): NavDirections =
        ActionOnlyNavDirections(R.id.action_resumen_to_pago)
  }
}
