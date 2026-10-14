package br.com.coffeemail.models.broadcasts;

import br.com.coffeemail.models.support.Copies;
import java.util.List;
import java.util.Optional;

/** Página de campanhas. */
public record ListBroadcastsResponse(List<BroadcastDetail> broadcasts, Optional<String> nextCursor) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListBroadcastsResponse {
        broadcasts = Copies.list(broadcasts);
        nextCursor = Copies.optional(nextCursor);
    }
}
