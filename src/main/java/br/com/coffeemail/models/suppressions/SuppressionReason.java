package br.com.coffeemail.models.suppressions;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Motivo de uma supressão. */
public enum SuppressionReason {
    /** {@code manual}. */
    MANUAL("manual"),
    /** {@code bounce}. */
    BOUNCE("bounce"),
    /** {@code isp_block}. */
    ISP_BLOCK("isp_block"),
    /** {@code mailbox_not_found}. */
    MAILBOX_NOT_FOUND("mailbox_not_found"),
    /** {@code complaint}. */
    COMPLAINT("complaint"),
    /** {@code unsubscribe}. */
    UNSUBSCRIBE("unsubscribe"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    SuppressionReason(final String value) {
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
