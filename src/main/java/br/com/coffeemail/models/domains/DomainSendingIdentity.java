package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/** Situação do domínio no provedor de envio. */
public record DomainSendingIdentity(
        SendingProvider provider, SendingIdentityState state, Optional<Instant> lastSyncedAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainSendingIdentity {
        lastSyncedAt = Copies.optional(lastSyncedAt);
    }
}
