package br.com.coffeemail.errors;

import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

/** Erro base de todas as falhas do SDK. */
public class CoffeeMailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int status;
    private final String code;
    private final @Nullable String apiCode;
    private final transient Map<String, Object> details;
    private final @Nullable String requestId;

    /**
     * Cria um erro para um status HTTP sem classe específica.
     *
     * @param status status HTTP, ou 0 para erros sem resposta
     * @param code código canônico
     * @param error detalhes do erro
     */
    public CoffeeMailException(final int status, final String code, final ApiErrorDetails error) {
        this(status, code, error, null);
    }

    /**
     * Cria um erro preservando a causa original.
     *
     * @param status status HTTP, ou 0 para erros sem resposta
     * @param code código canônico
     * @param error detalhes do erro
     * @param cause causa original
     */
    protected CoffeeMailException(
            final int status, final String code, final ApiErrorDetails error, final @Nullable Throwable cause) {
        super(error.message(), cause);
        this.status = status;
        this.code = code;
        this.apiCode = error.apiCode();
        this.details = error.details();
        this.requestId = error.requestId();
    }

    /**
     * Status HTTP da resposta, ou 0 quando não houve resposta.
     *
     * @return o status
     */
    public int status() {
        return status;
    }

    /**
     * Código canônico do erro, estável entre os SDKs (ver {@link ErrorCodes}).
     *
     * @return o código
     */
    public String code() {
        return code;
    }

    /**
     * Código original enviado pela API, como {@code EMAIL_NOT_FOUND}.
     *
     * @return o código do servidor, quando houver
     */
    public Optional<String> apiCode() {
        return Optional.ofNullable(apiCode);
    }

    /**
     * Metadados do erro enviados pela API.
     *
     * @return mapa imutável, vazio quando não houver metadados
     */
    public Map<String, Object> details() {
        return details;
    }

    /**
     * Identificador da requisição ({@code x-request-id}), útil para suporte.
     *
     * @return o identificador, quando houver
     */
    public Optional<String> requestId() {
        return Optional.ofNullable(requestId);
    }
}
