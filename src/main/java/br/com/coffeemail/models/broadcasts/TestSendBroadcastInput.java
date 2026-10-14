package br.com.coffeemail.models.broadcasts;

import br.com.coffeemail.models.support.Copies;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** Envio de teste de uma campanha. */
public record TestSendBroadcastInput(String to, @Nullable Map<String, Object> variables) {

    /**
     * Inicia a construção.
     *
     * @param to campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String to) {
        return new Builder(to);
    }

    /** Builder de {@link TestSendBroadcastInput}. */
    public static final class Builder {
        private final String to;
        private @Nullable Map<String, Object> variables;

        private Builder(final String to) {
            this.to = to;
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
        public TestSendBroadcastInput build() {
            return new TestSendBroadcastInput(to, variables);
        }
    }
}
