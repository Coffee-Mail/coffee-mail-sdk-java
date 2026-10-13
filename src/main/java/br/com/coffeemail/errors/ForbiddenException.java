package br.com.coffeemail.errors;

import br.com.coffeemail.internal.constants.HttpStatus;

/** A chave de API não tem acesso ao recurso (HTTP 403). */
public final class ForbiddenException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria o erro.
     *
     * @param error detalhes do erro
     */
    public ForbiddenException(final ApiErrorDetails error) {
        super(HttpStatus.FORBIDDEN, ErrorCodes.FORBIDDEN, error);
    }
}
