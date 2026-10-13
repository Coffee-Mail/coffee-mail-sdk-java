package br.com.coffeemail.models.domains;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Verificação DNS exigida para o domínio. */
public enum DomainCheck {
    /** {@code spf}. */
    SPF("spf"),
    /** {@code dkim}. */
    DKIM("dkim"),
    /** {@code dmarc}. */
    DMARC("dmarc"),
    /** {@code ownership}. */
    OWNERSHIP("ownership"),
    /** {@code infrastructureSpf}. */
    INFRASTRUCTURESPF("infrastructureSpf"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    DomainCheck(final String value) {
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
