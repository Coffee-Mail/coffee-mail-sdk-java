package br.com.coffeemail.internal.errors;

import br.com.coffeemail.CoffeeMailLocale;
import br.com.coffeemail.errors.ApiErrorDetails;
import br.com.coffeemail.errors.AuthenticationException;
import br.com.coffeemail.errors.CoffeeMailException;
import br.com.coffeemail.errors.ConflictException;
import br.com.coffeemail.errors.ErrorCodes;
import br.com.coffeemail.errors.ForbiddenException;
import br.com.coffeemail.errors.InternalServerException;
import br.com.coffeemail.errors.NotFoundException;
import br.com.coffeemail.errors.PaymentRequiredException;
import br.com.coffeemail.errors.RateLimitException;
import br.com.coffeemail.errors.ValidationException;
import br.com.coffeemail.internal.constants.HeaderNames;
import br.com.coffeemail.internal.constants.HttpStatus;
import br.com.coffeemail.internal.i18n.MessageKey;
import br.com.coffeemail.internal.i18n.Messages;
import br.com.coffeemail.internal.json.JsonCodec;
import br.com.coffeemail.internal.result.Result;
import br.com.coffeemail.transport.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

public final class ErrorMapper {

    private static final Pattern RETRY_AFTER_SECONDS = Pattern.compile("\\d{1,18}");
    private static final String ERROR_FIELD = "error";
    private static final String MESSAGE_FIELD = "message";
    private static final String CODE_FIELD = "code";
    private static final String META_FIELD = "meta";
    private static final String DETAILS_FIELD = "details";

    private static final Map<Integer, ErrorFactory> FACTORIES = Map.ofEntries(
            Map.entry(HttpStatus.BAD_REQUEST, (status, error, retryAfter) -> new ValidationException(error)),
            Map.entry(HttpStatus.UNAUTHORIZED, (status, error, retryAfter) -> new AuthenticationException(error)),
            Map.entry(HttpStatus.PAYMENT_REQUIRED, (status, error, retryAfter) -> new PaymentRequiredException(error)),
            Map.entry(HttpStatus.FORBIDDEN, (status, error, retryAfter) -> new ForbiddenException(error)),
            Map.entry(HttpStatus.NOT_FOUND, (status, error, retryAfter) -> new NotFoundException(error)),
            Map.entry(HttpStatus.CONFLICT, (status, error, retryAfter) -> new ConflictException(error)),
            Map.entry(
                    HttpStatus.TOO_MANY_REQUESTS,
                    (status, error, retryAfter) -> new RateLimitException(error, retryAfter)),
            Map.entry(HttpStatus.INTERNAL_SERVER_ERROR, InternalServerFactory.INSTANCE),
            Map.entry(HttpStatus.BAD_GATEWAY, InternalServerFactory.INSTANCE),
            Map.entry(HttpStatus.SERVICE_UNAVAILABLE, InternalServerFactory.INSTANCE),
            Map.entry(HttpStatus.GATEWAY_TIMEOUT, InternalServerFactory.INSTANCE));

    private final JsonCodec codec;

    public ErrorMapper(final JsonCodec codec) {
        this.codec = codec;
    }

    public CoffeeMailException map(final HttpResponse response, final CoffeeMailLocale locale) {
        final ApiErrorDetails error = details(response, locale);
        final ErrorFactory factory = FACTORIES.get(response.status());
        if (factory != null) {
            return factory.create(response.status(), error, retryAfterSeconds(response));
        }
        final String apiCode = error.apiCode();
        return new CoffeeMailException(response.status(), apiCode == null ? ErrorCodes.API_ERROR : apiCode, error);
    }

    private ApiErrorDetails details(final HttpResponse response, final CoffeeMailLocale locale) {
        final Result<JsonNode, Exception> parsed = codec.readTree(response.body());
        final JsonNode root = parsed.fold(node -> node, failure -> MissingNode.getInstance());
        final JsonNode error = root.path(ERROR_FIELD);
        final Optional<String> rawBody = parsed.fold(
                node -> Optional.empty(),
                failure -> Optional.of(response.body()).filter(body -> !body.isBlank()));
        final String message = firstText(error.path(MESSAGE_FIELD), root.path(MESSAGE_FIELD))
                .or(() -> rawBody)
                .orElseGet(() -> Messages.format(locale, MessageKey.UNEXPECTED_ERROR));
        final Map<String, Object> details = new LinkedHashMap<>(codec.toMap(error.path(META_FIELD)));
        details.putAll(codec.toMap(error.path(DETAILS_FIELD)));
        return new ApiErrorDetails(
                message,
                firstText(error.path(CODE_FIELD)).orElse(null),
                details,
                response.header(HeaderNames.REQUEST_ID).orElse(null));
    }

    private static Optional<String> firstText(final JsonNode... candidates) {
        return Stream.of(candidates)
                .filter(JsonNode::isTextual)
                .map(JsonNode::asText)
                .filter(text -> !text.isBlank())
                .findFirst();
    }

    private static @Nullable Long retryAfterSeconds(final HttpResponse response) {
        return response.header(HeaderNames.RETRY_AFTER)
                .map(String::trim)
                .filter(RETRY_AFTER_SECONDS.asMatchPredicate())
                .map(Long::valueOf)
                .orElse(null);
    }

    @FunctionalInterface
    private interface ErrorFactory {
        CoffeeMailException create(int status, ApiErrorDetails error, @Nullable Long retryAfterSeconds);
    }

    private enum InternalServerFactory implements ErrorFactory {
        INSTANCE;

        @Override
        public CoffeeMailException create(
                final int status, final ApiErrorDetails error, final @Nullable Long retryAfterSeconds) {
            return new InternalServerException(status, error);
        }
    }
}
