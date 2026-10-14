package br.com.coffeemail.models.suppressions;

import br.com.coffeemail.models.support.Copies;
import java.util.List;
import java.util.Optional;

/** Página de supressões. */
public record ListSuppressionsResponse(List<SuppressionDetail> suppressions, Optional<String> nextCursor) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListSuppressionsResponse {
        suppressions = Copies.list(suppressions);
        nextCursor = Copies.optional(nextCursor);
    }
}
