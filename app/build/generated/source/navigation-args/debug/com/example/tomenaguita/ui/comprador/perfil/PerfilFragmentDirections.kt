package com.example.tomenaguita.ui.comprador.perfil

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tomenaguita.R

public class PerfilFragmentDirections private constructor() {
  public companion object {
    public fun actionPerfilToEditar(): NavDirections =
        ActionOnlyNavDirections(R.id.action_perfil_to_editar)
  }
}
