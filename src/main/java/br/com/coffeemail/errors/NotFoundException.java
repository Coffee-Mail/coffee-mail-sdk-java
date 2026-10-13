package br.com.coffeemail.errors;

import br.com.coffeemail.internal.constants.HttpStatus;

/** Recurso não encontrado (HTTP 404). */
public final class NotFoundException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria o erro.
     *
     * @param error detalhes do erro
     */
    public NotFoundException(final ApiErrorDetails error) {
        super(HttpStatus.NOT_FOUND, ErrorCodes.NOT_FOUND, error);
    }
}
