package br.com.coffeemail.models.templates;

import org.jspecify.annotations.Nullable;

/** Paginação da listagem de templates. */
public record ListTemplatesQuery(
        @Nullable Integer limit, @Nullable String after) {

    /**
     * Inicia a construção.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder de {@link ListTemplatesQuery}. */
    public static final class Builder {
        private @Nullable Integer limit;
        private @Nullable String after;

        private Builder() {}

        public Builder limit(final Integer value) {
            this.limit = value;
            return this;
        }

        public Builder after(final String value) {
            this.after = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public ListTemplatesQuery build() {
            return new ListTemplatesQuery(limit, after);
        }
    }
}
