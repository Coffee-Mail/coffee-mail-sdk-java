package br.com.coffeemail.models.webhooks;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Situação de um webhook; {@code disabled} é atribuído pelo sistema. */
public enum WebhookStatus {
    /** {@code active}. */
    ACTIVE("active"),
    /** {@code paused}. */
    PAUSED("paused"),
    /** {@code disabled}. */
    DISABLED("disabled"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    WebhookStatus(final String value) {
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
