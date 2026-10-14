package br.com.coffeemail.models.webhooks;

import com.fasterxml.jackson.annotation.JsonValue;

/** Situações aceitas ao ativar ou pausar um webhook. */
public enum ToggleableWebhookStatus {
    /** {@code active}. */
    ACTIVE("active"),
    /** {@code paused}. */
    PAUSED("paused");

    private final String value;

    ToggleableWebhookStatus(final String value) {
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
