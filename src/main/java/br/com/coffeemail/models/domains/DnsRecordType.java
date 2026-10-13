package br.com.coffeemail.models.domains;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Tipo de registro DNS. */
public enum DnsRecordType {
    /** {@code TXT}. */
    TXT("TXT"),
    /** {@code CNAME}. */
    CNAME("CNAME"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    DnsRecordType(final String value) {
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
