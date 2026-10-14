package br.com.coffeemail.models.webhooks;

/** Nova situação do webhook. */
public record ToggleWebhookResult(String id, ToggleableWebhookStatus status) {}
