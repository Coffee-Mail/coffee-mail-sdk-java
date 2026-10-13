package br.com.coffeemail.models.templates;

import org.jspecify.annotations.Nullable;

/** Formatação do código-fonte de um template. */
public record FormatTemplateInput(String html, @Nullable TemplateFormat format) {

    /**
     * Inicia a construção.
     *
     * @param html campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String html) {
        return new Builder(html);
    }

    /** Builder de {@link FormatTemplateInput}. */
    public static final class Builder {
        private final String html;
        private @Nullable TemplateFormat format;

        private Builder(final String html) {
            this.html = html;
        }

        public Builder format(final TemplateFormat value) {
            this.format = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public FormatTemplateInput build() {
            return new FormatTemplateInput(html, format);
        }
    }
}
