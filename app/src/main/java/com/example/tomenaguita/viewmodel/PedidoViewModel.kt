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
import com.example.tomenaguita.data.repository.PedidoRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PedidoViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = PedidoRepository(AppDatabase.getInstance(app).pedidoDao())

    private val _operationResult = MutableLiveData<Result<Long>>()
    val operationResult: LiveData<Result<Long>> = _operationResult

    fun getPedidosByUsuario(usuarioId: Long) = repo.getPedidosByUsuario(usuarioId).asLiveData()

    fun getAllPedidos() = repo.getAllPedidos().asLiveData()

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
            val id = repo.crearPedido(pedido, items)
            _operationResult.value = Result.success(id)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }

    fun avanzarEstado(pedidoId: Long, estadoActual: String) = viewModelScope.launch {
        val siguiente = EstadoPedido.fromString(estadoActual).siguiente()
        if (siguiente != null) repo.actualizarEstado(pedidoId, siguiente.valor)
    }

    private fun generarNumeroPedido(): String {
        val fecha = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val seq = (1000..9999).random()
        return "TA-$fecha-$seq"
    }
}
