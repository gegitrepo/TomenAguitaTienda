package com.example.tomenaguita.ui.comprador.pago

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Float
import kotlin.String
import kotlin.jvm.JvmStatic

public data class PasarelaPagoFragmentArgs(
  public val orderNumber: String = "",
  public val totalAmount: Float = 0.0F,
  public val emailComprador: String = "",
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("orderNumber", this.orderNumber)
    result.putFloat("totalAmount", this.totalAmount)
    result.putString("emailComprador", this.emailComprador)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("orderNumber", this.orderNumber)
    result.set("totalAmount", this.totalAmount)
    result.set("emailComprador", this.emailComprador)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): PasarelaPagoFragmentArgs {
      bundle.setClassLoader(PasarelaPagoFragmentArgs::class.java.classLoader)
      val __orderNumber : String?
      if (bundle.containsKey("orderNumber")) {
        __orderNumber = bundle.getString("orderNumber")
        if (__orderNumber == null) {
          throw IllegalArgumentException("Argument \"orderNumber\" is marked as non-null but was passed a null value.")
        }
      } else {
        __orderNumber = ""
      }
      val __totalAmount : Float
      if (bundle.containsKey("totalAmount")) {
        __totalAmount = bundle.getFloat("totalAmount")
      } else {
        __totalAmount = 0.0F
      }
      val __emailComprador : String?
      if (bundle.containsKey("emailComprador")) {
        __emailComprador = bundle.getString("emailComprador")
        if (__emailComprador == null) {
          throw IllegalArgumentException("Argument \"emailComprador\" is marked as non-null but was passed a null value.")
        }
      } else {
        __emailComprador = ""
      }
      return PasarelaPagoFragmentArgs(__orderNumber, __totalAmount, __emailComprador)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): PasarelaPagoFragmentArgs {
      val __orderNumber : String?
      if (savedStateHandle.contains("orderNumber")) {
        __orderNumber = savedStateHandle["orderNumber"]
        if (__orderNumber == null) {
          throw IllegalArgumentException("Argument \"orderNumber\" is marked as non-null but was passed a null value")
        }
      } else {
        __orderNumber = ""
      }
      val __totalAmount : Float?
      if (savedStateHandle.contains("totalAmount")) {
        __totalAmount = savedStateHandle["totalAmount"]
        if (__totalAmount == null) {
          throw IllegalArgumentException("Argument \"totalAmount\" of type float does not support null values")
        }
      } else {
        __totalAmount = 0.0F
      }
      val __emailComprador : String?
      if (savedStateHandle.contains("emailComprador")) {
        __emailComprador = savedStateHandle["emailComprador"]
        if (__emailComprador == null) {
          throw IllegalArgumentException("Argument \"emailComprador\" is marked as non-null but was passed a null value")
        }
      } else {
        __emailComprador = ""
      }
      return PasarelaPagoFragmentArgs(__orderNumber, __totalAmount, __emailComprador)
    }
  }
}
