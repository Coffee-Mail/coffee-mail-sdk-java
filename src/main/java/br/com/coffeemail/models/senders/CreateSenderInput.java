package br.com.coffeemail.models.senders;

import org.jspecify.annotations.Nullable;

/** Dados para cadastrar um remetente verificado por endereço. */
public record CreateSenderInput(String email, @Nullable String displayName) {

    /**
     * Inicia a construção.
     *
     * @param email campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String email) {
        return new Builder(email);
    }

    /** Builder de {@link CreateSenderInput}. */
    public static final class Builder {
        private final String email;
        private @Nullable String displayName;

        private Builder(final String email) {
            this.email = email;
        }

        public Builder displayName(final String value) {
            this.displayName = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public CreateSenderInput build() {
            return new CreateSenderInput(email, displayName);
        }
    }
}
