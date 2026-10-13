package br.com.coffeemail.models.audiences;

/** Contato rejeitado na inserção em lote. */
public record BulkAddContactError(String email, String reason) {}
