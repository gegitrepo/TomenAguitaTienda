package com.example.tomenaguita.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tomenaguita.data.database.dao.*
import com.example.tomenaguita.data.database.entity.*

@Database(
    entities = [Usuario::class, Producto::class, CarritoItem::class, Pedido::class, DetallePedido::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao
    abstract fun pedidoDao(): PedidoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tomenaguita.db"
                ).build().also { INSTANCE = it }
            }
    }
}
