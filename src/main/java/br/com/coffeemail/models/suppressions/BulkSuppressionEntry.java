package br.com.coffeemail.models.suppressions;

import org.jspecify.annotations.Nullable;

/** Item da criação de supressões em lote. */
public record BulkSuppressionEntry(String email, @Nullable CreateSuppressionReason reason) {

    /**
     * Inicia a construção.
     *
     * @param email campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String email) {
        return new Builder(email);
    }

    /** Builder de {@link BulkSuppressionEntry}. */
    public static final class Builder {
        private final String email;
        private @Nullable CreateSuppressionReason reason;

        private Builder(final String email) {
            this.email = email;
        }

        public Builder reason(final CreateSuppressionReason value) {
            this.reason = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public BulkSuppressionEntry build() {
            return new BulkSuppressionEntry(email, reason);
        }
    }
}
