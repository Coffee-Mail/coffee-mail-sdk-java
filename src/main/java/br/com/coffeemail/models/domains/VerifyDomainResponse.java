package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Resultado da verificação DNS de um domínio. */
public record VerifyDomainResponse(
        String id,
        String name,
        DomainStatus status,
        Optional<DomainStatusReason> statusReason,
        Optional<DomainSendingIdentity> sendingIdentity,
        Optional<Instant> verifiedAt,
        DomainChecks checks,
        List<DomainCheck> requiredChecks) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public VerifyDomainResponse {
        statusReason = Copies.optional(statusReason);
        sendingIdentity = Copies.optional(sendingIdentity);
        verifiedAt = Copies.optional(verifiedAt);
        requiredChecks = Copies.list(requiredChecks);
    }
}
