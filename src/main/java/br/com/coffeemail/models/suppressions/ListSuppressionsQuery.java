package br.com.coffeemail.models.suppressions;

import org.jspecify.annotations.Nullable;

/** Filtros da listagem de supressões. */
public record ListSuppressionsQuery(
        @Nullable SuppressionStatus status,
        @Nullable SuppressionReason reason,
        @Nullable SuppressionSource source,
        @Nullable SuppressionCategory category,
        @Nullable String after,
        @Nullable Integer limit) {

    /**
     * Inicia a construção.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder de {@link ListSuppressionsQuery}. */
    public static final class Builder {
        private @Nullable SuppressionStatus status;
        private @Nullable SuppressionReason reason;
        private @Nullable SuppressionSource source;
        private @Nullable SuppressionCategory category;
        private @Nullable String after;
        private @Nullable Integer limit;

        private Builder() {}

        public Builder status(final SuppressionStatus value) {
            this.status = value;
            return this;
        }

        public Builder reason(final SuppressionReason value) {
            this.reason = value;
            return this;
        }

        public Builder source(final SuppressionSource value) {
            this.source = value;
            return this;
        }

        public Builder category(final SuppressionCategory value) {
            this.category = value;
            return this;
        }

        public Builder after(final String value) {
            this.after = value;
            return this;
        }

        public Builder limit(final Integer value) {
            this.limit = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public ListSuppressionsQuery build() {
            return new ListSuppressionsQuery(status, reason, source, category, after, limit);
        }
    }
}
