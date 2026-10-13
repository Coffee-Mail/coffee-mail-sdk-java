package br.com.coffeemail.models.templates;

import br.com.coffeemail.models.support.Copies;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** Renderização com sanitização do HTML resultante. */
public record TestRenderTemplateInput(
        String html,
        @Nullable TemplateFormat format,
        @Nullable Map<String, Object> variables) {

    /**
     * Inicia a construção.
     *
     * @param html campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String html) {
        return new Builder(html);
    }

    /** Builder de {@link TestRenderTemplateInput}. */
    public static final class Builder {
        private final String html;
        private @Nullable TemplateFormat format;
        private @Nullable Map<String, Object> variables;

        private Builder(final String html) {
            this.html = html;
        }

        public Builder format(final TemplateFormat value) {
            this.format = value;
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
        public TestRenderTemplateInput build() {
            return new TestRenderTemplateInput(html, format, variables);
        }
    }
}
