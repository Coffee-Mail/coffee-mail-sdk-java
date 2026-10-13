package br.com.coffeemail.errors;

import br.com.coffeemail.internal.constants.HttpStatus;
import org.jspecify.annotations.Nullable;

/** Falha de rede, DNS ou tempo limite excedido; não houve resposta da API. */
public final class NetworkException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria o erro.
     *
     * @param message mensagem legível
     * @param cause causa original, quando houver
     */
    public NetworkException(final String message, final @Nullable Throwable cause) {
        super(HttpStatus.NONE, ErrorCodes.NETWORK_ERROR, ApiErrorDetails.of(message), cause);
    }
}
