package br.com.coffeemail.models.domains;

import java.time.Instant;

/** Diagnóstico de saúde e entregabilidade do domínio. */
public record DomainHealthResponse(
        String domainId,
        String domain,
        DomainHealthStatus status,
        DomainHealthCheck spf,
        DomainHealthCheck dkim,
        DomainHealthCheck dmarc,
        Instant checkedAt) {}
