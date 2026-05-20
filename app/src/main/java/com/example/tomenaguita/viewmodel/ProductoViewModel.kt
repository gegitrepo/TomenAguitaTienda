package com.example.tomenaguita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.data.repository.FirestoreProductoRepository
import com.example.tomenaguita.data.repository.ProductoRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoViewModel(app: Application) : AndroidViewModel(app) {

    private val roomRepo = ProductoRepository(AppDatabase.getInstance(app).productoDao())
    private val firestoreRepo = FirestoreProductoRepository()

    val productosDisponibles = roomRepo.getAllDisponibles().asLiveData()

    private val _productoSeleccionado = MutableLiveData<Producto?>()
    val productoSeleccionado: LiveData<Producto?> = _productoSeleccionado

    // Null = sin evento pendiente. El observer debe llamar clearOperationResult() al procesarlo.
    private val _operationResult = MutableLiveData<Result<Unit>?>()
    val operationResult: LiveData<Result<Unit>?> = _operationResult

    fun clearOperationResult() { _operationResult.value = null }

    init {
        viewModelScope.launch {
            try { firestoreRepo.seedProductosIfEmpty() } catch (_: Exception) { }
        }
        // Sincronizar Firestore → Room en tiempo real (IO para no bloquear Main)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreRepo.getAllDisponibles().collect { firestoreProducts ->
                    firestoreProducts.forEach { fp ->
                        val docId = fp.firestoreDocId ?: return@forEach
                        val existing = roomRepo.getByFirestoreDocId(docId)
                        if (existing == null) {
                            roomRepo.insert(fp)
                        } else if (existing.precio != fp.precio || existing.stock != fp.stock) {
                            roomRepo.update(existing.copy(
                                nombre = fp.nombre,
                                descripcion = fp.descripcion,
                                precio = fp.precio,
                                stock = fp.stock,
                                disponible = fp.disponible,
                                imagenUrl = fp.imagenUrl,
                                updatedAt = System.currentTimeMillis()
                            ))
                        }
                    }
                }
            } catch (_: Exception) { }
        }
    }

    fun getProductosByVendedor(vendedorId: Long) = roomRepo.getByVendedor(vendedorId).asLiveData()

    fun selectProducto(id: Long) = viewModelScope.launch {
        _productoSeleccionado.value = roomRepo.getById(id)
    }

    fun saveProducto(producto: Producto) = viewModelScope.launch {
        try {
            val vendedorUid = FirebaseAuth.getInstance().currentUser?.uid ?: "demo"
            if (producto.id == 0L) {
                val firestoreDocId = firestoreRepo.insert(producto, vendedorUid)
                roomRepo.insert(producto.copy(firestoreDocId = firestoreDocId))
            } else {
                producto.firestoreDocId?.let { docId ->
                    firestoreRepo.update(docId, mapOf(
                        "nombre" to producto.nombre,
                        "descripcion" to producto.descripcion,
                        "precio" to producto.precio,
                        "stock" to producto.stock,
                        "disponible" to (producto.disponible == 1),
                        "imagenUrl" to (producto.imagenUrl ?: "")
                    ))
                }
                roomRepo.update(producto.copy(updatedAt = System.currentTimeMillis()))
            }
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }

    fun deleteProducto(id: Long) = viewModelScope.launch {
        try {
            roomRepo.getById(id)?.firestoreDocId?.let { firestoreRepo.softDelete(it) }
            roomRepo.delete(id)
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }
}
