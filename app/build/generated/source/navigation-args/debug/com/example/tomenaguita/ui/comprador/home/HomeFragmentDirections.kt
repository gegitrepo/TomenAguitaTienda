package com.example.tomenaguita.ui.comprador.home

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.tomenaguita.R
import kotlin.Int
import kotlin.Long

public class HomeFragmentDirections private constructor() {
  private data class ActionHomeToDetalle(
    public val productoId: Long = -1L,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_home_to_detalle

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("productoId", this.productoId)
        return result
      }
  }

  public companion object {
    public fun actionHomeToDetalle(productoId: Long = -1L): NavDirections =
        ActionHomeToDetalle(productoId)
  }
}
