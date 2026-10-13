package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.util.List;
import java.util.Optional;

/** Resultado de uma verificação DNS. */
public record DomainDnsCheck(
        boolean ok, Optional<DnsResolution> resolution, String expected, Optional<String> got, List<String> matches) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainDnsCheck {
        resolution = Copies.optional(resolution);
        got = Copies.optional(got);
        matches = Copies.list(matches);
    }
}
