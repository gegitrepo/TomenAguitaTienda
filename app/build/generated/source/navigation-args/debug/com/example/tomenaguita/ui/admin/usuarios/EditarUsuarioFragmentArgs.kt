package com.example.tomenaguita.ui.admin.usuarios

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Long
import kotlin.jvm.JvmStatic

public data class EditarUsuarioFragmentArgs(
  public val usuarioId: Long = -1L,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("usuarioId", this.usuarioId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("usuarioId", this.usuarioId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): EditarUsuarioFragmentArgs {
      bundle.setClassLoader(EditarUsuarioFragmentArgs::class.java.classLoader)
      val __usuarioId : Long
      if (bundle.containsKey("usuarioId")) {
        __usuarioId = bundle.getLong("usuarioId")
      } else {
        __usuarioId = -1L
      }
      return EditarUsuarioFragmentArgs(__usuarioId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): EditarUsuarioFragmentArgs {
      val __usuarioId : Long?
      if (savedStateHandle.contains("usuarioId")) {
        __usuarioId = savedStateHandle["usuarioId"]
        if (__usuarioId == null) {
          throw IllegalArgumentException("Argument \"usuarioId\" of type long does not support null values")
        }
      } else {
        __usuarioId = -1L
      }
      return EditarUsuarioFragmentArgs(__usuarioId)
    }
  }
}
