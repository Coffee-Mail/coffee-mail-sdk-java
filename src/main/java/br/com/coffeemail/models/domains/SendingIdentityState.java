package br.com.coffeemail.models.domains;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Situação do domínio no provedor de envio. */
public enum SendingIdentityState {
    /** {@code not_provisioned}. */
    NOT_PROVISIONED("not_provisioned"),
    /** {@code pending}. */
    PENDING("pending"),
    /** {@code ready}. */
    READY("ready"),
    /** {@code failed}. */
    FAILED("failed"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    SendingIdentityState(final String value) {
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
