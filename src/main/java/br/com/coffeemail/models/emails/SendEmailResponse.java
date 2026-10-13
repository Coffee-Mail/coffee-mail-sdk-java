package br.com.coffeemail.models.emails;

import java.time.Instant;

/** Resposta do enfileiramento de um e-mail. */
public record SendEmailResponse(String id, SendEmailStatus status, Instant queuedAt) {}
