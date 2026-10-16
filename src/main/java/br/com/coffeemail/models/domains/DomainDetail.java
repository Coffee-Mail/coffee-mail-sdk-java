package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Detalhe completo de um domínio.
 *
 * <p>{@link #dkimRotation()} traz a rotação de DKIM pendente, {@link #returnPath()} o return-path próprio e
 * {@link #trackingDomain()} o host de tracking próprio; os três ficam vazios quando não se aplicam.
 * {@link #trackingDomainsAvailable()} indica se a plataforma permite cadastrar host de tracking.
 */
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
        List<DomainDnsRecord> dnsRecordsToPublish,
        Optional<DkimRotation> dkimRotation,
        Optional<CustomHostStatus> returnPath,
        Optional<CustomHostStatus> trackingDomain,
        Optional<Boolean> trackingDomainsAvailable) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainDetail {
        statusReason = Copies.optional(statusReason);
        sendingIdentity = Copies.optional(sendingIdentity);
        dkimPublicKey = Copies.optional(dkimPublicKey);
        verifiedAt = Copies.optional(verifiedAt);
        lastCheckAt = Copies.optional(lastCheckAt);
        dnsRecordsToPublish = Copies.list(dnsRecordsToPublish);
        dkimRotation = Copies.optional(dkimRotation);
        returnPath = Copies.optional(returnPath);
        trackingDomain = Copies.optional(trackingDomain);
        trackingDomainsAvailable = Copies.optional(trackingDomainsAvailable);
    }

    /**
     * Construtor sem rotação de DKIM, return-path e tracking, mantido por compatibilidade.
     *
     * @param id identificador
     * @param name domínio
     * @param status status
     * @param statusReason motivo do status
     * @param sendingIdentity situação no provedor de envio
     * @param dkimSelector seletor DKIM
     * @param dkimPublicKey chave pública DKIM
     * @param verifiedAt verificação
     * @param lastCheckAt última checagem
     * @param createdAt criação
     * @param dnsRecordsToPublish registros DNS a publicar
     */
    public DomainDetail(
            final String id,
            final String name,
            final DomainStatus status,
            final Optional<DomainStatusReason> statusReason,
            final Optional<DomainSendingIdentity> sendingIdentity,
            final String dkimSelector,
            final Optional<String> dkimPublicKey,
            final Optional<Instant> verifiedAt,
            final Optional<Instant> lastCheckAt,
            final Instant createdAt,
            final List<DomainDnsRecord> dnsRecordsToPublish) {
        this(
                id,
                name,
                status,
                statusReason,
                sendingIdentity,
                dkimSelector,
                dkimPublicKey,
                verifiedAt,
                lastCheckAt,
                createdAt,
                dnsRecordsToPublish,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
}
