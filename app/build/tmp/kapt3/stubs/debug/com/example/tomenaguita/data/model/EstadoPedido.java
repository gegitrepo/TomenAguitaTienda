package com.example.tomenaguita.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0086\u0081\u0002\u0018\u0000 \r2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\rB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u0004\u0018\u00010\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f\u00a8\u0006\u000e"}, d2 = {"Lcom/example/tomenaguita/data/model/EstadoPedido;", "", "valor", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getValor", "()Ljava/lang/String;", "siguiente", "PENDIENTE", "PAGADO", "ENVIADO", "ENTREGADO", "CANCELADO", "Companion", "app_debug"})
public enum EstadoPedido {
    /*public static final*/ PENDIENTE /* = new PENDIENTE(null) */,
    /*public static final*/ PAGADO /* = new PAGADO(null) */,
    /*public static final*/ ENVIADO /* = new ENVIADO(null) */,
    /*public static final*/ ENTREGADO /* = new ENTREGADO(null) */,
    /*public static final*/ CANCELADO /* = new CANCELADO(null) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String valor = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.data.model.EstadoPedido.Companion Companion = null;
    
    EstadoPedido(java.lang.String valor) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getValor() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.example.tomenaguita.data.model.EstadoPedido siguiente() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.example.tomenaguita.data.model.EstadoPedido> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/tomenaguita/data/model/EstadoPedido$Companion;", "", "()V", "fromString", "Lcom/example/tomenaguita/data/model/EstadoPedido;", "valor", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.tomenaguita.data.model.EstadoPedido fromString(@org.jetbrains.annotations.NotNull()
        java.lang.String valor) {
            return null;
        }
    }
}