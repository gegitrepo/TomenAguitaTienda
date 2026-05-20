package com.example.tomenaguita.viewmodel

import android.app.Application
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

class PedidoViewModel(app: Application) : AndroidViewModel(app) {

    private val roomRepo = PedidoRepository(AppDatabase.getInstance(app).pedidoDao())
    private val firestoreRepo = FirestorePedidoRepository()

    private val _operationResult = MutableLiveData<Result<Long>>()
    val operationResult: LiveData<Result<Long>> = _operationResult

    private val _pedidoActual = MutableLiveData<Pedido?>()
    val pedidoActual: LiveData<Pedido?> = _pedidoActual

    // Pedido recién creado — permite navegar a la pasarela con el orderNumber.
    // Null = sin evento pendiente; llamar clearUltimoPedido() al consumirlo.
    private val _ultimoPedidoCreado = MutableLiveData<Pedido?>()
    val ultimoPedidoCreado: LiveData<Pedido?> = _ultimoPedidoCreado
    fun clearUltimoPedido() { _ultimoPedidoCreado.value = null }

    private val _detallesPedido = MutableLiveData<List<DetallePedido>>(emptyList())
    val detallesPedido: LiveData<List<DetallePedido>> = _detallesPedido

    fun cargarDetalles(pedidoId: Long) = viewModelScope.launch {
        _detallesPedido.value = roomRepo.getDetalles(pedidoId)
    }

    fun cancelarPedido(pedidoId: Long) = viewModelScope.launch {
        roomRepo.actualizarEstado(pedidoId, EstadoPedido.CANCELADO.valor)
        try {
            val pedido = roomRepo.getById(pedidoId)
            pedido?.let { firestoreRepo.actualizarEstado(it.orderNumber, EstadoPedido.CANCELADO.valor) }
        } catch (_: Exception) { }
    }

    fun getPedidosByUsuario(usuarioId: Long) = roomRepo.getPedidosByUsuario(usuarioId).asLiveData()

    fun getAllPedidos() = roomRepo.getAllPedidos().asLiveData()

    fun getPedidosByVendedor(vendedorId: Long) = roomRepo.getPedidosByVendedor(vendedorId).asLiveData()

    fun selectPedido(id: Long) = viewModelScope.launch {
        _pedidoActual.value = roomRepo.getById(id)
    }

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

    fun avanzarEstado(pedidoId: Long, estadoActual: String) = viewModelScope.launch {
        val siguiente = EstadoPedido.fromString(estadoActual).siguiente()
        if (siguiente != null) {
            roomRepo.actualizarEstado(pedidoId, siguiente.valor)
            try {
                val pedido = roomRepo.getById(pedidoId)
                pedido?.let { firestoreRepo.actualizarEstado(it.orderNumber, siguiente.valor) }
                if (_pedidoActual.value?.id == pedidoId) {
                    _pedidoActual.value = roomRepo.getById(pedidoId)
                }
            } catch (_: Exception) { }
        }
    }

    /** Actualiza el estado de un pedido directamente por su orderNumber (usado tras el pago). */
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

    private fun generarNumeroPedido(): String {
        val fecha = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val seq = (1000..9999).random()
        return "TA-$fecha-$seq"
    }
}
