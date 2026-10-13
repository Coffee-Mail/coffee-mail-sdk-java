package br.com.coffeemail.models.audiences;

import br.com.coffeemail.models.support.Copies;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** Dados para cadastrar um contato. */
public record CreateContactInput(
        String email,
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable Map<String, Object> metadata) {

    /**
     * Inicia a construção.
     *
     * @param email campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String email) {
        return new Builder(email);
    }

    /** Builder de {@link CreateContactInput}. */
    public static final class Builder {
        private final String email;
        private @Nullable String firstName;
        private @Nullable String lastName;
        private @Nullable Map<String, Object> metadata;

        private Builder(final String email) {
            this.email = email;
        }

        public Builder firstName(final String value) {
            this.firstName = value;
            return this;
        }

        public Builder lastName(final String value) {
            this.lastName = value;
            return this;
        }

        public Builder metadata(final Map<String, Object> value) {
            this.metadata = Copies.map(value);
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public CreateContactInput build() {
            return new CreateContactInput(email, firstName, lastName, metadata);
        }
    }
}
