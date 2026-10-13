package br.com.coffeemail.models.domains;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Provedor responsável pela assinatura DKIM. */
public enum SendingProvider {
    /** {@code aws_ses}. */
    AWS_SES("aws_ses"),
    /** {@code none}. */
    NONE("none"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    SendingProvider(final String value) {
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
