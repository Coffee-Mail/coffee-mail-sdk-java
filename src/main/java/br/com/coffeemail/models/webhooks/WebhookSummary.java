package br.com.coffeemail.models.webhooks;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Resumo de um webhook, sem o segredo. */
public record WebhookSummary(
        String id,
        String url,
        List<WebhookEventType> events,
        WebhookStatus status,
        Optional<String> description,
        boolean hasSecret,
        Instant createdAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public WebhookSummary {
        events = Copies.list(events);
        description = Copies.optional(description);
    }
}
