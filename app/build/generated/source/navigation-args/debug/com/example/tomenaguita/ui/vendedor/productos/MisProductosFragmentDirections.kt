package com.example.tomenaguita.ui.vendedor.productos

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.tomenaguita.R
import kotlin.Int
import kotlin.Long

public class MisProductosFragmentDirections private constructor() {
  private data class ActionMisProductosToCrear(
    public val productoId: Long = -1L,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_mis_productos_to_crear

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("productoId", this.productoId)
        return result
      }
  }

  private data class ActionMisProductosToEditar(
    public val productoId: Long = -1L,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_mis_productos_to_editar

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("productoId", this.productoId)
        return result
      }
  }

  public companion object {
    public fun actionMisProductosToCrear(productoId: Long = -1L): NavDirections =
        ActionMisProductosToCrear(productoId)

    public fun actionMisProductosToEditar(productoId: Long = -1L): NavDirections =
        ActionMisProductosToEditar(productoId)
  }
}
