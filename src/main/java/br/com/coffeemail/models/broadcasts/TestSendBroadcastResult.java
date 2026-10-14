package br.com.coffeemail.models.broadcasts;

/** Resultado do envio de teste de uma campanha. */
public record TestSendBroadcastResult(boolean success, String messageId, String recipient) {}
