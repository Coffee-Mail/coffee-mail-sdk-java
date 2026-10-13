package br.com.coffeemail.errors;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Informações de um erro retornado pela API.
 *
 * @param message mensagem legível, traduzida conforme o locale
 * @param apiCode código original enviado pelo servidor, quando houver
 * @param details metadados do erro ({@code error.meta} e {@code error.details})
 * @param requestId identificador da requisição ({@code x-request-id}), quando houver
 */
public record ApiErrorDetails(
        String message,
        @Nullable String apiCode,
        Map<String, Object> details,
        @Nullable String requestId) {

    /**
     * Normaliza os metadados para uma cópia imutável.
     *
     * @param message mensagem legível
     * @param apiCode código original do servidor
     * @param details metadados do erro
     * @param requestId identificador da requisição
     */
    public ApiErrorDetails {
        details = Collections.unmodifiableMap(new LinkedHashMap<>(details));
    }

    /**
     * Cria detalhes contendo apenas a mensagem.
     *
     * @param message mensagem legível
     * @return os detalhes do erro
     */
    public static ApiErrorDetails of(final String message) {
        return new ApiErrorDetails(message, null, Map.of(), null);
    }
}
