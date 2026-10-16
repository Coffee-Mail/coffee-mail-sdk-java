package br.com.coffeemail.models.webhooks;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/**
 * Tentativa de entrega de um evento, com o corpo enviado ao endpoint.
 *
 * @param id identificador da entrega
 * @param webhookId identificador do webhook
 * @param emailEventId evento de e-mail que originou a entrega
 * @param eventType tipo de evento, como {@code email.delivered}
 * @param responseStatus status HTTP devolvido pelo endpoint
 * @param responseBody corpo devolvido pelo endpoint
 * @param attempts tentativas já realizadas
 * @param lastError último erro ocorrido no envio
 * @param status situação atual da entrega
 * @param nextRunAt próxima tentativa
 * @param createdAt criação
 * @param requestPayload corpo JSON enviado ao endpoint, como texto
 */
public record WebhookDeliveryDetail(
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
        Instant createdAt,
        String requestPayload) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public WebhookDeliveryDetail {
        emailEventId = Copies.optional(emailEventId);
        responseStatus = Copies.optional(responseStatus);
        responseBody = Copies.optional(responseBody);
        lastError = Copies.optional(lastError);
    }

    /**
     * A mesma entrega sem o corpo enviado, no formato da listagem.
     *
     * @return a entrega
     */
    public WebhookDelivery delivery() {
        return new WebhookDelivery(
                id,
                webhookId,
                emailEventId,
                eventType,
                responseStatus,
                responseBody,
                attempts,
                lastError,
                status,
                nextRunAt,
                createdAt);
    }
}
