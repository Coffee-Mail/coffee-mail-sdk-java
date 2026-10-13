package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/** Resumo de um domínio, como retornado pela listagem. */
public record DomainSummary(
        String id,
        String name,
        DomainStatus status,
        Optional<DomainStatusReason> statusReason,
        String dkimSelector,
        Optional<String> dkimPublicKey,
        Optional<Instant> verifiedAt,
        Optional<Instant> lastCheckAt,
        Instant createdAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainSummary {
        statusReason = Copies.optional(statusReason);
        dkimPublicKey = Copies.optional(dkimPublicKey);
        verifiedAt = Copies.optional(verifiedAt);
        lastCheckAt = Copies.optional(lastCheckAt);
    }
}
