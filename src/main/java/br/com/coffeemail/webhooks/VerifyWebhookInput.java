package br.com.coffeemail.webhooks;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * Dados de uma entrega de webhook a verificar.
 *
 * @param payload corpo bruto da requisição, exatamente como recebido (não o JSON reserializado)
 * @param signature valor do cabeçalho {@value WebhookHeaders#SIGNATURE}
 * @param timestamp valor do cabeçalho {@value WebhookHeaders#TIMESTAMP}
 * @param idempotencyKey valor do cabeçalho {@value WebhookHeaders#IDEMPOTENCY_KEY}
 * @param secret segredo do webhook ({@code whsec_...})
 */
public record VerifyWebhookInput(
        String payload, String signature, String timestamp, String idempotencyKey, String secret) {

    /**
     * Monta a entrada a partir do corpo bruto e dos cabeçalhos HTTP, sem diferenciar maiúsculas de minúsculas.
     *
     * @param payload corpo bruto da requisição
     * @param headers cabeçalhos HTTP recebidos
     * @param secret segredo do webhook
     * @return a entrada; cabeçalhos ausentes resultam em verificação inválida
     */
    public static VerifyWebhookInput fromHeaders(
            final String payload, final Map<String, String> headers, final String secret) {
        return new VerifyWebhookInput(
                payload,
                header(headers, WebhookHeaders.SIGNATURE),
                header(headers, WebhookHeaders.TIMESTAMP),
                header(headers, WebhookHeaders.IDEMPOTENCY_KEY),
                secret);
    }

    /**
     * Monta a entrada a partir do corpo bruto em bytes (UTF-8) e dos cabeçalhos HTTP.
     *
     * @param payload corpo bruto da requisição
     * @param headers cabeçalhos HTTP recebidos
     * @param secret segredo do webhook
     * @return a entrada; cabeçalhos ausentes resultam em verificação inválida
     */
    public static VerifyWebhookInput fromHeaders(
            final byte[] payload, final Map<String, String> headers, final String secret) {
        return fromHeaders(new String(payload, StandardCharsets.UTF_8), headers, secret);
    }

    private static String header(final Map<String, String> headers, final String name) {
        return headers.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase(Locale.ROOT).equals(name))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("");
    }
}
