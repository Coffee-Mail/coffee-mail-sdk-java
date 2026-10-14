package br.com.coffeemail.models.webhooks;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Webhook recém-criado; é a única resposta que expõe o segredo completo. */
public record CreatedWebhookDetail(
        String id,
        String url,
        List<WebhookEventType> events,
        WebhookStatus status,
        Optional<String> description,
        Instant createdAt,
        String secret) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public CreatedWebhookDetail {
        events = Copies.list(events);
        description = Copies.optional(description);
    }
}
