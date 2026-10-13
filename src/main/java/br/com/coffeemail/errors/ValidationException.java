package br.com.coffeemail.errors;

import br.com.coffeemail.internal.constants.HttpStatus;

/** Payload ou parâmetros rejeitados pela validação da API (HTTP 400). */
public final class ValidationException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria o erro.
     *
     * @param error detalhes do erro
     */
    public ValidationException(final ApiErrorDetails error) {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.VALIDATION_ERROR, error);
    }
}
