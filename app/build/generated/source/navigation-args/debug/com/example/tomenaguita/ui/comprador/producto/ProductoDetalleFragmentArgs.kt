package com.example.tomenaguita.ui.comprador.producto

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Long
import kotlin.jvm.JvmStatic

public data class ProductoDetalleFragmentArgs(
  public val productoId: Long = -1L,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("productoId", this.productoId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("productoId", this.productoId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): ProductoDetalleFragmentArgs {
      bundle.setClassLoader(ProductoDetalleFragmentArgs::class.java.classLoader)
      val __productoId : Long
      if (bundle.containsKey("productoId")) {
        __productoId = bundle.getLong("productoId")
      } else {
        __productoId = -1L
      }
      return ProductoDetalleFragmentArgs(__productoId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle):
        ProductoDetalleFragmentArgs {
      val __productoId : Long?
      if (savedStateHandle.contains("productoId")) {
        __productoId = savedStateHandle["productoId"]
        if (__productoId == null) {
          throw IllegalArgumentException("Argument \"productoId\" of type long does not support null values")
        }
      } else {
        __productoId = -1L
      }
      return ProductoDetalleFragmentArgs(__productoId)
    }
  }
}
