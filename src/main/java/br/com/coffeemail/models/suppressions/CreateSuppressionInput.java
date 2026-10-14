package br.com.coffeemail.models.suppressions;

import java.time.Instant;
import org.jspecify.annotations.Nullable;

/** Dados para criar uma supressão. */
public record CreateSuppressionInput(
        String email,
        @Nullable CreateSuppressionReason reason,
        @Nullable Instant expiresAt) {

    /**
     * Inicia a construção.
     *
     * @param email campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String email) {
        return new Builder(email);
    }

    /** Builder de {@link CreateSuppressionInput}. */
    public static final class Builder {
        private final String email;
        private @Nullable CreateSuppressionReason reason;
        private @Nullable Instant expiresAt;

        private Builder(final String email) {
            this.email = email;
        }

        public Builder reason(final CreateSuppressionReason value) {
            this.reason = value;
            return this;
        }

        public Builder expiresAt(final Instant value) {
            this.expiresAt = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public CreateSuppressionInput build() {
            return new CreateSuppressionInput(email, reason, expiresAt);
        }
    }
}
