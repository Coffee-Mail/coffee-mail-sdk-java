package br.com.coffeemail.models.suppressions;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Origem de uma supressão. */
public enum SuppressionSource {
    /** {@code manual}. */
    MANUAL("manual"),
    /** {@code auto}. */
    AUTO("auto"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    SuppressionSource(final String value) {
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
