package com.example.tomenaguita.ui.comprador.pago

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.tomenaguita.R
import kotlin.Float
import kotlin.Int
import kotlin.String

public class ResumenPedidoFragmentDirections private constructor() {
  private data class ActionResumenToPago(
    public val orderNumber: String = "",
    public val totalAmount: Float = 0.0F,
    public val emailComprador: String = "",
  ) : NavDirections {
    public override val actionId: Int = R.id.action_resumen_to_pago

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("orderNumber", this.orderNumber)
        result.putFloat("totalAmount", this.totalAmount)
        result.putString("emailComprador", this.emailComprador)
        return result
      }
  }

  public companion object {
    public fun actionResumenToPago(
      orderNumber: String = "",
      totalAmount: Float = 0.0F,
      emailComprador: String = "",
    ): NavDirections = ActionResumenToPago(orderNumber, totalAmount, emailComprador)
  }
}
