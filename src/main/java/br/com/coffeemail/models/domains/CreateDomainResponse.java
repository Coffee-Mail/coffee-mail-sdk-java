package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Domínio recém-criado e os registros DKIM a publicar. */
public record CreateDomainResponse(
        String id,
        String name,
        DomainStatus status,
        List<DomainDnsRecord> dkimRecordsToPublish,
        Optional<Instant> createdAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public CreateDomainResponse {
        dkimRecordsToPublish = Copies.list(dkimRecordsToPublish);
        createdAt = Copies.optional(createdAt);
    }
}
