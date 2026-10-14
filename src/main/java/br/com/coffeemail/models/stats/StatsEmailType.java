package br.com.coffeemail.models.stats;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Tipo de e-mail considerado nas estatísticas. */
public enum StatsEmailType {
    /** {@code all}. */
    ALL("all"),
    /** {@code transactional}. */
    TRANSACTIONAL("transactional"),
    /** {@code broadcast}. */
    BROADCAST("broadcast"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    StatsEmailType(final String value) {
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
