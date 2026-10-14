package br.com.coffeemail.models.webhooks;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Webhooks da organização. */
public record ListWebhooksResponse(List<WebhookSummary> webhooks) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListWebhooksResponse {
        webhooks = Copies.list(webhooks);
    }
}
