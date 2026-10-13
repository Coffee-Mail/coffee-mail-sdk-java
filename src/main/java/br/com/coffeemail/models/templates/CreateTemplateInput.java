package br.com.coffeemail.models.templates;

import br.com.coffeemail.models.support.Copies;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** Dados para criar um template. */
public record CreateTemplateInput(
        String name,
        String html,
        @Nullable String alias,
        @Nullable String subject,
        @Nullable String preheader,
        @Nullable Map<String, Object> contentJson,
        @Nullable String textPayload,
        @Nullable List<TemplateVariable> variables,
        @Nullable TemplateFormat format,
        @Nullable String sourceLocale) {

    /**
     * Inicia a construção.
     *
     * @param name campo obrigatório
     * @param html campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String name, final String html) {
        return new Builder(name, html);
    }

    /** Builder de {@link CreateTemplateInput}. */
    public static final class Builder {
        private final String name;
        private final String html;
        private @Nullable String alias;
        private @Nullable String subject;
        private @Nullable String preheader;
        private @Nullable Map<String, Object> contentJson;
        private @Nullable String textPayload;
        private @Nullable List<TemplateVariable> variables;
        private @Nullable TemplateFormat format;
        private @Nullable String sourceLocale;

        private Builder(final String name, final String html) {
            this.name = name;
            this.html = html;
        }

        public Builder alias(final String value) {
            this.alias = value;
            return this;
        }

        public Builder subject(final String value) {
            this.subject = value;
            return this;
        }

        public Builder preheader(final String value) {
            this.preheader = value;
            return this;
        }

        public Builder contentJson(final Map<String, Object> value) {
            this.contentJson = Copies.map(value);
            return this;
        }

        public Builder textPayload(final String value) {
            this.textPayload = value;
            return this;
        }

        public Builder variables(final List<TemplateVariable> value) {
            this.variables = List.copyOf(value);
            return this;
        }

        public Builder format(final TemplateFormat value) {
            this.format = value;
            return this;
        }

        public Builder sourceLocale(final String value) {
            this.sourceLocale = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public CreateTemplateInput build() {
            return new CreateTemplateInput(
                    name, html, alias, subject, preheader, contentJson, textPayload, variables, format, sourceLocale);
        }
    }
}
