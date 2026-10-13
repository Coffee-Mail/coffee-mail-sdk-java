package br.com.coffeemail.models.audiences;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Página de audiências. */
public record ListAudiencesResponse(List<AudienceDetail> audiences, int total) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListAudiencesResponse {
        audiences = Copies.list(audiences);
    }
}
