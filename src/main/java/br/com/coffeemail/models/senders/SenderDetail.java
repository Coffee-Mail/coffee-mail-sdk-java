package br.com.coffeemail.models.senders;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/** Remetente verificado por endereço. */
public record SenderDetail(
        String id,
        String email,
        Optional<String> displayName,
        Optional<String> verificationToken,
        Optional<Instant> verifiedAt,
        boolean active,
        Instant createdAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public SenderDetail {
        displayName = Copies.optional(displayName);
        verificationToken = Copies.optional(verificationToken);
        verifiedAt = Copies.optional(verifiedAt);
    }
}
