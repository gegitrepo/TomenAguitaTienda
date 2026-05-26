package com.example.tomenaguita.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tomenaguita.data.database.dao.*
import com.example.tomenaguita.data.database.entity.*

/*
 * Clase principal de la base de datos local Room de TomenAgüita.
 * Define las entidades que conforman el esquema, la version actual de la base
 * y expone los DAOs para acceder a cada tabla.
 *
 * Se implementa con el patron Singleton (doble verificacion con bloqueo) para
 * garantizar que exista una unica instancia en toda la aplicacion.
 *
 * Version actual: 2
 *   - Version 1: esquema inicial con todas las tablas.
 *   - Version 2: agrega la columna firestoreDocId a usuarios y productos
 *                para soportar la sincronizacion con Firestore.
 */
@Database(
    entities = [Usuario::class, Producto::class, CarritoItem::class, Pedido::class, DetallePedido::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAO para operaciones CRUD sobre la tabla de usuarios
    abstract fun usuarioDao(): UsuarioDao

    // DAO para operaciones CRUD sobre la tabla de productos
    abstract fun productoDao(): ProductoDao

    // DAO para operaciones CRUD sobre la tabla del carrito de compras
    abstract fun carritoDao(): CarritoDao

    // DAO para operaciones CRUD sobre las tablas de pedidos y detalles de pedidos
    abstract fun pedidoDao(): PedidoDao

    companion object {
        // La anotacion Volatile asegura que los cambios a INSTANCE sean visibles
        // inmediatamente para todos los hilos
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /*
         * Migracion de la version 1 a la version 2.
         * Agrega la columna firestoreDocId (TEXT, nullable) a las tablas
         * usuarios y productos para vincular cada fila con su documento en Firestore.
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE usuarios ADD COLUMN firestoreDocId TEXT")
                database.execSQL("ALTER TABLE productos ADD COLUMN firestoreDocId TEXT")
            }
        }

        /**
         * Devuelve la instancia unica de AppDatabase, creandola si no existe.
         * Usa doble verificacion con bloqueo (double-checked locking) para
         * garantizar que solo se cree una instancia incluso bajo concurrencia.
         *
         * Consume: context — contexto de Android; se usa applicationContext
         *          para evitar fugas de memoria.
         * Devuelve: la instancia unica de AppDatabase.
         */
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tomenaguita.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build().also { INSTANCE = it }
            }
    }
}
