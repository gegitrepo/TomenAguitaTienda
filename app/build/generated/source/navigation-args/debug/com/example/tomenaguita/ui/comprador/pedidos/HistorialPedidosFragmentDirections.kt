package com.example.tomenaguita.ui.comprador.pedidos

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.tomenaguita.R
import kotlin.Int
import kotlin.Long

public class HistorialPedidosFragmentDirections private constructor() {
  private data class ActionHistorialToDetalle(
    public val pedidoId: Long = -1L,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_historial_to_detalle

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("pedidoId", this.pedidoId)
        return result
      }
  }

  public companion object {
    public fun actionHistorialToDetalle(pedidoId: Long = -1L): NavDirections =
        ActionHistorialToDetalle(pedidoId)
  }
}
