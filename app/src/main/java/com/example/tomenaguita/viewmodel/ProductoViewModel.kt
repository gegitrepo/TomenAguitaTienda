package com.example.tomenaguita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.data.repository.ProductoRepository
import kotlinx.coroutines.launch

class ProductoViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ProductoRepository(AppDatabase.getInstance(app).productoDao())

    val productosDisponibles = repo.getAllDisponibles().asLiveData()

    private val _productoSeleccionado = MutableLiveData<Producto?>()
    val productoSeleccionado: LiveData<Producto?> = _productoSeleccionado

    private val _operationResult = MutableLiveData<Result<Unit>>()
    val operationResult: LiveData<Result<Unit>> = _operationResult

    fun getProductosByVendedor(vendedorId: Long) = repo.getByVendedor(vendedorId).asLiveData()

    fun selectProducto(id: Long) = viewModelScope.launch {
        _productoSeleccionado.value = repo.getById(id)
    }

    fun saveProducto(producto: Producto) = viewModelScope.launch {
        try {
            if (producto.id == 0L) repo.insert(producto) else repo.update(producto)
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }

    fun deleteProducto(id: Long) = viewModelScope.launch {
        try {
            repo.delete(id)
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }
}
