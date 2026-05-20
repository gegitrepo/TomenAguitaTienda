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
import com.example.tomenaguita.data.repository.FirestoreCarritoRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class CarritoViewModel(app: Application) : AndroidViewModel(app) {

    private val roomRepo = CarritoRepository(AppDatabase.getInstance(app).carritoDao())
    private val firestoreRepo = FirestoreCarritoRepository()

    private val userUid get() = FirebaseAuth.getInstance().currentUser?.uid

    private val _total = MutableLiveData(0.0)
    val total: LiveData<Double> = _total

    fun getCarrito(usuarioId: Long) = roomRepo.getCarrito(usuarioId).asLiveData()

    fun getCount(usuarioId: Long) = roomRepo.getCount(usuarioId).asLiveData()

    fun agregarItem(item: CarritoItem) = viewModelScope.launch {
        val existente = roomRepo.getByUsuarioAndProducto(item.usuarioId, item.productoId)
        if (existente != null) {
            // Producto ya en carrito → acumular cantidad en lugar de duplicar
            val nuevaCantidad = existente.cantidad + item.cantidad
            roomRepo.update(existente.copy(cantidad = nuevaCantidad))
            userUid?.let { uid ->
                try { firestoreRepo.update(uid, item.productoId, nuevaCantidad) } catch (_: Exception) { }
            }
        } else {
            roomRepo.insert(item)
            userUid?.let { uid ->
                try { firestoreRepo.insert(uid, item) } catch (_: Exception) { }
            }
        }
    }

    fun actualizarCantidad(item: CarritoItem, nuevaCantidad: Int) = viewModelScope.launch {
        if (nuevaCantidad <= 0) {
            roomRepo.delete(item.id)
            userUid?.let { uid ->
                try { firestoreRepo.delete(uid, item.productoId) } catch (_: Exception) { }
            }
        } else {
            roomRepo.update(item.copy(cantidad = nuevaCantidad))
            userUid?.let { uid ->
                try { firestoreRepo.update(uid, item.productoId, nuevaCantidad) } catch (_: Exception) { }
            }
        }
    }

    fun eliminarItem(id: Long) = viewModelScope.launch {
        val item = roomRepo.getById(id)
        roomRepo.delete(id)
        userUid?.let { uid ->
            item?.let { try { firestoreRepo.delete(uid, it.productoId) } catch (_: Exception) { } }
        }
    }

    fun vaciarCarrito(usuarioId: Long) = viewModelScope.launch {
        roomRepo.vaciar(usuarioId)
        userUid?.let { uid ->
            try { firestoreRepo.vaciar(uid) } catch (_: Exception) { }
        }
    }

    fun calcularTotal(items: List<CarritoItem>) {
        _total.value = items.sumOf { it.precioAlMomento * it.cantidad }
    }
}
