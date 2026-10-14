package br.com.coffeemail.models.broadcasts;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Situação de uma campanha. */
public enum BroadcastStatus {
    /** {@code draft}. */
    DRAFT("draft"),
    /** {@code scheduled}. */
    SCHEDULED("scheduled"),
    /** {@code sending}. */
    SENDING("sending"),
    /** {@code sent}. */
    SENT("sent"),
    /** {@code failed}. */
    FAILED("failed"),
    /** {@code cancelled}. */
    CANCELLED("cancelled"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    BroadcastStatus(final String value) {
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
