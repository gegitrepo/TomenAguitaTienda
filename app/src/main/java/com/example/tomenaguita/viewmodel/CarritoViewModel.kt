package com.example.tomenaguita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.database.entity.CarritoItem
import com.example.tomenaguita.data.repository.CarritoRepository
import kotlinx.coroutines.launch

class CarritoViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = CarritoRepository(AppDatabase.getInstance(app).carritoDao())

    private val _total = MutableLiveData(0.0)
    val total: LiveData<Double> = _total

    fun getCarrito(usuarioId: Long) = repo.getCarrito(usuarioId).asLiveData()

    fun getCount(usuarioId: Long) = repo.getCount(usuarioId).asLiveData()

    fun agregarItem(item: CarritoItem) = viewModelScope.launch {
        repo.insert(item)
    }

    fun actualizarCantidad(item: CarritoItem, nuevaCantidad: Int) = viewModelScope.launch {
        if (nuevaCantidad <= 0) repo.delete(item.id)
        else repo.update(item.copy(cantidad = nuevaCantidad))
    }

    fun eliminarItem(id: Long) = viewModelScope.launch {
        repo.delete(id)
    }

    fun vaciarCarrito(usuarioId: Long) = viewModelScope.launch {
        repo.vaciar(usuarioId)
    }

    fun calcularTotal(items: List<CarritoItem>) {
        _total.value = items.sumOf { it.precioAlMomento * it.cantidad }
    }
}
