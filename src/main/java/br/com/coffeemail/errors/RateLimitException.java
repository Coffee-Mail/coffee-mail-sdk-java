package br.com.coffeemail.errors;

import br.com.coffeemail.internal.constants.HttpStatus;
import java.util.OptionalLong;
import org.jspecify.annotations.Nullable;

/** Limite de requisições excedido (HTTP 429). */
public final class RateLimitException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    private final @Nullable Long retryAfterSeconds;

    /**
     * Cria o erro.
     *
     * @param error detalhes do erro
     * @param retryAfterSeconds segundos indicados no cabeçalho {@code Retry-After}, quando houver
     */
    public RateLimitException(final ApiErrorDetails error, final @Nullable Long retryAfterSeconds) {
        super(HttpStatus.TOO_MANY_REQUESTS, ErrorCodes.RATE_LIMIT_EXCEEDED, error);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    /**
     * Segundos a aguardar antes de tentar novamente, conforme o cabeçalho {@code Retry-After}.
     *
     * @return os segundos, quando o servidor informar em formato numérico
     */
    public OptionalLong retryAfterSeconds() {
        final Long seconds = retryAfterSeconds;
        return seconds == null ? OptionalLong.empty() : OptionalLong.of(seconds);
    }
}
