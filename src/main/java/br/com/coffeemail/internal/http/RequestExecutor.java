package br.com.coffeemail.internal.http;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.errors.ApiErrorDetails;
import br.com.coffeemail.errors.CoffeeMailException;
import br.com.coffeemail.errors.ErrorCodes;
import br.com.coffeemail.errors.NetworkException;
import br.com.coffeemail.internal.ClientConfig;
import br.com.coffeemail.internal.constants.Defaults;
import br.com.coffeemail.internal.constants.HeaderNames;
import br.com.coffeemail.internal.constants.HttpStatus;
import br.com.coffeemail.internal.errors.ErrorMapper;
import br.com.coffeemail.internal.i18n.MessageKey;
import br.com.coffeemail.internal.i18n.Messages;
import br.com.coffeemail.internal.json.JsonCodec;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.internal.result.Result;
import br.com.coffeemail.internal.result.TryCatch;
import br.com.coffeemail.transport.HttpRequest;
import br.com.coffeemail.transport.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

public final class RequestExecutor {

    private static final String TIMEOUT_PARAM = "timeoutMs";
    private static final String DETAILS_PARAM = "details";

    private final ClientConfig config;
    private final JsonCodec codec;
    private final ErrorMapper errors;

    public RequestExecutor(final ClientConfig config, final JsonCodec codec, final ErrorMapper errors) {
        this.config = config;
        this.codec = codec;
        this.errors = errors;
    }

    public <T> CoffeeMailResponse<T> execute(final Operation<T> operation) {
        return executeAsync(operation).join();
    }

    public <T> CompletableFuture<CoffeeMailResponse<T>> executeAsync(final Operation<T> operation) {
        return buildRequest(operation).fold(request -> dispatch(request, operation), failure -> failed(failure));
    }

    private Result<HttpRequest, Exception> buildRequest(final Operation<?> operation) {
        return encodeBody(operation)
                .flatMap(body -> codec.toQuery(operation.query())
                        .map(query -> new HttpRequest(
                                operation.method(),
                                UrlBuilder.build(config.baseUrl(), operation.path(), query),
                                headers(operation, body.isPresent()),
                                body)));
    }

    private Result<Optional<String>, Exception> encodeBody(final Operation<?> operation) {
        return operation
                .body()
                .map(body -> codec.encode(body).map(Optional::of))
                .orElseGet(() -> Result.ok(Optional.empty()));
    }

    private Map<String, String> headers(final Operation<?> operation, final boolean hasBody) {
        final Map<String, String> defaults = Map.of(
                HeaderNames.AUTHORIZATION,
                Defaults.BEARER_PREFIX + config.apiKey(),
                HeaderNames.ACCEPT,
                Defaults.JSON_MEDIA_TYPE,
                HeaderNames.ACCEPT_LANGUAGE,
                config.locale().tag(),
                HeaderNames.USER_AGENT,
                Defaults.USER_AGENT);
        final Map<String, String> content =
                hasBody ? Map.of(HeaderNames.CONTENT_TYPE, Defaults.JSON_MEDIA_TYPE) : Map.of();
        return Stream.of(defaults, content, operation.headers())
                .flatMap(headers -> headers.entrySet().stream())
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey, Map.Entry::getValue, (first, second) -> second));
    }

    private <T> CompletableFuture<CoffeeMailResponse<T>> dispatch(
            final HttpRequest request, final Operation<T> operation) {
        return TryCatch.of(() -> config.transport().send(request, config.timeout()))
                .fold(
                        future -> future.handle((response, failure) -> complete(response, failure, operation)),
                        failure -> failed(failure));
    }

    private <T> CoffeeMailResponse<T> complete(
            final @Nullable HttpResponse response, final @Nullable Throwable failure, final Operation<T> operation) {
        if (failure != null || response == null) {
            return CoffeeMailResponse.failure(transportFailure(failure));
        }
        return interpret(response, operation);
    }

    private <T> CoffeeMailResponse<T> interpret(final HttpResponse response, final Operation<T> operation) {
        final ResponseType<T> type = operation.responseType();
        if (!HttpStatus.isSuccess(response.status())) {
            return CoffeeMailResponse.failure(operation.errorMapping().apply(errors.map(response, config.locale())));
        }
        if (response.status() == HttpStatus.NO_CONTENT || response.body().isBlank()) {
            return type.emptyValue()
                    .map(CoffeeMailResponse::success)
                    .orElseGet(() -> CoffeeMailResponse.failure(invalidResponse(response)));
        }
        return codec.decode(response.body(), type)
                .fold(CoffeeMailResponse::success, failure -> CoffeeMailResponse.failure(invalidResponse(response)));
    }

    private CoffeeMailException invalidResponse(final HttpResponse response) {
        return new CoffeeMailException(
                response.status(),
                ErrorCodes.INVALID_RESPONSE,
                new ApiErrorDetails(
                        Messages.format(config.locale(), MessageKey.INVALID_JSON),
                        null,
                        Map.of(),
                        response.header(HeaderNames.REQUEST_ID).orElse(null)));
    }

    private <T> CompletableFuture<CoffeeMailResponse<T>> failed(final Throwable failure) {
        return CompletableFuture.completedFuture(CoffeeMailResponse.failure(transportFailure(failure)));
    }

    private CoffeeMailException transportFailure(final @Nullable Throwable failure) {
        final Optional<Throwable> cause = unwrap(failure);
        final boolean timedOut = cause.filter(RequestExecutor::isTimeout).isPresent();
        if (timedOut) {
            return new NetworkException(
                    Messages.format(
                            config.locale(),
                            MessageKey.REQUEST_TIMEOUT,
                            Map.of(
                                    TIMEOUT_PARAM,
                                    String.valueOf(config.timeout().toMillis()))),
                    cause.orElse(null));
        }
        return new NetworkException(
                Messages.format(
                        config.locale(),
                        MessageKey.NETWORK_ERROR,
                        Map.of(
                                DETAILS_PARAM,
                                cause.map(RequestExecutor::describe).orElse(""))),
                cause.orElse(null));
    }

    private static Optional<Throwable> unwrap(final @Nullable Throwable failure) {
        if (failure == null) {
            return Optional.empty();
        }
        final Throwable cause = failure.getCause();
        return isWrapper(failure) && cause != null ? unwrap(cause) : Optional.of(failure);
    }

    private static boolean isWrapper(final Throwable failure) {
        return failure instanceof CompletionException || failure instanceof ExecutionException;
    }

    private static boolean isTimeout(final Throwable failure) {
        return failure instanceof HttpTimeoutException || failure instanceof TimeoutException;
    }

    private static String describe(final Throwable failure) {
        return Optional.ofNullable(failure.getMessage())
                .orElse(failure.getClass().getSimpleName());
    }
}
