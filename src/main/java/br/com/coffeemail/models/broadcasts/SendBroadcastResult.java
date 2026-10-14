package br.com.coffeemail.models.broadcasts;

/** Resultado do disparo de uma campanha. */
public record SendBroadcastResult(String id, BroadcastStatus status, int recipientsTotal, int recipientsQueued) {}
