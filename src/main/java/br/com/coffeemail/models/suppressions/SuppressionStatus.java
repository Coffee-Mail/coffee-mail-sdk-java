package br.com.coffeemail.models.suppressions;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Situação de uma supressão. */
public enum SuppressionStatus {
    /** {@code active}. */
    ACTIVE("active"),
    /** {@code expired}. */
    EXPIRED("expired"),
    /** {@code inactive}. */
    INACTIVE("inactive"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    SuppressionStatus(final String value) {
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
