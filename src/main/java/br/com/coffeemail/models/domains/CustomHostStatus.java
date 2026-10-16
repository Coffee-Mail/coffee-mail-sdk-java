package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/**
 * Host próprio do cliente (return-path ou tracking) que aponta, por CNAME, para a plataforma.
 *
 * @param host host do domínio do cliente
 * @param target host da plataforma para onde o CNAME deve apontar
 * @param verified indica se o CNAME foi encontrado apontando para o alvo
 * @param verifiedAt quando o CNAME foi confirmado
 * @param record registro CNAME a publicar
 */
public record CustomHostStatus(
        String host, String target, boolean verified, Optional<Instant> verifiedAt, DomainDnsRecord record) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public CustomHostStatus {
        verifiedAt = Copies.optional(verifiedAt);
    }
}
