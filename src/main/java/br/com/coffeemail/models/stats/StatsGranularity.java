package br.com.coffeemail.models.stats;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Agrupamento da série histórica. */
public enum StatsGranularity {
    /** {@code day}. */
    DAY("day"),
    /** {@code week}. */
    WEEK("week"),
    /** {@code month}. */
    MONTH("month"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    StatsGranularity(final String value) {
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
