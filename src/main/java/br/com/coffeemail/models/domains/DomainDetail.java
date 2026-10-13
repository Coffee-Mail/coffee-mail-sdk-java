package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Detalhe completo de um domínio. */
public record DomainDetail(
        String id,
        String name,
        DomainStatus status,
        Optional<DomainStatusReason> statusReason,
        Optional<DomainSendingIdentity> sendingIdentity,
        String dkimSelector,
        Optional<String> dkimPublicKey,
        Optional<Instant> verifiedAt,
        Optional<Instant> lastCheckAt,
        Instant createdAt,
        List<DomainDnsRecord> dnsRecordsToPublish) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainDetail {
        statusReason = Copies.optional(statusReason);
        sendingIdentity = Copies.optional(sendingIdentity);
        dkimPublicKey = Copies.optional(dkimPublicKey);
        verifiedAt = Copies.optional(verifiedAt);
        lastCheckAt = Copies.optional(lastCheckAt);
        dnsRecordsToPublish = Copies.list(dnsRecordsToPublish);
    }
}
