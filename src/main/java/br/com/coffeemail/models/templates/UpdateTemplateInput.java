package br.com.coffeemail.models.templates;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Atualização parcial de um template. Apenas os campos definidos no builder são enviados; os métodos {@code clear*} enviam
 * {@code null} para limpar o valor.
 *
 * @param fields campos que serão enviados à API
 */
public record UpdateTemplateInput(@JsonValue Map<String, Object> fields) {

    private static final String NAME = "name";
    private static final String ALIAS = "alias";
    private static final String SUBJECT = "subject";
    private static final String PREHEADER = "preheader";
    private static final String HTML = "html";
    private static final String CONTENT_JSON = "contentJson";
    private static final String TEXT_PAYLOAD = "textPayload";
    private static final String VARIABLES = "variables";
    private static final String FORMAT = "format";
    private static final String IS_ACTIVE = "isActive";
    private static final String SOURCE_LOCALE = "sourceLocale";

    /**
     * Copia os campos para um mapa imutável que preserva a ordem e valores nulos.
     *
     * @param fields campos que serão enviados à API
     */
    public UpdateTemplateInput {
        fields = Collections.unmodifiableMap(new LinkedHashMap<>(fields));
    }

    /**
     * Inicia a construção.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder de {@link UpdateTemplateInput}. */
    public static final class Builder {
        private final Map<String, Object> fields = new LinkedHashMap<>();

        private Builder() {}

        public Builder name(final String value) {
            fields.put(NAME, value);
            return this;
        }

        public Builder alias(final String value) {
            fields.put(ALIAS, value);
            return this;
        }

        public Builder clearAlias() {
            fields.put(ALIAS, null);
            return this;
        }

        public Builder subject(final String value) {
            fields.put(SUBJECT, value);
            return this;
        }

        public Builder clearSubject() {
            fields.put(SUBJECT, null);
            return this;
        }

        public Builder preheader(final String value) {
            fields.put(PREHEADER, value);
            return this;
        }

        public Builder clearPreheader() {
            fields.put(PREHEADER, null);
            return this;
        }

        public Builder html(final String value) {
            fields.put(HTML, value);
            return this;
        }

        public Builder contentJson(final Map<String, Object> value) {
            fields.put(CONTENT_JSON, value);
            return this;
        }

        public Builder clearContentJson() {
            fields.put(CONTENT_JSON, null);
            return this;
        }

        public Builder textPayload(final String value) {
            fields.put(TEXT_PAYLOAD, value);
            return this;
        }

        public Builder clearTextPayload() {
            fields.put(TEXT_PAYLOAD, null);
            return this;
        }

        public Builder variables(final List<TemplateVariable> value) {
            fields.put(VARIABLES, List.copyOf(value));
            return this;
        }

        public Builder format(final TemplateFormat value) {
            fields.put(FORMAT, value);
            return this;
        }

        public Builder isActive(final Boolean value) {
            fields.put(IS_ACTIVE, value);
            return this;
        }

        public Builder sourceLocale(final String value) {
            fields.put(SOURCE_LOCALE, value);
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public UpdateTemplateInput build() {
            return new UpdateTemplateInput(fields);
        }
    }
}
