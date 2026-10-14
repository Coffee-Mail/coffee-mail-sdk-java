package br.com.coffeemail.models.webhooks;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Atualização parcial de um webhook. Apenas os campos definidos no builder são enviados; os métodos {@code clear*} enviam
 * {@code null} para limpar o valor.
 *
 * @param fields campos que serão enviados à API
 */
public record UpdateWebhookInput(@JsonValue Map<String, Object> fields) {

    private static final String URL = "url";
    private static final String EVENTS = "events";
    private static final String DESCRIPTION = "description";

    /**
     * Copia os campos para um mapa imutável que preserva a ordem e valores nulos.
     *
     * @param fields campos que serão enviados à API
     */
    public UpdateWebhookInput {
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

    /** Builder de {@link UpdateWebhookInput}. */
    public static final class Builder {
        private final Map<String, Object> fields = new LinkedHashMap<>();

        private Builder() {}

        public Builder url(final String value) {
            fields.put(URL, value);
            return this;
        }

        public Builder events(final List<WebhookEventType> value) {
            fields.put(EVENTS, List.copyOf(value));
            return this;
        }

        public Builder description(final String value) {
            fields.put(DESCRIPTION, value);
            return this;
        }

        public Builder clearDescription() {
            fields.put(DESCRIPTION, null);
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public UpdateWebhookInput build() {
            return new UpdateWebhookInput(fields);
        }
    }
}
