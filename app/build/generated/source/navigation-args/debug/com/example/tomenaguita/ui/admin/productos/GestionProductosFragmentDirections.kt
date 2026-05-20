package com.example.tomenaguita.ui.admin.productos

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.tomenaguita.R
import kotlin.Int
import kotlin.Long

public class GestionProductosFragmentDirections private constructor() {
  private data class ActionGestionToEditarProducto(
    public val productoId: Long = -1L,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_gestion_to_editar_producto

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("productoId", this.productoId)
        return result
      }
  }

  public companion object {
    public fun actionGestionToEditarProducto(productoId: Long = -1L): NavDirections =
        ActionGestionToEditarProducto(productoId)
  }
}
