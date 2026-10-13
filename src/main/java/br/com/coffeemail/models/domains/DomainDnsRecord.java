package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;

/** Registro DNS a publicar no provedor. */
public record DomainDnsRecord(
        DnsRecordType type, String host, String value, Optional<Integer> ttl, Optional<Boolean> isInfrastructure) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainDnsRecord {
        ttl = Copies.optional(ttl);
        isInfrastructure = Copies.optional(isInfrastructure);
    }
}
