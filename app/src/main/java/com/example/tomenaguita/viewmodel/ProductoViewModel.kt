package com.example.tomenaguita.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.database.entity.CarritoItem
import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.data.repository.FirestoreProductoRepository
import com.example.tomenaguita.data.repository.ProductoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ViewModel de productos. Gestiona el catálogo de agua disponible para la venta.
// Mantiene sincronizados en tiempo real el caché local de Room con Firestore,
// y expone LiveData con el listado de productos disponibles para la UI.
class ProductoViewModel(app: Application) : AndroidViewModel(app) {

    // Repositorio local Room para operaciones CRUD sobre la tabla de productos
    private val roomRepo = ProductoRepository(AppDatabase.getInstance(app).productoDao())

    // Repositorio Firestore para sincronizar el catálogo en la nube
    private val firestoreRepo = FirestoreProductoRepository()

    // Lista reactiva de productos con disponible=1 almacenados en Room
    val productosDisponibles = roomRepo.getAllDisponibles().asLiveData()

    // Producto actualmente seleccionado para ver detalle o editar
    private val _productoSeleccionado = MutableLiveData<Producto?>()
    val productoSeleccionado: LiveData<Producto?> = _productoSeleccionado

    // Null = sin evento pendiente. El observer debe llamar clearOperationResult() al procesarlo.
    private val _operationResult = MutableLiveData<Result<Unit>?>()
    val operationResult: LiveData<Result<Unit>?> = _operationResult

    // Limpia el resultado de operación para evitar que el observer lo procese más de una vez
    fun clearOperationResult() { _operationResult.value = null }

    // Al crear el ViewModel, siembra productos de demostración si Firestore está vacío
    // e inicia la sincronización continua de Firestore hacia Room en el hilo IO.
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

    // Devuelve un LiveData con los productos creados por un vendedor específico desde Room.
    // Parámetros: vendedorId es el ID local del vendedor en Room.
    // Devuelve un Flow convertido a LiveData con la lista de sus productos.
    fun getProductosByVendedor(vendedorId: Long) = roomRepo.getByVendedor(vendedorId).asLiveData()

    /**
     * Devuelve en tiempo real los productos del vendedor consultando Firestore por su UID.
     * Para cada producto, busca el registro en Room por firestoreDocId para obtener el Room ID
     * correcto (necesario para la navegación de edición). Si no está en Room aún, lo inserta
     * con el vendedorRoomId indicado para que el listado y la edición funcionen correctamente.
     * Esto resuelve el caso de Room vacío (reinstalación / clear data).
     */
    // Parámetros: vendedorUid es el UID de Firebase Auth del vendedor;
    //             vendedorRoomId es su ID local en Room para asociar nuevos productos.
    // Devuelve un MutableLiveData con la lista de productos enriquecidos con el ID de Room.
    fun getProductosByVendedorFirestore(
        vendedorUid: String,
        vendedorRoomId: Long
    ): MutableLiveData<List<Producto>> {
        val result = MutableLiveData<List<Producto>>()
        viewModelScope.launch {
            firestoreRepo.getByVendedor(vendedorUid).collect { firestoreList ->
                val enriched = firestoreList.map { fp ->
                    val docId = fp.firestoreDocId ?: return@map fp
                    var roomProducto = roomRepo.getByFirestoreDocId(docId)
                    if (roomProducto == null) {
                        val newId = roomRepo.insert(fp.copy(vendedorId = vendedorRoomId))
                        roomProducto = fp.copy(id = newId, vendedorId = vendedorRoomId)
                    }
                    roomProducto
                }
                result.postValue(enriched)
            }
        }
        return result
    }

    // Carga un producto por su ID local desde Room y lo publica en _productoSeleccionado.
    // Parámetros: id es el identificador local del producto en Room.
    fun selectProducto(id: Long) = viewModelScope.launch {
        _productoSeleccionado.value = roomRepo.getById(id)
    }

    // Guarda un producto en Firestore y en Room. Si el producto es nuevo (id == 0)
    // lo inserta en ambos; si ya existe, actualiza los campos modificables.
    // Parámetros: producto es el objeto con los datos a guardar.
    // Publica en _operationResult el resultado de la operación.
    fun saveProducto(producto: Producto) = viewModelScope.launch {
        try {
            val vendedorUid = FirebaseAuth.getInstance().currentUser?.uid ?: "demo"
            if (producto.id == 0L) {
                // Producto nuevo: insertar en Firestore y luego en Room con el docId obtenido
                val firestoreDocId = firestoreRepo.insert(producto, vendedorUid)
                roomRepo.insert(producto.copy(firestoreDocId = firestoreDocId))
            } else {
                // Producto existente: actualizar campos en Firestore y Room
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

    /**
     * Resta del inventario las cantidades compradas en una orden exitosa.
     * Actualiza Room y Firestore. Corre en viewModelScope — sobrevive la navegación.
     */
    // Parámetros: cartItems es la lista de ítems comprados con sus cantidades;
    //             productos es la lista de entidades Producto para obtener el docId y stock actual.
    fun reducirStock(cartItems: List<CarritoItem>, productos: List<Producto>) =
        viewModelScope.launch {
            try {
                val productoMap = productos.associateBy { it.id }
                cartItems.forEach { item ->
                    val producto = productoMap[item.productoId] ?: return@forEach
                    val docId = producto.firestoreDocId ?: return@forEach

                    // Firestore: decremento atómico (seguro ante accesos concurrentes)
                    firestoreRepo.update(
                        docId,
                        mapOf("stock" to FieldValue.increment(-item.cantidad.toLong()))
                    )

                    // Room: refleja el nuevo stock localmente
                    val nuevoStock = maxOf(0, producto.stock - item.cantidad)
                    roomRepo.update(
                        producto.copy(stock = nuevoStock, updatedAt = System.currentTimeMillis())
                    )
                }
            } catch (e: Exception) {
                Log.e("ProductoViewModel", "Error reduciendo stock: ${e.message}")
            }
        }

    // Elimina un producto: realiza borrado lógico en Firestore (softDelete) y lo borra de Room.
    // Parámetros: id es el identificador local del producto en Room.
    // Publica en _operationResult el resultado de la operación.
    fun deleteProducto(id: Long) = viewModelScope.launch {
        try {
            // Marcar como no disponible en Firestore (borrado lógico)
            roomRepo.getById(id)?.firestoreDocId?.let { firestoreRepo.softDelete(it) }
            // Eliminar el registro de Room
            roomRepo.delete(id)
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }
}
