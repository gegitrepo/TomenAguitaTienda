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
import com.example.tomenaguita.data.database.entity.CarritoItem;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class CarritoDao_Impl implements CarritoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CarritoItem> __insertionAdapterOfCarritoItem;

  private final EntityDeletionOrUpdateAdapter<CarritoItem> __updateAdapterOfCarritoItem;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfVaciarCarrito;

  public CarritoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCarritoItem = new EntityInsertionAdapter<CarritoItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `carrito` (`id`,`usuarioId`,`productoId`,`cantidad`,`precioAlMomento`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CarritoItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUsuarioId());
        statement.bindLong(3, entity.getProductoId());
        statement.bindLong(4, entity.getCantidad());
        statement.bindDouble(5, entity.getPrecioAlMomento());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfCarritoItem = new EntityDeletionOrUpdateAdapter<CarritoItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `carrito` SET `id` = ?,`usuarioId` = ?,`productoId` = ?,`cantidad` = ?,`precioAlMomento` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CarritoItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUsuarioId());
        statement.bindLong(3, entity.getProductoId());
        statement.bindLong(4, entity.getCantidad());
        statement.bindDouble(5, entity.getPrecioAlMomento());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM carrito WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfVaciarCarrito = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM carrito WHERE usuarioId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CarritoItem item, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCarritoItem.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CarritoItem item, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCarritoItem.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object vaciarCarrito(final long usuarioId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfVaciarCarrito.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, usuarioId);
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
          __preparedStmtOfVaciarCarrito.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CarritoItem>> getCarritoByUsuario(final long usuarioId) {
    final String _sql = "SELECT * FROM carrito WHERE usuarioId = ? ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, usuarioId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"carrito"}, new Callable<List<CarritoItem>>() {
      @Override
      @NonNull
      public List<CarritoItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfProductoId = CursorUtil.getColumnIndexOrThrow(_cursor, "productoId");
          final int _cursorIndexOfCantidad = CursorUtil.getColumnIndexOrThrow(_cursor, "cantidad");
          final int _cursorIndexOfPrecioAlMomento = CursorUtil.getColumnIndexOrThrow(_cursor, "precioAlMomento");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<CarritoItem> _result = new ArrayList<CarritoItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CarritoItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUsuarioId;
            _tmpUsuarioId = _cursor.getLong(_cursorIndexOfUsuarioId);
            final long _tmpProductoId;
            _tmpProductoId = _cursor.getLong(_cursorIndexOfProductoId);
            final int _tmpCantidad;
            _tmpCantidad = _cursor.getInt(_cursorIndexOfCantidad);
            final double _tmpPrecioAlMomento;
            _tmpPrecioAlMomento = _cursor.getDouble(_cursorIndexOfPrecioAlMomento);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new CarritoItem(_tmpId,_tmpUsuarioId,_tmpProductoId,_tmpCantidad,_tmpPrecioAlMomento,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<Integer> getCarritoCount(final long usuarioId) {
    final String _sql = "SELECT COUNT(*) FROM carrito WHERE usuarioId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, usuarioId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"carrito"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
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
  public Object getById(final long id, final Continuation<? super CarritoItem> $completion) {
    final String _sql = "SELECT * FROM carrito WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CarritoItem>() {
      @Override
      @Nullable
      public CarritoItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfProductoId = CursorUtil.getColumnIndexOrThrow(_cursor, "productoId");
          final int _cursorIndexOfCantidad = CursorUtil.getColumnIndexOrThrow(_cursor, "cantidad");
          final int _cursorIndexOfPrecioAlMomento = CursorUtil.getColumnIndexOrThrow(_cursor, "precioAlMomento");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final CarritoItem _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUsuarioId;
            _tmpUsuarioId = _cursor.getLong(_cursorIndexOfUsuarioId);
            final long _tmpProductoId;
            _tmpProductoId = _cursor.getLong(_cursorIndexOfProductoId);
            final int _tmpCantidad;
            _tmpCantidad = _cursor.getInt(_cursorIndexOfCantidad);
            final double _tmpPrecioAlMomento;
            _tmpPrecioAlMomento = _cursor.getDouble(_cursorIndexOfPrecioAlMomento);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new CarritoItem(_tmpId,_tmpUsuarioId,_tmpProductoId,_tmpCantidad,_tmpPrecioAlMomento,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getByUsuarioAndProducto(final long usuarioId, final long productoId,
      final Continuation<? super CarritoItem> $completion) {
    final String _sql = "SELECT * FROM carrito WHERE usuarioId = ? AND productoId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, usuarioId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, productoId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CarritoItem>() {
      @Override
      @Nullable
      public CarritoItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfProductoId = CursorUtil.getColumnIndexOrThrow(_cursor, "productoId");
          final int _cursorIndexOfCantidad = CursorUtil.getColumnIndexOrThrow(_cursor, "cantidad");
          final int _cursorIndexOfPrecioAlMomento = CursorUtil.getColumnIndexOrThrow(_cursor, "precioAlMomento");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final CarritoItem _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUsuarioId;
            _tmpUsuarioId = _cursor.getLong(_cursorIndexOfUsuarioId);
            final long _tmpProductoId;
            _tmpProductoId = _cursor.getLong(_cursorIndexOfProductoId);
            final int _tmpCantidad;
            _tmpCantidad = _cursor.getInt(_cursorIndexOfCantidad);
            final double _tmpPrecioAlMomento;
            _tmpPrecioAlMomento = _cursor.getDouble(_cursorIndexOfPrecioAlMomento);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new CarritoItem(_tmpId,_tmpUsuarioId,_tmpProductoId,_tmpCantidad,_tmpPrecioAlMomento,_tmpCreatedAt,_tmpUpdatedAt);
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
