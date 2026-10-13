package br.com.coffeemail.models.domains;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Resultado da consulta DNS de uma verificação. */
public enum DnsResolution {
    /** {@code found}. */
    FOUND("found"),
    /** {@code absent}. */
    ABSENT("absent"),
    /** {@code unknown}. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    DnsResolution(final String value) {
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
