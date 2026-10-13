package br.com.coffeemail.models.emails;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Situação de um e-mail. */
public enum EmailStatus {
    /** {@code queued}. */
    QUEUED("queued"),
    /** {@code processing}. */
    PROCESSING("processing"),
    /** {@code sent}. */
    SENT("sent"),
    /** {@code delivered}. */
    DELIVERED("delivered"),
    /** {@code bounced}. */
    BOUNCED("bounced"),
    /** {@code complained}. */
    COMPLAINED("complained"),
    /** {@code failed}. */
    FAILED("failed"),
    /** {@code skipped}. */
    SKIPPED("skipped"),
    /** {@code scheduled}. */
    SCHEDULED("scheduled"),
    /** {@code cancelled}. */
    CANCELLED("cancelled"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    EmailStatus(final String value) {
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
