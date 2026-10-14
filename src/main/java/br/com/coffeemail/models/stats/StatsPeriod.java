package br.com.coffeemail.models.stats;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Intervalo relativo. */
public enum StatsPeriod {
    /** {@code last7d}. */
    LAST7D("last7d"),
    /** {@code last30d}. */
    LAST30D("last30d"),
    /** {@code last90d}. */
    LAST90D("last90d"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    StatsPeriod(final String value) {
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
