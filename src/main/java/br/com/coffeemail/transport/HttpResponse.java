package br.com.coffeemail.transport;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Resposta HTTP devolvida pelo {@link HttpTransport}.
 *
 * @param status status HTTP
 * @param headers cabeçalhos, com nomes normalizados para minúsculas
 * @param body corpo bruto, vazio quando não houver
 */
public record HttpResponse(int status, Map<String, String> headers, String body) {

    /**
     * Lê um cabeçalho sem diferenciar maiúsculas de minúsculas.
     *
     * @param name nome do cabeçalho
     * @return o valor, quando presente
     */
    public Optional<String> header(final String name) {
        return Optional.ofNullable(headers.get(name.toLowerCase(Locale.ROOT)));
    }

    /**
     * Normaliza os nomes dos cabeçalhos para minúsculas.
     *
     * @param status status HTTP
     * @param headers cabeçalhos
     * @param body corpo bruto
     */
    public HttpResponse {
        headers = headers.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> entry.getKey().toLowerCase(Locale.ROOT),
                        Map.Entry::getValue,
                        (first, second) -> first));
    }
}
