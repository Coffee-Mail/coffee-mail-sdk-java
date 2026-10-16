package br.com.coffeemail.models.domains;

import java.time.Instant;

/**
 * Rotação de DKIM pendente. A chave em uso só muda quando o registro TXT do novo seletor é encontrado no DNS.
 *
 * @param domainId identificador do domínio
 * @param selector seletor DKIM pendente, que passa a valer após a propagação do DNS
 * @param startedAt quando a rotação foi iniciada
 * @param record registro TXT a publicar para concluir a rotação
 */
public record DkimRotation(String domainId, String selector, Instant startedAt, DomainDnsRecord record) {}
