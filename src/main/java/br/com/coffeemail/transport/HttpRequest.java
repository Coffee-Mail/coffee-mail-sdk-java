package br.com.coffeemail.transport;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * Requisição HTTP montada pelo SDK e entregue ao {@link HttpTransport}.
 *
 * @param method método HTTP
 * @param uri URI completa, com query string
 * @param headers cabeçalhos em minúsculas
 * @param body corpo JSON, quando houver
 */
public record HttpRequest(HttpMethod method, URI uri, Map<String, String> headers, Optional<String> body) {

    /**
     * Normaliza os cabeçalhos para uma cópia imutável.
     *
     * @param method método HTTP
     * @param uri URI completa
     * @param headers cabeçalhos
     * @param body corpo JSON
     */
    public HttpRequest {
        headers = Map.copyOf(headers);
    }
}
