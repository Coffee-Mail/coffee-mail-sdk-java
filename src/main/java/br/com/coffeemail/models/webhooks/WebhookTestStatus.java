package br.com.coffeemail.models.webhooks;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Resultado do envio de teste de um webhook. */
public enum WebhookTestStatus {
    /** {@code success}. */
    SUCCESS("success"),
    /** {@code failed}. */
    FAILED("failed"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    WebhookTestStatus(final String value) {
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
