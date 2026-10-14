package br.com.coffeemail.models.webhooks;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/** Tentativa de entrega de um evento. */
public record WebhookDelivery(
        String id,
        String webhookId,
        Optional<String> emailEventId,
        String eventType,
        Optional<Integer> responseStatus,
        Optional<String> responseBody,
        int attempts,
        Optional<String> lastError,
        WebhookDeliveryStatus status,
        Instant nextRunAt,
        Instant createdAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public WebhookDelivery {
        emailEventId = Copies.optional(emailEventId);
        responseStatus = Copies.optional(responseStatus);
        responseBody = Copies.optional(responseBody);
        lastError = Copies.optional(lastError);
    }
}
