package br.com.coffeemail.models.emails;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Situação de um e-mail recém-enfileirado. */
public enum SendEmailStatus {
    /** {@code queued}. */
    QUEUED("queued"),
    /** {@code scheduled}. */
    SCHEDULED("scheduled"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    SendEmailStatus(final String value) {
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
