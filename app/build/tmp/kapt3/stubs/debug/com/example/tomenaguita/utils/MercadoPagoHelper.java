package com.example.tomenaguita.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J>\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004H\u0086@\u00a2\u0006\u0002\u0010\u0010R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/example/tomenaguita/utils/MercadoPagoHelper;", "", "()V", "API_URL", "", "client", "Lokhttp3/OkHttpClient;", "crearPreferencia", "accessToken", "titulo", "cantidad", "", "precioUnitario", "", "emailComprador", "referenciaExterna", "(Ljava/lang/String;Ljava/lang/String;IDLjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class MercadoPagoHelper {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String API_URL = "https://api.mercadopago.com/checkout/preferences";
    @org.jetbrains.annotations.NotNull()
    private static final okhttp3.OkHttpClient client = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.utils.MercadoPagoHelper INSTANCE = null;
    
    private MercadoPagoHelper() {
        super();
    }
    
    /**
     * Crea una preferencia de pago en Mercado Pago Sandbox.
     *
     * Sin `payer.email` el checkout obliga al comprador a iniciar sesión,
     * lo cual es necesario en sandbox Colombia para que las tarjetas de prueba funcionen.
     *
     * Buyer test user: TESTUSER1685025191177064552 / 9FzRrFjciM
     * Tarjetas: Mastercard 5254 1336 7440 3564 · Visa 4013 5406 8274 6260
     * Titular: APRO (aprobado) · CVV: 123 · Venc: 11/30 · DNI: 123456789
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object crearPreferencia(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    java.lang.String titulo, int cantidad, double precioUnitario, @org.jetbrains.annotations.NotNull()
    java.lang.String emailComprador, @org.jetbrains.annotations.NotNull()
    java.lang.String referenciaExterna, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
}