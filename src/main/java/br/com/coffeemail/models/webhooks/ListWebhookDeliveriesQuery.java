package br.com.coffeemail.models.webhooks;

import java.time.Instant;
import org.jspecify.annotations.Nullable;

/** Filtros da listagem de entregas. */
public record ListWebhookDeliveriesQuery(
        @Nullable String eventType,
        @Nullable WebhookDeliveryStatus status,
        @Nullable Instant from,
        @Nullable Instant to,
        @Nullable Integer limit) {

    /**
     * Inicia a construção.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder de {@link ListWebhookDeliveriesQuery}. */
    public static final class Builder {
        private @Nullable String eventType;
        private @Nullable WebhookDeliveryStatus status;
        private @Nullable Instant from;
        private @Nullable Instant to;
        private @Nullable Integer limit;

        private Builder() {}

        public Builder eventType(final String value) {
            this.eventType = value;
            return this;
        }

        public Builder status(final WebhookDeliveryStatus value) {
            this.status = value;
            return this;
        }

        public Builder from(final Instant value) {
            this.from = value;
            return this;
        }

        public Builder to(final Instant value) {
            this.to = value;
            return this;
        }

        public Builder limit(final Integer value) {
            this.limit = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public ListWebhookDeliveriesQuery build() {
            return new ListWebhookDeliveriesQuery(eventType, status, from, to, limit);
        }
    }
}
