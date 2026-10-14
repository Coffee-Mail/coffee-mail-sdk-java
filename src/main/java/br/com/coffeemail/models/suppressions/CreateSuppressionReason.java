package br.com.coffeemail.models.suppressions;

import com.fasterxml.jackson.annotation.JsonValue;

/** Motivos aceitos na criação manual de uma supressão. */
public enum CreateSuppressionReason {
    /** {@code manual}. */
    MANUAL("manual"),
    /** {@code bounce}. */
    BOUNCE("bounce"),
    /** {@code complaint}. */
    COMPLAINT("complaint");

    private final String value;

    CreateSuppressionReason(final String value) {
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
