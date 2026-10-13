package br.com.coffeemail.errors;

import br.com.coffeemail.internal.constants.HttpStatus;

/** Cota do plano esgotada ou upgrade necessário (HTTP 402). */
public final class PaymentRequiredException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria o erro.
     *
     * @param error detalhes do erro
     */
    public PaymentRequiredException(final ApiErrorDetails error) {
        super(HttpStatus.PAYMENT_REQUIRED, ErrorCodes.PAYMENT_REQUIRED, error);
    }
}
