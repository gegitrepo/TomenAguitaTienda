package com.example.tomenaguita.data.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&\u00a8\u0006\f"}, d2 = {"Lcom/example/tomenaguita/data/database/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "carritoDao", "Lcom/example/tomenaguita/data/database/dao/CarritoDao;", "pedidoDao", "Lcom/example/tomenaguita/data/database/dao/PedidoDao;", "productoDao", "Lcom/example/tomenaguita/data/database/dao/ProductoDao;", "usuarioDao", "Lcom/example/tomenaguita/data/database/dao/UsuarioDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.example.tomenaguita.data.database.entity.Usuario.class, com.example.tomenaguita.data.database.entity.Producto.class, com.example.tomenaguita.data.database.entity.CarritoItem.class, com.example.tomenaguita.data.database.entity.Pedido.class, com.example.tomenaguita.data.database.entity.DetallePedido.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.tomenaguita.data.database.AppDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_1_2 = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.data.database.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.tomenaguita.data.database.dao.UsuarioDao usuarioDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.tomenaguita.data.database.dao.ProductoDao productoDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.tomenaguita.data.database.dao.CarritoDao carritoDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.tomenaguita.data.database.dao.PedidoDao pedidoDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/example/tomenaguita/data/database/AppDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/example/tomenaguita/data/database/AppDatabase;", "MIGRATION_1_2", "Landroidx/room/migration/Migration;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.tomenaguita.data.database.AppDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}