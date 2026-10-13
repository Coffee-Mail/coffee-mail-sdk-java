package br.com.coffeemail.models.templates;

import br.com.coffeemail.models.support.Copies;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** Envio de teste de um template. */
public record TestSendTemplateInput(
        String to,
        @Nullable String from,
        @Nullable String subject,
        @Nullable Map<String, Object> variables) {

    /**
     * Inicia a construção.
     *
     * @param to campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String to) {
        return new Builder(to);
    }

    /** Builder de {@link TestSendTemplateInput}. */
    public static final class Builder {
        private final String to;
        private @Nullable String from;
        private @Nullable String subject;
        private @Nullable Map<String, Object> variables;

        private Builder(final String to) {
            this.to = to;
        }

        public Builder from(final String value) {
            this.from = value;
            return this;
        }

        public Builder subject(final String value) {
            this.subject = value;
            return this;
        }

        public Builder variables(final Map<String, Object> value) {
            this.variables = Copies.map(value);
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public TestSendTemplateInput build() {
            return new TestSendTemplateInput(to, from, subject, variables);
        }
    }
}
