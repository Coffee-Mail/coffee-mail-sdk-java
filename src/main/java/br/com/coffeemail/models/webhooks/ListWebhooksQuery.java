package br.com.coffeemail.models.webhooks;

import org.jspecify.annotations.Nullable;

/** Filtros da listagem de webhooks. */
public record ListWebhooksQuery(@Nullable WebhookStatus status) {

    /**
     * Inicia a construção.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder de {@link ListWebhooksQuery}. */
    public static final class Builder {
        private @Nullable WebhookStatus status;

        private Builder() {}

        public Builder status(final WebhookStatus value) {
            this.status = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public ListWebhooksQuery build() {
            return new ListWebhooksQuery(status);
        }
    }
}
