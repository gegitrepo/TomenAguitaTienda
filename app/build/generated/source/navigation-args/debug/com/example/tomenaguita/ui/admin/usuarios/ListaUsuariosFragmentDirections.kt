package com.example.tomenaguita.ui.admin.usuarios

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tomenaguita.R
import kotlin.Int
import kotlin.Long

public class ListaUsuariosFragmentDirections private constructor() {
  private data class ActionListaToEditar(
    public val usuarioId: Long = -1L,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_lista_to_editar

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("usuarioId", this.usuarioId)
        return result
      }
  }

  public companion object {
    public fun actionListaToCrear(): NavDirections =
        ActionOnlyNavDirections(R.id.action_lista_to_crear)

    public fun actionListaToEditar(usuarioId: Long = -1L): NavDirections =
        ActionListaToEditar(usuarioId)
  }
}
