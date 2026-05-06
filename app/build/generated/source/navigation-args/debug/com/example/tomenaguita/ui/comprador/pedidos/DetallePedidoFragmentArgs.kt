package com.example.tomenaguita.ui.comprador.pedidos

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Long
import kotlin.jvm.JvmStatic

public data class DetallePedidoFragmentArgs(
  public val pedidoId: Long = -1L,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("pedidoId", this.pedidoId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("pedidoId", this.pedidoId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): DetallePedidoFragmentArgs {
      bundle.setClassLoader(DetallePedidoFragmentArgs::class.java.classLoader)
      val __pedidoId : Long
      if (bundle.containsKey("pedidoId")) {
        __pedidoId = bundle.getLong("pedidoId")
      } else {
        __pedidoId = -1L
      }
      return DetallePedidoFragmentArgs(__pedidoId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): DetallePedidoFragmentArgs {
      val __pedidoId : Long?
      if (savedStateHandle.contains("pedidoId")) {
        __pedidoId = savedStateHandle["pedidoId"]
        if (__pedidoId == null) {
          throw IllegalArgumentException("Argument \"pedidoId\" of type long does not support null values")
        }
      } else {
        __pedidoId = -1L
      }
      return DetallePedidoFragmentArgs(__pedidoId)
    }
  }
}
