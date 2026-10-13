package br.com.coffeemail.errors;

import br.com.coffeemail.internal.constants.HttpStatus;

/** Conflito com um recurso existente, como domínio duplicado (HTTP 409). */
public final class ConflictException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria o erro.
     *
     * @param error detalhes do erro
     */
    public ConflictException(final ApiErrorDetails error) {
        super(HttpStatus.CONFLICT, ErrorCodes.CONFLICT, error);
    }
}
