package br.com.coffeemail.models.emails;

import com.fasterxml.jackson.annotation.JsonValue;

/** Modo de anexação. */
public enum AttachmentDisposition {
    /** {@code attachment}. */
    ATTACHMENT("attachment"),
    /** {@code inline}. */
    INLINE("inline");

    private final String value;

    AttachmentDisposition(final String value) {
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
