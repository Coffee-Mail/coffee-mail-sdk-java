package br.com.coffeemail.models.webhooks;

/** Novo segredo do webhook, exibido uma única vez. */
public record RotateWebhookSecretResult(String webhookId, String secret) {}
