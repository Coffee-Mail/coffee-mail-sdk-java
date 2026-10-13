package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;

/** Verificações DNS de um domínio. */
public record DomainChecks(
        DomainDnsCheck spf,
        DomainDnsCheck dkim,
        DomainDnsCheck dmarc,
        DomainDnsCheck ownership,
        Optional<DomainDnsCheck> infrastructureSpf) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainChecks {
        infrastructureSpf = Copies.optional(infrastructureSpf);
    }
}
