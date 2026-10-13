package br.com.coffeemail.errors;

/** Códigos canônicos de erro expostos por {@link CoffeeMailException#code()}. */
public final class ErrorCodes {

    /** Payload ou parâmetros inválidos (HTTP 400). */
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    /** Chave de API ausente, inválida ou expirada (HTTP 401). */
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    /** Cota do plano esgotada (HTTP 402). */
    public static final String PAYMENT_REQUIRED = "PAYMENT_REQUIRED";
    /** Acesso negado ao recurso (HTTP 403). */
    public static final String FORBIDDEN = "FORBIDDEN";
    /** Recurso inexistente (HTTP 404). */
    public static final String NOT_FOUND = "NOT_FOUND";
    /** Conflito com recurso existente (HTTP 409). */
    public static final String CONFLICT = "CONFLICT";
    /** Limite de requisições excedido (HTTP 429). */
    public static final String RATE_LIMIT_EXCEEDED = "RATE_LIMIT_EXCEEDED";
    /** Erro do servidor (HTTP 5xx). */
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    /** Falha de rede ou tempo limite excedido. */
    public static final String NETWORK_ERROR = "NETWORK_ERROR";
    /** A chave de API não tem a permissão exigida pela operação. */
    public static final String PERMISSION_DENIED = "PERMISSION_DENIED";
    /** Erro HTTP sem classe específica. */
    public static final String API_ERROR = "API_ERROR";
    /** Resposta de sucesso com corpo ilegível. */
    public static final String INVALID_RESPONSE = "INVALID_RESPONSE";

    private ErrorCodes() {}
}
