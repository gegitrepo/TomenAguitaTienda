package com.example.tomenaguita.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0004H\u0086@\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/example/tomenaguita/utils/StripeHelper;", "", "()V", "API_URL", "", "createPaymentIntent", "amount", "", "currency", "(ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class StripeHelper {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String API_URL = "https://api.stripe.com/v1/payment_intents";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.utils.StripeHelper INSTANCE = null;
    
    private StripeHelper() {
        super();
    }
    
    /**
     * Crea un PaymentIntent en Stripe y devuelve el client_secret necesario
     * para confirmar el pago desde el SDK de Stripe en el cliente.
     *
     * Consume:
     *  - amount: monto en la unidad principal de la moneda (ej. 15000 para 15 000 COP).
     *            Se multiplica por 100 internamente para enviar centavos a Stripe.
     *  - currency: código ISO de moneda en minúsculas (por defecto "cop").
     *
     * Devuelve: el client_secret del PaymentIntent creado.
     * Lanza Exception si Stripe responde con un código de error HTTP.
     *
     * HARDCODED: usa Constants.STRIPE_SECRET_KEY directamente en el cliente;
     *           solo valido en entorno de prueba academico.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createPaymentIntent(int amount, @org.jetbrains.annotations.NotNull()
    java.lang.String currency, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
}