package br.com.coffeemail.models.audiences;

import org.jspecify.annotations.Nullable;

/** Dados para criar uma audiência. */
public record CreateAudienceInput(String name, @Nullable String description) {

    /**
     * Inicia a construção.
     *
     * @param name campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String name) {
        return new Builder(name);
    }

    /** Builder de {@link CreateAudienceInput}. */
    public static final class Builder {
        private final String name;
        private @Nullable String description;

        private Builder(final String name) {
            this.name = name;
        }

        public Builder description(final String value) {
            this.description = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public CreateAudienceInput build() {
            return new CreateAudienceInput(name, description);
        }
    }
}
