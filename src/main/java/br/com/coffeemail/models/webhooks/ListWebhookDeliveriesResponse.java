package br.com.coffeemail.models.webhooks;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Entregas de um webhook. */
public record ListWebhookDeliveriesResponse(List<WebhookDelivery> deliveries) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListWebhookDeliveriesResponse {
        deliveries = Copies.list(deliveries);
    }
}
