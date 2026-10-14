package br.com.coffeemail.models.webhooks;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;

/** Resultado do envio de teste para o endpoint. */
public record TestWebhookResult(
        WebhookTestStatus status, Optional<Integer> statusCode, Optional<String> body, Optional<String> error) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public TestWebhookResult {
        statusCode = Copies.optional(statusCode);
        body = Copies.optional(body);
        error = Copies.optional(error);
    }
}
