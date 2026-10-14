package br.com.coffeemail.models.broadcasts;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/** Resultado do cancelamento de uma campanha. */
public record CancelBroadcastResult(String id, BroadcastStatus status, Optional<Instant> cancelledAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public CancelBroadcastResult {
        cancelledAt = Copies.optional(cancelledAt);
    }
}
