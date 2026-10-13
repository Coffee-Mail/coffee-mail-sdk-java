package br.com.coffeemail.models.emails;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Classificação do e-mail para supressão e relatórios. */
public enum EmailType {
    /** {@code transactional}. */
    TRANSACTIONAL("transactional"),
    /** {@code broadcast}. */
    BROADCAST("broadcast"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    EmailType(final String value) {
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
