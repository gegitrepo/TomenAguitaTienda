package com.example.tomenaguita.ui.comprador.pago

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tomenaguita.R

public class PasarelaPagoFragmentDirections private constructor() {
  public companion object {
    public fun actionPagoToHistorial(): NavDirections =
        ActionOnlyNavDirections(R.id.action_pago_to_historial)
  }
}
