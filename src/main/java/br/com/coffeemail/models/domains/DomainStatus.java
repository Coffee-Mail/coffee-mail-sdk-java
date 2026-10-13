package br.com.coffeemail.models.domains;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Situação de verificação de um domínio. */
public enum DomainStatus {
    /** {@code pending}. */
    PENDING("pending"),
    /** {@code verified}. */
    VERIFIED("verified"),
    /** {@code failed}. */
    FAILED("failed"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    DomainStatus(final String value) {
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
