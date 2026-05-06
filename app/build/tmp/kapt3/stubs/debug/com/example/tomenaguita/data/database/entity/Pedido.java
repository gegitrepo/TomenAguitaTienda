package com.example.tomenaguita.data.database.entity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b/\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u008b\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\b\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0005\u0012\u0006\u0010\u000f\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0013J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0005H\u00c6\u0003J\t\u0010)\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\t\u0010-\u001a\u00020\u0005H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\bH\u00c6\u0003J\t\u00100\u001a\u00020\bH\u00c6\u0003J\t\u00101\u001a\u00020\bH\u00c6\u0003J\t\u00102\u001a\u00020\u0005H\u00c6\u0003J\u0010\u00103\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001dJ\u0010\u00104\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001dJ\u00a0\u0001\u00105\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\b2\b\b\u0002\u0010\u000b\u001a\u00020\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00052\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u00106J\u0013\u00107\u001a\u0002082\b\u00109\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010:\u001a\u00020;H\u00d6\u0001J\t\u0010<\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0017R\u0015\u0010\f\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u001e\u001a\u0004\b\u001c\u0010\u001dR\u0015\u0010\r\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u001e\u001a\u0004\b\u001f\u0010\u001dR\u0011\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0019R\u0011\u0010\n\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0015R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0015R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0019R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0017\u00a8\u0006="}, d2 = {"Lcom/example/tomenaguita/data/database/entity/Pedido;", "", "id", "", "orderNumber", "", "usuarioId", "totalProductos", "", "costoEnvio", "totalPedido", "direccionEntrega", "latitud", "longitud", "estado", "metodoPago", "transactionId", "createdAt", "updatedAt", "(JLjava/lang/String;JDDDLjava/lang/String;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJ)V", "getCostoEnvio", "()D", "getCreatedAt", "()J", "getDireccionEntrega", "()Ljava/lang/String;", "getEstado", "getId", "getLatitud", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getLongitud", "getMetodoPago", "getOrderNumber", "getTotalPedido", "getTotalProductos", "getTransactionId", "getUpdatedAt", "getUsuarioId", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLjava/lang/String;JDDDLjava/lang/String;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJ)Lcom/example/tomenaguita/data/database/entity/Pedido;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
@androidx.room.Entity(tableName = "pedidos")
public final class Pedido {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String orderNumber = null;
    private final long usuarioId = 0L;
    private final double totalProductos = 0.0;
    private final double costoEnvio = 0.0;
    private final double totalPedido = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String direccionEntrega = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double latitud = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double longitud = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String estado = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String metodoPago = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String transactionId = null;
    private final long createdAt = 0L;
    private final long updatedAt = 0L;
    
    public Pedido(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String orderNumber, long usuarioId, double totalProductos, double costoEnvio, double totalPedido, @org.jetbrains.annotations.NotNull()
    java.lang.String direccionEntrega, @org.jetbrains.annotations.Nullable()
    java.lang.Double latitud, @org.jetbrains.annotations.Nullable()
    java.lang.Double longitud, @org.jetbrains.annotations.NotNull()
    java.lang.String estado, @org.jetbrains.annotations.NotNull()
    java.lang.String metodoPago, @org.jetbrains.annotations.Nullable()
    java.lang.String transactionId, long createdAt, long updatedAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOrderNumber() {
        return null;
    }
    
    public final long getUsuarioId() {
        return 0L;
    }
    
    public final double getTotalProductos() {
        return 0.0;
    }
    
    public final double getCostoEnvio() {
        return 0.0;
    }
    
    public final double getTotalPedido() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDireccionEntrega() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getLatitud() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getLongitud() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEstado() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMetodoPago() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTransactionId() {
        return null;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    public final long getUpdatedAt() {
        return 0L;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }
    
    public final long component13() {
        return 0L;
    }
    
    public final long component14() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.tomenaguita.data.database.entity.Pedido copy(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String orderNumber, long usuarioId, double totalProductos, double costoEnvio, double totalPedido, @org.jetbrains.annotations.NotNull()
    java.lang.String direccionEntrega, @org.jetbrains.annotations.Nullable()
    java.lang.Double latitud, @org.jetbrains.annotations.Nullable()
    java.lang.Double longitud, @org.jetbrains.annotations.NotNull()
    java.lang.String estado, @org.jetbrains.annotations.NotNull()
    java.lang.String metodoPago, @org.jetbrains.annotations.Nullable()
    java.lang.String transactionId, long createdAt, long updatedAt) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}