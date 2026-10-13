package br.com.coffeemail.models.domains;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Motivo da situação atual de um domínio. */
public enum DomainStatusReason {
    /** {@code dns_records_missing}. */
    DNS_RECORDS_MISSING("dns_records_missing"),
    /** {@code provider_pending}. */
    PROVIDER_PENDING("provider_pending"),
    /** {@code provider_failed}. */
    PROVIDER_FAILED("provider_failed"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    DomainStatusReason(final String value) {
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
