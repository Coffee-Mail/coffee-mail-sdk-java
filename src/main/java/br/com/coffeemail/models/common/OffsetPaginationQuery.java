package br.com.coffeemail.models.common;

import org.jspecify.annotations.Nullable;

/** Paginação por offset, usada por audiências e contatos. */
public record OffsetPaginationQuery(
        @Nullable Integer limit, @Nullable Integer offset) {

    /**
     * Inicia a construção.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder de {@link OffsetPaginationQuery}. */
    public static final class Builder {
        private @Nullable Integer limit;
        private @Nullable Integer offset;

        private Builder() {}

        public Builder limit(final Integer value) {
            this.limit = value;
            return this;
        }

        public Builder offset(final Integer value) {
            this.offset = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public OffsetPaginationQuery build() {
            return new OffsetPaginationQuery(limit, offset);
        }
    }
}
