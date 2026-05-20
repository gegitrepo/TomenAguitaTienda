package com.example.tomenaguita.ui.vendedor.perfil

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tomenaguita.R

public class PerfilVendedorFragmentDirections private constructor() {
  public companion object {
    public fun actionPerfilVendedorToEditarPerfil(): NavDirections =
        ActionOnlyNavDirections(R.id.action_perfilVendedor_to_editarPerfil)
  }
}
