package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Domínios da organização. */
public record ListDomainsResponse(List<DomainSummary> domains) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListDomainsResponse {
        domains = Copies.list(domains);
    }
}
