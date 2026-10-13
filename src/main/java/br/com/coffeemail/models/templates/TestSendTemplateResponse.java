package br.com.coffeemail.models.templates;

/** Resultado do envio de teste. */
public record TestSendTemplateResponse(boolean success, String messageId, String recipient) {}
