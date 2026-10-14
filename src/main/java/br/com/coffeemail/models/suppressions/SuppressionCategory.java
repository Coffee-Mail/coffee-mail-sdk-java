package br.com.coffeemail.models.suppressions;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Categoria de supressão usada como filtro. */
public enum SuppressionCategory {
    /** {@code manual}. */
    MANUAL("manual"),
    /** {@code bounce}. */
    BOUNCE("bounce"),
    /** {@code block}. */
    BLOCK("block"),
    /** {@code complaint}. */
    COMPLAINT("complaint"),
    /** {@code unsubscribe}. */
    UNSUBSCRIBE("unsubscribe"),
    /** {@code invalid_email}. */
    INVALID_EMAIL("invalid_email"),
    /** {@code global}. */
    GLOBAL("global"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    SuppressionCategory(final String value) {
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
