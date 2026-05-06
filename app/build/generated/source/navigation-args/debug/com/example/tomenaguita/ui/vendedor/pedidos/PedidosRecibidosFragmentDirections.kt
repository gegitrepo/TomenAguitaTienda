package com.example.tomenaguita.ui.vendedor.pedidos

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.tomenaguita.R
import kotlin.Int
import kotlin.Long

public class PedidosRecibidosFragmentDirections private constructor() {
  private data class ActionPedidosToDetalle(
    public val pedidoId: Long = -1L,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_pedidos_to_detalle

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("pedidoId", this.pedidoId)
        return result
      }
  }

  public companion object {
    public fun actionPedidosToDetalle(pedidoId: Long = -1L): NavDirections =
        ActionPedidosToDetalle(pedidoId)
  }
}
