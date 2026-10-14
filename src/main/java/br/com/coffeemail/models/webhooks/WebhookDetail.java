package br.com.coffeemail.models.webhooks;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Detalhes de um webhook; o segredo completo só aparece na criação e na rotação. */
public record WebhookDetail(
        String id,
        String url,
        List<WebhookEventType> events,
        WebhookStatus status,
        Optional<String> description,
        Instant createdAt,
        Optional<String> secret,
        Optional<String> secretPreview) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public WebhookDetail {
        events = Copies.list(events);
        description = Copies.optional(description);
        secret = Copies.optional(secret);
        secretPreview = Copies.optional(secretPreview);
    }
}
