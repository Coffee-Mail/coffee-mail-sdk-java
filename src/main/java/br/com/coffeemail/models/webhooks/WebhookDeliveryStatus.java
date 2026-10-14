package br.com.coffeemail.models.webhooks;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Situação de uma entrega de webhook. */
public enum WebhookDeliveryStatus {
    /** {@code pending}. */
    PENDING("pending"),
    /** {@code success}. */
    SUCCESS("success"),
    /** {@code failed}. */
    FAILED("failed"),
    /** {@code exhausted}. */
    EXHAUSTED("exhausted"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    WebhookDeliveryStatus(final String value) {
        this.value = value;
    }

    /**
     * Valor usado pela API.
     *
     * @return o valor serializado
     */
    @JsonValue
    public String value() {
        return value;
    }
}
