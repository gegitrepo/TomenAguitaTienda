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
import com.example.tomenaguita.data.database.entity.Usuario;
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
public final class UsuarioDao_Impl implements UsuarioDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Usuario> __insertionAdapterOfUsuario;

  private final EntityDeletionOrUpdateAdapter<Usuario> __updateAdapterOfUsuario;

  private final SharedSQLiteStatement __preparedStmtOfDesactivar;

  public UsuarioDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUsuario = new EntityInsertionAdapter<Usuario>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `usuarios` (`id`,`nombre`,`email`,`password`,`telefono`,`rol`,`activo`,`fotoUrl`,`direccion`,`latitud`,`longitud`,`biometricEnabled`,`firestoreDocId`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Usuario entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombre());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPassword());
        }
        if (entity.getTelefono() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelefono());
        }
        if (entity.getRol() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getRol());
        }
        statement.bindLong(7, entity.getActivo());
        if (entity.getFotoUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFotoUrl());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDireccion());
        }
        if (entity.getLatitud() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLatitud());
        }
        if (entity.getLongitud() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getLongitud());
        }
        statement.bindLong(12, entity.getBiometricEnabled());
        if (entity.getFirestoreDocId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getFirestoreDocId());
        }
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfUsuario = new EntityDeletionOrUpdateAdapter<Usuario>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `usuarios` SET `id` = ?,`nombre` = ?,`email` = ?,`password` = ?,`telefono` = ?,`rol` = ?,`activo` = ?,`fotoUrl` = ?,`direccion` = ?,`latitud` = ?,`longitud` = ?,`biometricEnabled` = ?,`firestoreDocId` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Usuario entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombre());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPassword());
        }
        if (entity.getTelefono() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelefono());
        }
        if (entity.getRol() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getRol());
        }
        statement.bindLong(7, entity.getActivo());
        if (entity.getFotoUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFotoUrl());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDireccion());
        }
        if (entity.getLatitud() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLatitud());
        }
        if (entity.getLongitud() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getLongitud());
        }
        statement.bindLong(12, entity.getBiometricEnabled());
        if (entity.getFirestoreDocId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getFirestoreDocId());
        }
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getUpdatedAt());
        statement.bindLong(16, entity.getId());
      }
    };
    this.__preparedStmtOfDesactivar = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE usuarios SET activo = 0, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Usuario usuario, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfUsuario.insertAndReturnId(usuario);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Usuario usuario, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUsuario.handle(usuario);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object desactivar(final long id, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDesactivar.acquire();
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
          __preparedStmtOfDesactivar.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Usuario>> getAllUsuarios() {
    final String _sql = "SELECT * FROM usuarios ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"usuarios"}, new Callable<List<Usuario>>() {
      @Override
      @NonNull
      public List<Usuario> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfRol = CursorUtil.getColumnIndexOrThrow(_cursor, "rol");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUrl");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfLatitud = CursorUtil.getColumnIndexOrThrow(_cursor, "latitud");
          final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
          final int _cursorIndexOfBiometricEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "biometricEnabled");
          final int _cursorIndexOfFirestoreDocId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreDocId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Usuario> _result = new ArrayList<Usuario>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Usuario _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpRol;
            if (_cursor.isNull(_cursorIndexOfRol)) {
              _tmpRol = null;
            } else {
              _tmpRol = _cursor.getString(_cursorIndexOfRol);
            }
            final int _tmpActivo;
            _tmpActivo = _cursor.getInt(_cursorIndexOfActivo);
            final String _tmpFotoUrl;
            if (_cursor.isNull(_cursorIndexOfFotoUrl)) {
              _tmpFotoUrl = null;
            } else {
              _tmpFotoUrl = _cursor.getString(_cursorIndexOfFotoUrl);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
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
            final int _tmpBiometricEnabled;
            _tmpBiometricEnabled = _cursor.getInt(_cursorIndexOfBiometricEnabled);
            final String _tmpFirestoreDocId;
            if (_cursor.isNull(_cursorIndexOfFirestoreDocId)) {
              _tmpFirestoreDocId = null;
            } else {
              _tmpFirestoreDocId = _cursor.getString(_cursorIndexOfFirestoreDocId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Usuario(_tmpId,_tmpNombre,_tmpEmail,_tmpPassword,_tmpTelefono,_tmpRol,_tmpActivo,_tmpFotoUrl,_tmpDireccion,_tmpLatitud,_tmpLongitud,_tmpBiometricEnabled,_tmpFirestoreDocId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getUsuarioById(final long id, final Continuation<? super Usuario> $completion) {
    final String _sql = "SELECT * FROM usuarios WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Usuario>() {
      @Override
      @Nullable
      public Usuario call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfRol = CursorUtil.getColumnIndexOrThrow(_cursor, "rol");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUrl");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfLatitud = CursorUtil.getColumnIndexOrThrow(_cursor, "latitud");
          final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
          final int _cursorIndexOfBiometricEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "biometricEnabled");
          final int _cursorIndexOfFirestoreDocId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreDocId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Usuario _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpRol;
            if (_cursor.isNull(_cursorIndexOfRol)) {
              _tmpRol = null;
            } else {
              _tmpRol = _cursor.getString(_cursorIndexOfRol);
            }
            final int _tmpActivo;
            _tmpActivo = _cursor.getInt(_cursorIndexOfActivo);
            final String _tmpFotoUrl;
            if (_cursor.isNull(_cursorIndexOfFotoUrl)) {
              _tmpFotoUrl = null;
            } else {
              _tmpFotoUrl = _cursor.getString(_cursorIndexOfFotoUrl);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
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
            final int _tmpBiometricEnabled;
            _tmpBiometricEnabled = _cursor.getInt(_cursorIndexOfBiometricEnabled);
            final String _tmpFirestoreDocId;
            if (_cursor.isNull(_cursorIndexOfFirestoreDocId)) {
              _tmpFirestoreDocId = null;
            } else {
              _tmpFirestoreDocId = _cursor.getString(_cursorIndexOfFirestoreDocId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Usuario(_tmpId,_tmpNombre,_tmpEmail,_tmpPassword,_tmpTelefono,_tmpRol,_tmpActivo,_tmpFotoUrl,_tmpDireccion,_tmpLatitud,_tmpLongitud,_tmpBiometricEnabled,_tmpFirestoreDocId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getUsuarioByEmail(final String email,
      final Continuation<? super Usuario> $completion) {
    final String _sql = "SELECT * FROM usuarios WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Usuario>() {
      @Override
      @Nullable
      public Usuario call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfRol = CursorUtil.getColumnIndexOrThrow(_cursor, "rol");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUrl");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfLatitud = CursorUtil.getColumnIndexOrThrow(_cursor, "latitud");
          final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
          final int _cursorIndexOfBiometricEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "biometricEnabled");
          final int _cursorIndexOfFirestoreDocId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreDocId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Usuario _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpRol;
            if (_cursor.isNull(_cursorIndexOfRol)) {
              _tmpRol = null;
            } else {
              _tmpRol = _cursor.getString(_cursorIndexOfRol);
            }
            final int _tmpActivo;
            _tmpActivo = _cursor.getInt(_cursorIndexOfActivo);
            final String _tmpFotoUrl;
            if (_cursor.isNull(_cursorIndexOfFotoUrl)) {
              _tmpFotoUrl = null;
            } else {
              _tmpFotoUrl = _cursor.getString(_cursorIndexOfFotoUrl);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
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
            final int _tmpBiometricEnabled;
            _tmpBiometricEnabled = _cursor.getInt(_cursorIndexOfBiometricEnabled);
            final String _tmpFirestoreDocId;
            if (_cursor.isNull(_cursorIndexOfFirestoreDocId)) {
              _tmpFirestoreDocId = null;
            } else {
              _tmpFirestoreDocId = _cursor.getString(_cursorIndexOfFirestoreDocId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Usuario(_tmpId,_tmpNombre,_tmpEmail,_tmpPassword,_tmpTelefono,_tmpRol,_tmpActivo,_tmpFotoUrl,_tmpDireccion,_tmpLatitud,_tmpLongitud,_tmpBiometricEnabled,_tmpFirestoreDocId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object login(final String email, final String password,
      final Continuation<? super Usuario> $completion) {
    final String _sql = "SELECT * FROM usuarios WHERE email = ? AND password = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    _argIndex = 2;
    if (password == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, password);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Usuario>() {
      @Override
      @Nullable
      public Usuario call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfRol = CursorUtil.getColumnIndexOrThrow(_cursor, "rol");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUrl");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfLatitud = CursorUtil.getColumnIndexOrThrow(_cursor, "latitud");
          final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
          final int _cursorIndexOfBiometricEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "biometricEnabled");
          final int _cursorIndexOfFirestoreDocId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreDocId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Usuario _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpRol;
            if (_cursor.isNull(_cursorIndexOfRol)) {
              _tmpRol = null;
            } else {
              _tmpRol = _cursor.getString(_cursorIndexOfRol);
            }
            final int _tmpActivo;
            _tmpActivo = _cursor.getInt(_cursorIndexOfActivo);
            final String _tmpFotoUrl;
            if (_cursor.isNull(_cursorIndexOfFotoUrl)) {
              _tmpFotoUrl = null;
            } else {
              _tmpFotoUrl = _cursor.getString(_cursorIndexOfFotoUrl);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
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
            final int _tmpBiometricEnabled;
            _tmpBiometricEnabled = _cursor.getInt(_cursorIndexOfBiometricEnabled);
            final String _tmpFirestoreDocId;
            if (_cursor.isNull(_cursorIndexOfFirestoreDocId)) {
              _tmpFirestoreDocId = null;
            } else {
              _tmpFirestoreDocId = _cursor.getString(_cursorIndexOfFirestoreDocId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Usuario(_tmpId,_tmpNombre,_tmpEmail,_tmpPassword,_tmpTelefono,_tmpRol,_tmpActivo,_tmpFotoUrl,_tmpDireccion,_tmpLatitud,_tmpLongitud,_tmpBiometricEnabled,_tmpFirestoreDocId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getByFirestoreDocId(final String docId,
      final Continuation<? super Usuario> $completion) {
    final String _sql = "SELECT * FROM usuarios WHERE firestoreDocId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (docId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, docId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Usuario>() {
      @Override
      @Nullable
      public Usuario call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfRol = CursorUtil.getColumnIndexOrThrow(_cursor, "rol");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUrl");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfLatitud = CursorUtil.getColumnIndexOrThrow(_cursor, "latitud");
          final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
          final int _cursorIndexOfBiometricEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "biometricEnabled");
          final int _cursorIndexOfFirestoreDocId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreDocId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Usuario _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpRol;
            if (_cursor.isNull(_cursorIndexOfRol)) {
              _tmpRol = null;
            } else {
              _tmpRol = _cursor.getString(_cursorIndexOfRol);
            }
            final int _tmpActivo;
            _tmpActivo = _cursor.getInt(_cursorIndexOfActivo);
            final String _tmpFotoUrl;
            if (_cursor.isNull(_cursorIndexOfFotoUrl)) {
              _tmpFotoUrl = null;
            } else {
              _tmpFotoUrl = _cursor.getString(_cursorIndexOfFotoUrl);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
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
            final int _tmpBiometricEnabled;
            _tmpBiometricEnabled = _cursor.getInt(_cursorIndexOfBiometricEnabled);
            final String _tmpFirestoreDocId;
            if (_cursor.isNull(_cursorIndexOfFirestoreDocId)) {
              _tmpFirestoreDocId = null;
            } else {
              _tmpFirestoreDocId = _cursor.getString(_cursorIndexOfFirestoreDocId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Usuario(_tmpId,_tmpNombre,_tmpEmail,_tmpPassword,_tmpTelefono,_tmpRol,_tmpActivo,_tmpFotoUrl,_tmpDireccion,_tmpLatitud,_tmpLongitud,_tmpBiometricEnabled,_tmpFirestoreDocId,_tmpCreatedAt,_tmpUpdatedAt);
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
