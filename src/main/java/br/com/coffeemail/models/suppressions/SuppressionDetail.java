package br.com.coffeemail.models.suppressions;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/** Endereço que não deve receber e-mails. */
public record SuppressionDetail(
        String id,
        String email,
        SuppressionReason reason,
        SuppressionSource source,
        String category,
        SuppressionStatus status,
        Optional<Instant> expiresAt,
        Instant createdAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public SuppressionDetail {
        expiresAt = Copies.optional(expiresAt);
    }
}
