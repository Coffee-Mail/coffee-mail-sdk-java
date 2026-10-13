package br.com.coffeemail.internal.constants;

public final class HttpStatus {

    public static final int NONE = 0;
    public static final int SUCCESS_MIN = 200;
    public static final int NO_CONTENT = 204;
    public static final int SUCCESS_MAX = 299;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int PAYMENT_REQUIRED = 402;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;
    public static final int TOO_MANY_REQUESTS = 429;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    private HttpStatus() {}

    public static boolean isSuccess(final int status) {
        return status >= SUCCESS_MIN && status <= SUCCESS_MAX;
    }
}
