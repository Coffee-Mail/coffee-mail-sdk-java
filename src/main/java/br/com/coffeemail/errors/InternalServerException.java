package br.com.coffeemail.errors;

/** Erro do servidor da API (HTTP 500, 502, 503 ou 504). */
public final class InternalServerException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria o erro preservando o status recebido.
     *
     * @param status status HTTP 5xx recebido
     * @param error detalhes do erro
     */
    public InternalServerException(final int status, final ApiErrorDetails error) {
        super(status, ErrorCodes.INTERNAL_SERVER_ERROR, error);
    }
}
