package br.com.coffeemail.models.templates;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Tipo de uma variável de template. */
public enum TemplateVariableType {
    /** {@code string}. */
    STRING("string"),
    /** {@code number}. */
    NUMBER("number"),
    /** {@code color}. */
    COLOR("color"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    TemplateVariableType(final String value) {
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
