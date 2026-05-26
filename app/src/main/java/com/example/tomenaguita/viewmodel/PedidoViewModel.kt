package com.example.tomenaguita.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.data.database.entity.Pedido
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.data.repository.FirestorePedidoRepository
import com.example.tomenaguita.data.repository.PedidoRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ViewModel de pedidos. Coordina la creación, consulta y actualización de pedidos
// entre Room (caché local) y Firestore (nube). Maneja los flujos de comprador,
// vendedor y administrador con sincronización bidireccional en tiempo real.
class PedidoViewModel(app: Application) : AndroidViewModel(app) {

    // Repositorio local Room para operaciones CRUD sobre pedidos y sus detalles
    private val roomRepo = PedidoRepository(AppDatabase.getInstance(app).pedidoDao())

    // Repositorio Firestore para persistir y sincronizar pedidos en la nube
    private val firestoreRepo = FirestorePedidoRepository()

    // Resultado de operaciones de escritura; contiene el ID del pedido creado o un error
    private val _operationResult = MutableLiveData<Result<Long>>()
    val operationResult: LiveData<Result<Long>> = _operationResult

    // Pedido actualmente seleccionado para ver su detalle
    private val _pedidoActual = MutableLiveData<Pedido?>()
    val pedidoActual: LiveData<Pedido?> = _pedidoActual

    // Pedido recién creado — permite navegar a la pasarela con el orderNumber.
    // Null = sin evento pendiente; llamar clearUltimoPedido() al consumirlo.
    private val _ultimoPedidoCreado = MutableLiveData<Pedido?>()
    val ultimoPedidoCreado: LiveData<Pedido?> = _ultimoPedidoCreado

    // Limpia el pedido recién creado para evitar que el observer lo procese más de una vez
    fun clearUltimoPedido() { _ultimoPedidoCreado.value = null }

    // Lista de líneas de detalle del pedido seleccionado
    private val _detallesPedido = MutableLiveData<List<DetallePedido>>(emptyList())
    val detallesPedido: LiveData<List<DetallePedido>> = _detallesPedido

    // Carga los detalles (ítems) de un pedido desde Room y los publica en _detallesPedido.
    // Parámetros: pedidoId es el identificador local del pedido en Room.
    fun cargarDetalles(pedidoId: Long) = viewModelScope.launch {
        _detallesPedido.value = roomRepo.getDetalles(pedidoId)
    }

    // Cambia el estado de un pedido a CANCELADO tanto en Room como en Firestore.
    // Parámetros: pedidoId es el identificador local del pedido en Room.
    fun cancelarPedido(pedidoId: Long) = viewModelScope.launch {
        roomRepo.actualizarEstado(pedidoId, EstadoPedido.CANCELADO.valor)
        try {
            val pedido = roomRepo.getById(pedidoId)
            pedido?.let { firestoreRepo.actualizarEstado(it.orderNumber, EstadoPedido.CANCELADO.valor) }
        } catch (_: Exception) { }
    }

    // Devuelve un LiveData con los pedidos de un comprador específico desde Room.
    // Parámetros: usuarioId es el ID local del comprador en Room.
    fun getPedidosByUsuario(usuarioId: Long) = roomRepo.getPedidosByUsuario(usuarioId).asLiveData()

    // Devuelve un LiveData con todos los pedidos almacenados en Room.
    fun getAllPedidos() = roomRepo.getAllPedidos().asLiveData()

    // Devuelve un LiveData con los pedidos relacionados a los productos de un vendedor.
    // Parámetros: vendedorId es el ID local del vendedor en Room.
    fun getPedidosByVendedor(vendedorId: Long) = roomRepo.getPedidosByVendedor(vendedorId).asLiveData()

    // Carga un pedido específico desde Room y lo publica en _pedidoActual.
    // Parámetros: id es el identificador local del pedido en Room.
    fun selectPedido(id: Long) = viewModelScope.launch {
        _pedidoActual.value = roomRepo.getById(id)
    }

    // Crea un nuevo pedido con sus ítems de detalle en Room y lo replica en Firestore.
    // Genera un número de pedido único con formato TA-YYYYMMDD-XXXX.
    // Parámetros: usuarioId es el ID del comprador; items es la lista de detalles del pedido;
    //             direccion es la dirección de entrega; metodoPago es el método seleccionado.
    // Publica en _ultimoPedidoCreado el pedido creado y en _operationResult su ID o un error.
    fun crearPedido(
        usuarioId: Long,
        items: List<DetallePedido>,
        direccion: String,
        metodoPago: String
    ) = viewModelScope.launch {
        try {
            val total = items.sumOf { it.subtotal }
            val orderNumber = generarNumeroPedido()
            val pedido = Pedido(
                orderNumber = orderNumber,
                usuarioId = usuarioId,
                totalProductos = total,
                totalPedido = total,
                direccionEntrega = direccion,
                estado = EstadoPedido.PENDIENTE.valor,
                metodoPago = metodoPago
            )
            val id = roomRepo.crearPedido(pedido, items)
            val pedidoConId = pedido.copy(id = id)

            val usuarioUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            try { firestoreRepo.crearPedido(pedidoConId, items, usuarioUid) } catch (_: Exception) { }

            _ultimoPedidoCreado.value = pedidoConId
            _operationResult.value = Result.success(id)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }

    // Avanza el estado de un pedido al siguiente según el flujo definido en EstadoPedido.
    // Si el estado ya es el último (ENTREGADO o CANCELADO) no realiza ningún cambio.
    // Parámetros: pedidoId es el ID local en Room; estadoActual es el estado vigente del pedido.
    fun avanzarEstado(pedidoId: Long, estadoActual: String) = viewModelScope.launch {
        val siguiente = EstadoPedido.fromString(estadoActual).siguiente()
        if (siguiente != null) {
            roomRepo.actualizarEstado(pedidoId, siguiente.valor)
            try {
                val pedido = roomRepo.getById(pedidoId)
                pedido?.let { firestoreRepo.actualizarEstado(it.orderNumber, siguiente.valor) }
                // Si el pedido que se actualizó es el que está en pantalla, refrescar su LiveData
                if (_pedidoActual.value?.id == pedidoId) {
                    _pedidoActual.value = roomRepo.getById(pedidoId)
                }
            } catch (_: Exception) { }
        }
    }

    /**
     * Versión simplificada para admin: sincroniza todos los pedidos de Firestore a Room
     * (sin detalles, suficiente para dashboard y reportes).
     */
    // Devuelve un MutableLiveData con la lista de pedidos sincronizada desde Firestore.
    // Para cada pedido inserta en Room si no existe o actualiza el estado si cambió.
    fun getAllPedidosForAdmin(): MutableLiveData<List<Pedido>> {
        val result = MutableLiveData<List<Pedido>>()
        viewModelScope.launch {
            try {
                firestoreRepo.getAllPedidos().collect { firestoreOrders ->
                    val synced = mutableListOf<Pedido>()
                    for (fp in firestoreOrders) {
                        try {
                            var roomPedido = roomRepo.getByOrderNumber(fp.orderNumber)
                            if (roomPedido == null) {
                                val id = roomRepo.crearPedido(fp, emptyList())
                                roomPedido = fp.copy(id = id)
                            } else if (roomPedido.estado != fp.estado) {
                                roomRepo.actualizarEstado(roomPedido.id, fp.estado)
                                roomPedido = roomPedido.copy(estado = fp.estado)
                            }
                            synced.add(roomPedido)
                        } catch (e: Exception) {
                            Log.e("PedidoViewModel", "sync admin: ${e.message}")
                        }
                    }
                    result.postValue(synced)
                }
            } catch (e: Exception) {
                Log.e("PedidoViewModel", "getAllPedidosForAdmin: ${e.message}")
            }
        }
        return result
    }

    /**
     * Devuelve todos los pedidos desde Firestore en tiempo real y los sincroniza a Room.
     * Para cada pedido:
     *  - Si no está en Room → lo inserta junto con sus ítems (detalles) desde Firestore.
     *  - Si ya está en Room → actualiza el estado si cambió y asegura que los detalles existan.
     * Esto permite que el vendedor vea los pedidos de los compradores aunque su Room esté vacío.
     */
    // Devuelve un MutableLiveData con la lista de pedidos enriquecidos con detalles desde Firestore.
    fun getAllPedidosForVendedor(): MutableLiveData<List<Pedido>> {
        val result = MutableLiveData<List<Pedido>>()
        viewModelScope.launch {
            try {
                firestoreRepo.getAllPedidos().collect { firestoreOrders ->
                    val enriched = mutableListOf<Pedido>()
                    for (fp in firestoreOrders) {
                        try {
                            var roomPedido = roomRepo.getByOrderNumber(fp.orderNumber)
                            if (roomPedido == null) {
                                // Pedido nuevo para este dispositivo: sincronizar con ítems
                                val detalles = firestoreRepo.getDetallesByOrderNumber(fp.orderNumber)
                                val newId = roomRepo.crearPedido(fp, detalles)
                                roomPedido = fp.copy(id = newId)
                            } else {
                                // Pedido existente: actualizar estado si cambió
                                if (roomPedido.estado != fp.estado) {
                                    roomRepo.actualizarEstado(roomPedido.id, fp.estado)
                                    roomPedido = roomPedido.copy(estado = fp.estado)
                                }
                                // Asegurar que los ítems estén en Room (pueden faltar en primera sync)
                                if (roomRepo.getDetalles(roomPedido.id).isEmpty()) {
                                    val detalles = firestoreRepo.getDetallesByOrderNumber(fp.orderNumber)
                                    roomRepo.insertarDetalles(roomPedido.id, detalles)
                                }
                            }
                            enriched.add(roomPedido)
                        } catch (e: Exception) {
                            Log.e("PedidoViewModel", "Error sincronizando ${fp.orderNumber}: ${e.message}")
                        }
                    }
                    result.postValue(enriched)
                }
            } catch (e: Exception) {
                Log.e("PedidoViewModel", "Error en getAllPedidosForVendedor: ${e.message}")
            }
        }
        return result
    }

    /** Actualiza el estado de un pedido directamente por su orderNumber (usado tras el pago). */
    // Parámetros: orderNumber es el código único del pedido (ej: TA-20240101-1234);
    //             nuevoEstado es el valor de EstadoPedido a aplicar.
    fun actualizarEstadoByOrderNumber(orderNumber: String, nuevoEstado: String) =
        viewModelScope.launch {
            try {
                val pedido = roomRepo.getByOrderNumber(orderNumber)
                pedido?.let {
                    roomRepo.actualizarEstado(it.id, nuevoEstado)
                    try { firestoreRepo.actualizarEstado(orderNumber, nuevoEstado) } catch (_: Exception) { }
                }
            } catch (_: Exception) { }
        }

    // Genera un número de pedido único con el formato TA-YYYYMMDD-XXXX.
    // Usa la fecha actual y un número aleatorio de 4 dígitos para reducir colisiones.
    // Devuelve el número de pedido como String.
    private fun generarNumeroPedido(): String {
        val fecha = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val seq = (1000..9999).random()
        return "TA-$fecha-$seq"
    }
}
