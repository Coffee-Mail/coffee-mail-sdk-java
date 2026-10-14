package br.com.coffeemail.webhooks;

/** Cabeçalhos enviados pela CoffeeMail em cada entrega de webhook. */
public final class WebhookHeaders {

    /** Assinatura no formato {@code sha256=<hex>}. */
    public static final String SIGNATURE = "x-webhook-signature";
    /** Momento da assinatura, em segundos Unix. */
    public static final String TIMESTAMP = "x-webhook-timestamp";
    /** Chave de idempotência da entrega; use-a para descartar duplicatas. */
    public static final String IDEMPOTENCY_KEY = "x-webhook-idempotency-key";
    /** Presente em entregas de teste disparadas pelo dashboard ou pela API. */
    public static final String TEST = "x-webhook-test";

    private WebhookHeaders() {}
}
