package com.example.tomenaguita.ui.comprador.catalogo

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.tomenaguita.R
import kotlin.Int
import kotlin.Long

public class CatalogoFragmentDirections private constructor() {
  private data class ActionCatalogoToDetalle(
    public val productoId: Long = -1L,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_catalogo_to_detalle

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("productoId", this.productoId)
        return result
      }
  }

  public companion object {
    public fun actionCatalogoToDetalle(productoId: Long = -1L): NavDirections =
        ActionCatalogoToDetalle(productoId)
  }
}
