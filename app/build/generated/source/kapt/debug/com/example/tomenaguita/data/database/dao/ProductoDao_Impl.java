package com.example.tomenaguita.data.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.tomenaguita.data.database.entity.Producto;
import java.lang.Class;
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
public final class ProductoDao_Impl implements ProductoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Producto> __insertionAdapterOfProducto;

  private final EntityDeletionOrUpdateAdapter<Producto> __updateAdapterOfProducto;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public ProductoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProducto = new EntityInsertionAdapter<Producto>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `productos` (`id`,`nombre`,`descripcion`,`presentacion`,`precio`,`imagenUrl`,`disponible`,`stock`,`vendedorId`,`eliminado`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Producto entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombre());
        }
        if (entity.getDescripcion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescripcion());
        }
        if (entity.getPresentacion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPresentacion());
        }
        statement.bindDouble(5, entity.getPrecio());
        if (entity.getImagenUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getImagenUrl());
        }
        statement.bindLong(7, entity.getDisponible());
        statement.bindLong(8, entity.getStock());
        statement.bindLong(9, entity.getVendedorId());
        statement.bindLong(10, entity.getEliminado());
        statement.bindLong(11, entity.getCreatedAt());
        statement.bindLong(12, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfProducto = new EntityDeletionOrUpdateAdapter<Producto>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `productos` SET `id` = ?,`nombre` = ?,`descripcion` = ?,`presentacion` = ?,`precio` = ?,`imagenUrl` = ?,`disponible` = ?,`stock` = ?,`vendedorId` = ?,`eliminado` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Producto entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombre());
        }
        if (entity.getDescripcion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescripcion());
        }
        if (entity.getPresentacion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPresentacion());
        }
        statement.bindDouble(5, entity.getPrecio());
        if (entity.getImagenUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getImagenUrl());
        }
        statement.bindLong(7, entity.getDisponible());
        statement.bindLong(8, entity.getStock());
        statement.bindLong(9, entity.getVendedorId());
        statement.bindLong(10, entity.getEliminado());
        statement.bindLong(11, entity.getCreatedAt());
        statement.bindLong(12, entity.getUpdatedAt());
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE productos SET eliminado = 1, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Producto producto, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProducto.insertAndReturnId(producto);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Producto producto, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProducto.handle(producto);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object softDelete(final long id, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDelete.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
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
          __preparedStmtOfSoftDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Producto>> getAllProductosDisponibles() {
    final String _sql = "SELECT * FROM productos WHERE eliminado = 0 AND disponible = 1 ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"productos"}, new Callable<List<Producto>>() {
      @Override
      @NonNull
      public List<Producto> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfPresentacion = CursorUtil.getColumnIndexOrThrow(_cursor, "presentacion");
          final int _cursorIndexOfPrecio = CursorUtil.getColumnIndexOrThrow(_cursor, "precio");
          final int _cursorIndexOfImagenUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imagenUrl");
          final int _cursorIndexOfDisponible = CursorUtil.getColumnIndexOrThrow(_cursor, "disponible");
          final int _cursorIndexOfStock = CursorUtil.getColumnIndexOrThrow(_cursor, "stock");
          final int _cursorIndexOfVendedorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendedorId");
          final int _cursorIndexOfEliminado = CursorUtil.getColumnIndexOrThrow(_cursor, "eliminado");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Producto> _result = new ArrayList<Producto>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Producto _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpPresentacion;
            if (_cursor.isNull(_cursorIndexOfPresentacion)) {
              _tmpPresentacion = null;
            } else {
              _tmpPresentacion = _cursor.getString(_cursorIndexOfPresentacion);
            }
            final double _tmpPrecio;
            _tmpPrecio = _cursor.getDouble(_cursorIndexOfPrecio);
            final String _tmpImagenUrl;
            if (_cursor.isNull(_cursorIndexOfImagenUrl)) {
              _tmpImagenUrl = null;
            } else {
              _tmpImagenUrl = _cursor.getString(_cursorIndexOfImagenUrl);
            }
            final int _tmpDisponible;
            _tmpDisponible = _cursor.getInt(_cursorIndexOfDisponible);
            final int _tmpStock;
            _tmpStock = _cursor.getInt(_cursorIndexOfStock);
            final long _tmpVendedorId;
            _tmpVendedorId = _cursor.getLong(_cursorIndexOfVendedorId);
            final int _tmpEliminado;
            _tmpEliminado = _cursor.getInt(_cursorIndexOfEliminado);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Producto(_tmpId,_tmpNombre,_tmpDescripcion,_tmpPresentacion,_tmpPrecio,_tmpImagenUrl,_tmpDisponible,_tmpStock,_tmpVendedorId,_tmpEliminado,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Producto>> getProductosByVendedor(final long vendedorId) {
    final String _sql = "SELECT * FROM productos WHERE vendedorId = ? AND eliminado = 0 ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, vendedorId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"productos"}, new Callable<List<Producto>>() {
      @Override
      @NonNull
      public List<Producto> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfPresentacion = CursorUtil.getColumnIndexOrThrow(_cursor, "presentacion");
          final int _cursorIndexOfPrecio = CursorUtil.getColumnIndexOrThrow(_cursor, "precio");
          final int _cursorIndexOfImagenUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imagenUrl");
          final int _cursorIndexOfDisponible = CursorUtil.getColumnIndexOrThrow(_cursor, "disponible");
          final int _cursorIndexOfStock = CursorUtil.getColumnIndexOrThrow(_cursor, "stock");
          final int _cursorIndexOfVendedorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendedorId");
          final int _cursorIndexOfEliminado = CursorUtil.getColumnIndexOrThrow(_cursor, "eliminado");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Producto> _result = new ArrayList<Producto>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Producto _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpPresentacion;
            if (_cursor.isNull(_cursorIndexOfPresentacion)) {
              _tmpPresentacion = null;
            } else {
              _tmpPresentacion = _cursor.getString(_cursorIndexOfPresentacion);
            }
            final double _tmpPrecio;
            _tmpPrecio = _cursor.getDouble(_cursorIndexOfPrecio);
            final String _tmpImagenUrl;
            if (_cursor.isNull(_cursorIndexOfImagenUrl)) {
              _tmpImagenUrl = null;
            } else {
              _tmpImagenUrl = _cursor.getString(_cursorIndexOfImagenUrl);
            }
            final int _tmpDisponible;
            _tmpDisponible = _cursor.getInt(_cursorIndexOfDisponible);
            final int _tmpStock;
            _tmpStock = _cursor.getInt(_cursorIndexOfStock);
            final long _tmpVendedorId;
            _tmpVendedorId = _cursor.getLong(_cursorIndexOfVendedorId);
            final int _tmpEliminado;
            _tmpEliminado = _cursor.getInt(_cursorIndexOfEliminado);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Producto(_tmpId,_tmpNombre,_tmpDescripcion,_tmpPresentacion,_tmpPrecio,_tmpImagenUrl,_tmpDisponible,_tmpStock,_tmpVendedorId,_tmpEliminado,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getProductoById(final long id, final Continuation<? super Producto> $completion) {
    final String _sql = "SELECT * FROM productos WHERE id = ? AND eliminado = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Producto>() {
      @Override
      @Nullable
      public Producto call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfPresentacion = CursorUtil.getColumnIndexOrThrow(_cursor, "presentacion");
          final int _cursorIndexOfPrecio = CursorUtil.getColumnIndexOrThrow(_cursor, "precio");
          final int _cursorIndexOfImagenUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imagenUrl");
          final int _cursorIndexOfDisponible = CursorUtil.getColumnIndexOrThrow(_cursor, "disponible");
          final int _cursorIndexOfStock = CursorUtil.getColumnIndexOrThrow(_cursor, "stock");
          final int _cursorIndexOfVendedorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendedorId");
          final int _cursorIndexOfEliminado = CursorUtil.getColumnIndexOrThrow(_cursor, "eliminado");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Producto _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpPresentacion;
            if (_cursor.isNull(_cursorIndexOfPresentacion)) {
              _tmpPresentacion = null;
            } else {
              _tmpPresentacion = _cursor.getString(_cursorIndexOfPresentacion);
            }
            final double _tmpPrecio;
            _tmpPrecio = _cursor.getDouble(_cursorIndexOfPrecio);
            final String _tmpImagenUrl;
            if (_cursor.isNull(_cursorIndexOfImagenUrl)) {
              _tmpImagenUrl = null;
            } else {
              _tmpImagenUrl = _cursor.getString(_cursorIndexOfImagenUrl);
            }
            final int _tmpDisponible;
            _tmpDisponible = _cursor.getInt(_cursorIndexOfDisponible);
            final int _tmpStock;
            _tmpStock = _cursor.getInt(_cursorIndexOfStock);
            final long _tmpVendedorId;
            _tmpVendedorId = _cursor.getLong(_cursorIndexOfVendedorId);
            final int _tmpEliminado;
            _tmpEliminado = _cursor.getInt(_cursorIndexOfEliminado);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Producto(_tmpId,_tmpNombre,_tmpDescripcion,_tmpPresentacion,_tmpPrecio,_tmpImagenUrl,_tmpDisponible,_tmpStock,_tmpVendedorId,_tmpEliminado,_tmpCreatedAt,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
