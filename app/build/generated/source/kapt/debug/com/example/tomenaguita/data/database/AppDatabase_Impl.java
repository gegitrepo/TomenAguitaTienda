package com.example.tomenaguita.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.tomenaguita.data.database.dao.CarritoDao;
import com.example.tomenaguita.data.database.dao.CarritoDao_Impl;
import com.example.tomenaguita.data.database.dao.PedidoDao;
import com.example.tomenaguita.data.database.dao.PedidoDao_Impl;
import com.example.tomenaguita.data.database.dao.ProductoDao;
import com.example.tomenaguita.data.database.dao.ProductoDao_Impl;
import com.example.tomenaguita.data.database.dao.UsuarioDao;
import com.example.tomenaguita.data.database.dao.UsuarioDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UsuarioDao _usuarioDao;

  private volatile ProductoDao _productoDao;

  private volatile CarritoDao _carritoDao;

  private volatile PedidoDao _pedidoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `usuarios` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `email` TEXT NOT NULL, `password` TEXT NOT NULL, `telefono` TEXT NOT NULL, `rol` TEXT NOT NULL, `activo` INTEGER NOT NULL, `fotoUrl` TEXT, `direccion` TEXT, `latitud` REAL, `longitud` REAL, `biometricEnabled` INTEGER NOT NULL, `firestoreDocId` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `productos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `presentacion` TEXT NOT NULL, `precio` REAL NOT NULL, `imagenUrl` TEXT, `disponible` INTEGER NOT NULL, `stock` INTEGER NOT NULL, `vendedorId` INTEGER NOT NULL, `eliminado` INTEGER NOT NULL, `firestoreDocId` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `carrito` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `usuarioId` INTEGER NOT NULL, `productoId` INTEGER NOT NULL, `cantidad` INTEGER NOT NULL, `precioAlMomento` REAL NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pedidos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `orderNumber` TEXT NOT NULL, `usuarioId` INTEGER NOT NULL, `totalProductos` REAL NOT NULL, `costoEnvio` REAL NOT NULL, `totalPedido` REAL NOT NULL, `direccionEntrega` TEXT NOT NULL, `latitud` REAL, `longitud` REAL, `estado` TEXT NOT NULL, `metodoPago` TEXT NOT NULL, `transactionId` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `detalle_pedidos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pedidoId` INTEGER NOT NULL, `productoId` INTEGER NOT NULL, `nombreProducto` TEXT NOT NULL, `presentacion` TEXT NOT NULL, `cantidad` INTEGER NOT NULL, `precioUnitario` REAL NOT NULL, `subtotal` REAL NOT NULL, `vendedorId` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2209f83f682643fb7ca6d992edb83a54')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `usuarios`");
        db.execSQL("DROP TABLE IF EXISTS `productos`");
        db.execSQL("DROP TABLE IF EXISTS `carrito`");
        db.execSQL("DROP TABLE IF EXISTS `pedidos`");
        db.execSQL("DROP TABLE IF EXISTS `detalle_pedidos`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsuarios = new HashMap<String, TableInfo.Column>(15);
        _columnsUsuarios.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("telefono", new TableInfo.Column("telefono", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("rol", new TableInfo.Column("rol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("activo", new TableInfo.Column("activo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("fotoUrl", new TableInfo.Column("fotoUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("direccion", new TableInfo.Column("direccion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("latitud", new TableInfo.Column("latitud", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("longitud", new TableInfo.Column("longitud", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("biometricEnabled", new TableInfo.Column("biometricEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("firestoreDocId", new TableInfo.Column("firestoreDocId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsuarios = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsuarios = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsuarios = new TableInfo("usuarios", _columnsUsuarios, _foreignKeysUsuarios, _indicesUsuarios);
        final TableInfo _existingUsuarios = TableInfo.read(db, "usuarios");
        if (!_infoUsuarios.equals(_existingUsuarios)) {
          return new RoomOpenHelper.ValidationResult(false, "usuarios(com.example.tomenaguita.data.database.entity.Usuario).\n"
                  + " Expected:\n" + _infoUsuarios + "\n"
                  + " Found:\n" + _existingUsuarios);
        }
        final HashMap<String, TableInfo.Column> _columnsProductos = new HashMap<String, TableInfo.Column>(13);
        _columnsProductos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("descripcion", new TableInfo.Column("descripcion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("presentacion", new TableInfo.Column("presentacion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("precio", new TableInfo.Column("precio", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("imagenUrl", new TableInfo.Column("imagenUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("disponible", new TableInfo.Column("disponible", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("stock", new TableInfo.Column("stock", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("vendedorId", new TableInfo.Column("vendedorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("eliminado", new TableInfo.Column("eliminado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("firestoreDocId", new TableInfo.Column("firestoreDocId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProductos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProductos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductos = new TableInfo("productos", _columnsProductos, _foreignKeysProductos, _indicesProductos);
        final TableInfo _existingProductos = TableInfo.read(db, "productos");
        if (!_infoProductos.equals(_existingProductos)) {
          return new RoomOpenHelper.ValidationResult(false, "productos(com.example.tomenaguita.data.database.entity.Producto).\n"
                  + " Expected:\n" + _infoProductos + "\n"
                  + " Found:\n" + _existingProductos);
        }
        final HashMap<String, TableInfo.Column> _columnsCarrito = new HashMap<String, TableInfo.Column>(7);
        _columnsCarrito.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCarrito.put("usuarioId", new TableInfo.Column("usuarioId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCarrito.put("productoId", new TableInfo.Column("productoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCarrito.put("cantidad", new TableInfo.Column("cantidad", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCarrito.put("precioAlMomento", new TableInfo.Column("precioAlMomento", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCarrito.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCarrito.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCarrito = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCarrito = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCarrito = new TableInfo("carrito", _columnsCarrito, _foreignKeysCarrito, _indicesCarrito);
        final TableInfo _existingCarrito = TableInfo.read(db, "carrito");
        if (!_infoCarrito.equals(_existingCarrito)) {
          return new RoomOpenHelper.ValidationResult(false, "carrito(com.example.tomenaguita.data.database.entity.CarritoItem).\n"
                  + " Expected:\n" + _infoCarrito + "\n"
                  + " Found:\n" + _existingCarrito);
        }
        final HashMap<String, TableInfo.Column> _columnsPedidos = new HashMap<String, TableInfo.Column>(14);
        _columnsPedidos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("orderNumber", new TableInfo.Column("orderNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("usuarioId", new TableInfo.Column("usuarioId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("totalProductos", new TableInfo.Column("totalProductos", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("costoEnvio", new TableInfo.Column("costoEnvio", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("totalPedido", new TableInfo.Column("totalPedido", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("direccionEntrega", new TableInfo.Column("direccionEntrega", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("latitud", new TableInfo.Column("latitud", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("longitud", new TableInfo.Column("longitud", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("estado", new TableInfo.Column("estado", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("metodoPago", new TableInfo.Column("metodoPago", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("transactionId", new TableInfo.Column("transactionId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPedidos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPedidos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPedidos = new TableInfo("pedidos", _columnsPedidos, _foreignKeysPedidos, _indicesPedidos);
        final TableInfo _existingPedidos = TableInfo.read(db, "pedidos");
        if (!_infoPedidos.equals(_existingPedidos)) {
          return new RoomOpenHelper.ValidationResult(false, "pedidos(com.example.tomenaguita.data.database.entity.Pedido).\n"
                  + " Expected:\n" + _infoPedidos + "\n"
                  + " Found:\n" + _existingPedidos);
        }
        final HashMap<String, TableInfo.Column> _columnsDetallePedidos = new HashMap<String, TableInfo.Column>(9);
        _columnsDetallePedidos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedidos.put("pedidoId", new TableInfo.Column("pedidoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedidos.put("productoId", new TableInfo.Column("productoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedidos.put("nombreProducto", new TableInfo.Column("nombreProducto", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedidos.put("presentacion", new TableInfo.Column("presentacion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedidos.put("cantidad", new TableInfo.Column("cantidad", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedidos.put("precioUnitario", new TableInfo.Column("precioUnitario", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedidos.put("subtotal", new TableInfo.Column("subtotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedidos.put("vendedorId", new TableInfo.Column("vendedorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDetallePedidos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDetallePedidos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDetallePedidos = new TableInfo("detalle_pedidos", _columnsDetallePedidos, _foreignKeysDetallePedidos, _indicesDetallePedidos);
        final TableInfo _existingDetallePedidos = TableInfo.read(db, "detalle_pedidos");
        if (!_infoDetallePedidos.equals(_existingDetallePedidos)) {
          return new RoomOpenHelper.ValidationResult(false, "detalle_pedidos(com.example.tomenaguita.data.database.entity.DetallePedido).\n"
                  + " Expected:\n" + _infoDetallePedidos + "\n"
                  + " Found:\n" + _existingDetallePedidos);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2209f83f682643fb7ca6d992edb83a54", "962719439a0b5f7132dac3816cb9ace0");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "usuarios","productos","carrito","pedidos","detalle_pedidos");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `usuarios`");
      _db.execSQL("DELETE FROM `productos`");
      _db.execSQL("DELETE FROM `carrito`");
      _db.execSQL("DELETE FROM `pedidos`");
      _db.execSQL("DELETE FROM `detalle_pedidos`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UsuarioDao.class, UsuarioDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProductoDao.class, ProductoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CarritoDao.class, CarritoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PedidoDao.class, PedidoDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UsuarioDao usuarioDao() {
    if (_usuarioDao != null) {
      return _usuarioDao;
    } else {
      synchronized(this) {
        if(_usuarioDao == null) {
          _usuarioDao = new UsuarioDao_Impl(this);
        }
        return _usuarioDao;
      }
    }
  }

  @Override
  public ProductoDao productoDao() {
    if (_productoDao != null) {
      return _productoDao;
    } else {
      synchronized(this) {
        if(_productoDao == null) {
          _productoDao = new ProductoDao_Impl(this);
        }
        return _productoDao;
      }
    }
  }

  @Override
  public CarritoDao carritoDao() {
    if (_carritoDao != null) {
      return _carritoDao;
    } else {
      synchronized(this) {
        if(_carritoDao == null) {
          _carritoDao = new CarritoDao_Impl(this);
        }
        return _carritoDao;
      }
    }
  }

  @Override
  public PedidoDao pedidoDao() {
    if (_pedidoDao != null) {
      return _pedidoDao;
    } else {
      synchronized(this) {
        if(_pedidoDao == null) {
          _pedidoDao = new PedidoDao_Impl(this);
        }
        return _pedidoDao;
      }
    }
  }
}
