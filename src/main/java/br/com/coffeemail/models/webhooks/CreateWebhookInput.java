package br.com.coffeemail.models.webhooks;

import java.util.List;
import org.jspecify.annotations.Nullable;

/** Dados para cadastrar um webhook. */
public record CreateWebhookInput(
        String url, List<WebhookEventType> events, @Nullable String description) {

    /**
     * Inicia a construção.
     *
     * @param url campo obrigatório
     * @param events campo obrigatório
     * @return o builder
     */
    public static Builder builder(final String url, final List<WebhookEventType> events) {
        return new Builder(url, events);
    }

    /** Builder de {@link CreateWebhookInput}. */
    public static final class Builder {
        private final String url;
        private final List<WebhookEventType> events;
        private @Nullable String description;

        private Builder(final String url, final List<WebhookEventType> events) {
            this.url = url;
            this.events = events;
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
        public CreateWebhookInput build() {
            return new CreateWebhookInput(url, events, description);
        }
    }
}
