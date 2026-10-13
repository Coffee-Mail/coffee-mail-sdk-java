package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Tags distintas usadas pela organização. */
public record ListEmailTagsResponse(List<EmailTagSummary> tags) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListEmailTagsResponse {
        tags = Copies.list(tags);
    }
}
