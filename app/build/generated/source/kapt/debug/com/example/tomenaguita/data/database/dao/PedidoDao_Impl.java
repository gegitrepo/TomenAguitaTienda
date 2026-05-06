package com.example.tomenaguita.data.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.tomenaguita.data.database.entity.DetallePedido;
import com.example.tomenaguita.data.database.entity.Pedido;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PedidoDao_Impl implements PedidoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Pedido> __insertionAdapterOfPedido;

  private final EntityInsertionAdapter<DetallePedido> __insertionAdapterOfDetallePedido;

  private final SharedSQLiteStatement __preparedStmtOfActualizarEstado;

  public PedidoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPedido = new EntityInsertionAdapter<Pedido>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `pedidos` (`id`,`orderNumber`,`usuarioId`,`totalProductos`,`costoEnvio`,`totalPedido`,`direccionEntrega`,`latitud`,`longitud`,`estado`,`metodoPago`,`transactionId`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pedido entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getOrderNumber() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getOrderNumber());
        }
        statement.bindLong(3, entity.getUsuarioId());
        statement.bindDouble(4, entity.getTotalProductos());
        statement.bindDouble(5, entity.getCostoEnvio());
        statement.bindDouble(6, entity.getTotalPedido());
        if (entity.getDireccionEntrega() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDireccionEntrega());
        }
        if (entity.getLatitud() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getLatitud());
        }
        if (entity.getLongitud() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getLongitud());
        }
        if (entity.getEstado() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getEstado());
        }
        if (entity.getMetodoPago() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getMetodoPago());
        }
        if (entity.getTransactionId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getTransactionId());
        }
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfDetallePedido = new EntityInsertionAdapter<DetallePedido>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `detalle_pedidos` (`id`,`pedidoId`,`productoId`,`nombreProducto`,`presentacion`,`cantidad`,`precioUnitario`,`subtotal`,`vendedorId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DetallePedido entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPedidoId());
        statement.bindLong(3, entity.getProductoId());
        if (entity.getNombreProducto() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNombreProducto());
        }
        if (entity.getPresentacion() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPresentacion());
        }
        statement.bindLong(6, entity.getCantidad());
        statement.bindDouble(7, entity.getPrecioUnitario());
        statement.bindDouble(8, entity.getSubtotal());
        statement.bindLong(9, entity.getVendedorId());
      }
    };
    this.__preparedStmtOfActualizarEstado = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE pedidos SET estado = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertPedido(final Pedido pedido, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPedido.insertAndReturnId(pedido);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDetalle(final DetallePedido detalle,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDetallePedido.insert(detalle);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object actualizarEstado(final long id, final String estado, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfActualizarEstado.acquire();
        int _argIndex = 1;
        if (estado == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, estado);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfActualizarEstado.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Pedido>> getPedidosByUsuario(final long usuarioId) {
    final String _sql = "SELECT * FROM pedidos WHERE usuarioId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, usuarioId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pedidos"}, new Callable<List<Pedido>>() {
      @Override
      @NonNull
      public List<Pedido> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "orderNumber");
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfTotalProductos = CursorUtil.getColumnIndexOrThrow(_cursor, "totalProductos");
          final int _cursorIndexOfCostoEnvio = CursorUtil.getColumnIndexOrThrow(_cursor, "costoEnvio");
          final int _cursorIndexOfTotalPedido = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPedido");
          final int _cursorIndexOfDireccionEntrega = CursorUtil.getColumnIndexOrThrow(_cursor, "direccionEntrega");
          final int _cursorIndexOfLatitud = CursorUtil.getColumnIndexOrThrow(_cursor, "latitud");
          final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfMetodoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "metodoPago");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Pedido> _result = new ArrayList<Pedido>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pedido _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpOrderNumber;
            if (_cursor.isNull(_cursorIndexOfOrderNumber)) {
              _tmpOrderNumber = null;
            } else {
              _tmpOrderNumber = _cursor.getString(_cursorIndexOfOrderNumber);
            }
            final long _tmpUsuarioId;
            _tmpUsuarioId = _cursor.getLong(_cursorIndexOfUsuarioId);
            final double _tmpTotalProductos;
            _tmpTotalProductos = _cursor.getDouble(_cursorIndexOfTotalProductos);
            final double _tmpCostoEnvio;
            _tmpCostoEnvio = _cursor.getDouble(_cursorIndexOfCostoEnvio);
            final double _tmpTotalPedido;
            _tmpTotalPedido = _cursor.getDouble(_cursorIndexOfTotalPedido);
            final String _tmpDireccionEntrega;
            if (_cursor.isNull(_cursorIndexOfDireccionEntrega)) {
              _tmpDireccionEntrega = null;
            } else {
              _tmpDireccionEntrega = _cursor.getString(_cursorIndexOfDireccionEntrega);
            }
            final Double _tmpLatitud;
            if (_cursor.isNull(_cursorIndexOfLatitud)) {
              _tmpLatitud = null;
            } else {
              _tmpLatitud = _cursor.getDouble(_cursorIndexOfLatitud);
            }
            final Double _tmpLongitud;
            if (_cursor.isNull(_cursorIndexOfLongitud)) {
              _tmpLongitud = null;
            } else {
              _tmpLongitud = _cursor.getDouble(_cursorIndexOfLongitud);
            }
            final String _tmpEstado;
            if (_cursor.isNull(_cursorIndexOfEstado)) {
              _tmpEstado = null;
            } else {
              _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            }
            final String _tmpMetodoPago;
            if (_cursor.isNull(_cursorIndexOfMetodoPago)) {
              _tmpMetodoPago = null;
            } else {
              _tmpMetodoPago = _cursor.getString(_cursorIndexOfMetodoPago);
            }
            final String _tmpTransactionId;
            if (_cursor.isNull(_cursorIndexOfTransactionId)) {
              _tmpTransactionId = null;
            } else {
              _tmpTransactionId = _cursor.getString(_cursorIndexOfTransactionId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Pedido(_tmpId,_tmpOrderNumber,_tmpUsuarioId,_tmpTotalProductos,_tmpCostoEnvio,_tmpTotalPedido,_tmpDireccionEntrega,_tmpLatitud,_tmpLongitud,_tmpEstado,_tmpMetodoPago,_tmpTransactionId,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Pedido>> getAllPedidos() {
    final String _sql = "SELECT * FROM pedidos ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pedidos"}, new Callable<List<Pedido>>() {
      @Override
      @NonNull
      public List<Pedido> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "orderNumber");
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfTotalProductos = CursorUtil.getColumnIndexOrThrow(_cursor, "totalProductos");
          final int _cursorIndexOfCostoEnvio = CursorUtil.getColumnIndexOrThrow(_cursor, "costoEnvio");
          final int _cursorIndexOfTotalPedido = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPedido");
          final int _cursorIndexOfDireccionEntrega = CursorUtil.getColumnIndexOrThrow(_cursor, "direccionEntrega");
          final int _cursorIndexOfLatitud = CursorUtil.getColumnIndexOrThrow(_cursor, "latitud");
          final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfMetodoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "metodoPago");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Pedido> _result = new ArrayList<Pedido>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pedido _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpOrderNumber;
            if (_cursor.isNull(_cursorIndexOfOrderNumber)) {
              _tmpOrderNumber = null;
            } else {
              _tmpOrderNumber = _cursor.getString(_cursorIndexOfOrderNumber);
            }
            final long _tmpUsuarioId;
            _tmpUsuarioId = _cursor.getLong(_cursorIndexOfUsuarioId);
            final double _tmpTotalProductos;
            _tmpTotalProductos = _cursor.getDouble(_cursorIndexOfTotalProductos);
            final double _tmpCostoEnvio;
            _tmpCostoEnvio = _cursor.getDouble(_cursorIndexOfCostoEnvio);
            final double _tmpTotalPedido;
            _tmpTotalPedido = _cursor.getDouble(_cursorIndexOfTotalPedido);
            final String _tmpDireccionEntrega;
            if (_cursor.isNull(_cursorIndexOfDireccionEntrega)) {
              _tmpDireccionEntrega = null;
            } else {
              _tmpDireccionEntrega = _cursor.getString(_cursorIndexOfDireccionEntrega);
            }
            final Double _tmpLatitud;
            if (_cursor.isNull(_cursorIndexOfLatitud)) {
              _tmpLatitud = null;
            } else {
              _tmpLatitud = _cursor.getDouble(_cursorIndexOfLatitud);
            }
            final Double _tmpLongitud;
            if (_cursor.isNull(_cursorIndexOfLongitud)) {
              _tmpLongitud = null;
            } else {
              _tmpLongitud = _cursor.getDouble(_cursorIndexOfLongitud);
            }
            final String _tmpEstado;
            if (_cursor.isNull(_cursorIndexOfEstado)) {
              _tmpEstado = null;
            } else {
              _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            }
            final String _tmpMetodoPago;
            if (_cursor.isNull(_cursorIndexOfMetodoPago)) {
              _tmpMetodoPago = null;
            } else {
              _tmpMetodoPago = _cursor.getString(_cursorIndexOfMetodoPago);
            }
            final String _tmpTransactionId;
            if (_cursor.isNull(_cursorIndexOfTransactionId)) {
              _tmpTransactionId = null;
            } else {
              _tmpTransactionId = _cursor.getString(_cursorIndexOfTransactionId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Pedido(_tmpId,_tmpOrderNumber,_tmpUsuarioId,_tmpTotalProductos,_tmpCostoEnvio,_tmpTotalPedido,_tmpDireccionEntrega,_tmpLatitud,_tmpLongitud,_tmpEstado,_tmpMetodoPago,_tmpTransactionId,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getPedidoById(final long id, final Continuation<? super Pedido> $completion) {
    final String _sql = "SELECT * FROM pedidos WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Pedido>() {
      @Override
      @Nullable
      public Pedido call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "orderNumber");
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfTotalProductos = CursorUtil.getColumnIndexOrThrow(_cursor, "totalProductos");
          final int _cursorIndexOfCostoEnvio = CursorUtil.getColumnIndexOrThrow(_cursor, "costoEnvio");
          final int _cursorIndexOfTotalPedido = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPedido");
          final int _cursorIndexOfDireccionEntrega = CursorUtil.getColumnIndexOrThrow(_cursor, "direccionEntrega");
          final int _cursorIndexOfLatitud = CursorUtil.getColumnIndexOrThrow(_cursor, "latitud");
          final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfMetodoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "metodoPago");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Pedido _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpOrderNumber;
            if (_cursor.isNull(_cursorIndexOfOrderNumber)) {
              _tmpOrderNumber = null;
            } else {
              _tmpOrderNumber = _cursor.getString(_cursorIndexOfOrderNumber);
            }
            final long _tmpUsuarioId;
            _tmpUsuarioId = _cursor.getLong(_cursorIndexOfUsuarioId);
            final double _tmpTotalProductos;
            _tmpTotalProductos = _cursor.getDouble(_cursorIndexOfTotalProductos);
            final double _tmpCostoEnvio;
            _tmpCostoEnvio = _cursor.getDouble(_cursorIndexOfCostoEnvio);
            final double _tmpTotalPedido;
            _tmpTotalPedido = _cursor.getDouble(_cursorIndexOfTotalPedido);
            final String _tmpDireccionEntrega;
            if (_cursor.isNull(_cursorIndexOfDireccionEntrega)) {
              _tmpDireccionEntrega = null;
            } else {
              _tmpDireccionEntrega = _cursor.getString(_cursorIndexOfDireccionEntrega);
            }
            final Double _tmpLatitud;
            if (_cursor.isNull(_cursorIndexOfLatitud)) {
              _tmpLatitud = null;
            } else {
              _tmpLatitud = _cursor.getDouble(_cursorIndexOfLatitud);
            }
            final Double _tmpLongitud;
            if (_cursor.isNull(_cursorIndexOfLongitud)) {
              _tmpLongitud = null;
            } else {
              _tmpLongitud = _cursor.getDouble(_cursorIndexOfLongitud);
            }
            final String _tmpEstado;
            if (_cursor.isNull(_cursorIndexOfEstado)) {
              _tmpEstado = null;
            } else {
              _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            }
            final String _tmpMetodoPago;
            if (_cursor.isNull(_cursorIndexOfMetodoPago)) {
              _tmpMetodoPago = null;
            } else {
              _tmpMetodoPago = _cursor.getString(_cursorIndexOfMetodoPago);
            }
            final String _tmpTransactionId;
            if (_cursor.isNull(_cursorIndexOfTransactionId)) {
              _tmpTransactionId = null;
            } else {
              _tmpTransactionId = _cursor.getString(_cursorIndexOfTransactionId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Pedido(_tmpId,_tmpOrderNumber,_tmpUsuarioId,_tmpTotalProductos,_tmpCostoEnvio,_tmpTotalPedido,_tmpDireccionEntrega,_tmpLatitud,_tmpLongitud,_tmpEstado,_tmpMetodoPago,_tmpTransactionId,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDetallesByPedido(final long pedidoId,
      final Continuation<? super List<DetallePedido>> $completion) {
    final String _sql = "SELECT * FROM detalle_pedidos WHERE pedidoId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, pedidoId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DetallePedido>>() {
      @Override
      @NonNull
      public List<DetallePedido> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPedidoId = CursorUtil.getColumnIndexOrThrow(_cursor, "pedidoId");
          final int _cursorIndexOfProductoId = CursorUtil.getColumnIndexOrThrow(_cursor, "productoId");
          final int _cursorIndexOfNombreProducto = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreProducto");
          final int _cursorIndexOfPresentacion = CursorUtil.getColumnIndexOrThrow(_cursor, "presentacion");
          final int _cursorIndexOfCantidad = CursorUtil.getColumnIndexOrThrow(_cursor, "cantidad");
          final int _cursorIndexOfPrecioUnitario = CursorUtil.getColumnIndexOrThrow(_cursor, "precioUnitario");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfVendedorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendedorId");
          final List<DetallePedido> _result = new ArrayList<DetallePedido>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DetallePedido _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPedidoId;
            _tmpPedidoId = _cursor.getLong(_cursorIndexOfPedidoId);
            final long _tmpProductoId;
            _tmpProductoId = _cursor.getLong(_cursorIndexOfProductoId);
            final String _tmpNombreProducto;
            if (_cursor.isNull(_cursorIndexOfNombreProducto)) {
              _tmpNombreProducto = null;
            } else {
              _tmpNombreProducto = _cursor.getString(_cursorIndexOfNombreProducto);
            }
            final String _tmpPresentacion;
            if (_cursor.isNull(_cursorIndexOfPresentacion)) {
              _tmpPresentacion = null;
            } else {
              _tmpPresentacion = _cursor.getString(_cursorIndexOfPresentacion);
            }
            final int _tmpCantidad;
            _tmpCantidad = _cursor.getInt(_cursorIndexOfCantidad);
            final double _tmpPrecioUnitario;
            _tmpPrecioUnitario = _cursor.getDouble(_cursorIndexOfPrecioUnitario);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final long _tmpVendedorId;
            _tmpVendedorId = _cursor.getLong(_cursorIndexOfVendedorId);
            _item = new DetallePedido(_tmpId,_tmpPedidoId,_tmpProductoId,_tmpNombreProducto,_tmpPresentacion,_tmpCantidad,_tmpPrecioUnitario,_tmpSubtotal,_tmpVendedorId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
