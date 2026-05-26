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

// ViewModel del carrito de compras. Coordina las operaciones sobre los ítems del carrito
// entre el almacenamiento local Room y la copia en Firestore (para persistencia en la nube).
// Los errores en Firestore se ignoran silenciosamente para no bloquear la experiencia local.
class CarritoViewModel(app: Application) : AndroidViewModel(app) {

    // Repositorio local Room para operaciones CRUD sobre los ítems del carrito
    private val roomRepo = CarritoRepository(AppDatabase.getInstance(app).carritoDao())

    // Repositorio Firestore para sincronizar el carrito del usuario autenticado en la nube
    private val firestoreRepo = FirestoreCarritoRepository()

    // UID del usuario autenticado en Firebase; null si no hay sesión activa
    private val userUid get() = FirebaseAuth.getInstance().currentUser?.uid

    // Total calculado del carrito en pesos colombianos
    private val _total = MutableLiveData(0.0)
    val total: LiveData<Double> = _total

    // Devuelve un LiveData con los ítems del carrito del usuario especificado desde Room.
    // Parámetros: usuarioId es el ID local del usuario en Room.
    fun getCarrito(usuarioId: Long) = roomRepo.getCarrito(usuarioId).asLiveData()

    // Devuelve un LiveData con la cantidad de ítems distintos en el carrito del usuario.
    // Parámetros: usuarioId es el ID local del usuario en Room.
    fun getCount(usuarioId: Long) = roomRepo.getCount(usuarioId).asLiveData()

    // Agrega un producto al carrito. Si ya existe acumula la cantidad en lugar de duplicar.
    // También refleja el cambio en Firestore de forma asíncrona.
    // Parámetros: item es el CarritoItem con el productoId, usuarioId, cantidad y precio.
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

    // Actualiza la cantidad de un ítem del carrito. Si la nueva cantidad es 0 o menor,
    // elimina el ítem del carrito tanto en Room como en Firestore.
    // Parámetros: item es el CarritoItem a modificar; nuevaCantidad es el valor deseado.
    fun actualizarCantidad(item: CarritoItem, nuevaCantidad: Int) = viewModelScope.launch {
        if (nuevaCantidad <= 0) {
            // Si la cantidad llega a cero, eliminar el ítem del carrito
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

    // Elimina un ítem específico del carrito por su ID local, y lo borra también en Firestore.
    // Parámetros: id es el identificador local del CarritoItem en Room.
    fun eliminarItem(id: Long) = viewModelScope.launch {
        val item = roomRepo.getById(id)
        roomRepo.delete(id)
        userUid?.let { uid ->
            item?.let { try { firestoreRepo.delete(uid, it.productoId) } catch (_: Exception) { } }
        }
    }

    // Vacía completamente el carrito de un usuario en Room y en Firestore.
    // Se llama tras confirmar un pedido o al cerrar sesión.
    // Parámetros: usuarioId es el ID local del usuario en Room.
    fun vaciarCarrito(usuarioId: Long) = viewModelScope.launch {
        roomRepo.vaciar(usuarioId)
        userUid?.let { uid ->
            try { firestoreRepo.vaciar(uid) } catch (_: Exception) { }
        }
    }

    // Calcula el total del carrito sumando (precioAlMomento * cantidad) de cada ítem
    // y publica el resultado en _total para que la UI lo muestre actualizado.
    // Parámetros: items es la lista actual de CarritoItem del usuario.
    fun calcularTotal(items: List<CarritoItem>) {
        _total.value = items.sumOf { it.precioAlMomento * it.cantidad }
    }
}
