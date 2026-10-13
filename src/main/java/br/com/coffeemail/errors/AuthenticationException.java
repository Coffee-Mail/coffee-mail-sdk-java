package br.com.coffeemail.errors;

import br.com.coffeemail.internal.constants.HttpStatus;

/** Chave de API ausente, inválida ou expirada (HTTP 401). */
public final class AuthenticationException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria o erro.
     *
     * @param error detalhes do erro
     */
    public AuthenticationException(final ApiErrorDetails error) {
        super(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED, error);
    }
}
