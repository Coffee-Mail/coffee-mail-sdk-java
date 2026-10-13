package br.com.coffeemail.internal.errors;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.coffeemail.CoffeeMailLocale;
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
import br.com.coffeemail.internal.json.JsonCodec;
import br.com.coffeemail.transport.HttpResponse;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class ErrorMapperTest {

    private static final String API_ERROR_BODY = "{\"error\":{\"code\":\"SERVER_CODE\",\"message\":\"mensagem\"}}";

    private final ErrorMapper mapper = new ErrorMapper(new JsonCodec());

    private CoffeeMailException map(final int status, final String body) {
        return map(status, body, Map.of());
    }

    private CoffeeMailException map(final int status, final String body, final Map<String, String> headers) {
        return mapper.map(new HttpResponse(status, headers, body), CoffeeMailLocale.PT_BR);
    }

    static Stream<Arguments> mappedStatuses() {
        return Stream.of(
                Arguments.of(400, ValidationException.class, ErrorCodes.VALIDATION_ERROR),
                Arguments.of(401, AuthenticationException.class, ErrorCodes.UNAUTHORIZED),
                Arguments.of(402, PaymentRequiredException.class, ErrorCodes.PAYMENT_REQUIRED),
                Arguments.of(403, ForbiddenException.class, ErrorCodes.FORBIDDEN),
                Arguments.of(404, NotFoundException.class, ErrorCodes.NOT_FOUND),
                Arguments.of(409, ConflictException.class, ErrorCodes.CONFLICT),
                Arguments.of(429, RateLimitException.class, ErrorCodes.RATE_LIMIT_EXCEEDED),
                Arguments.of(500, InternalServerException.class, ErrorCodes.INTERNAL_SERVER_ERROR),
                Arguments.of(502, InternalServerException.class, ErrorCodes.INTERNAL_SERVER_ERROR),
                Arguments.of(503, InternalServerException.class, ErrorCodes.INTERNAL_SERVER_ERROR),
                Arguments.of(504, InternalServerException.class, ErrorCodes.INTERNAL_SERVER_ERROR));
    }

    @ParameterizedTest
    @MethodSource("mappedStatuses")
    void CA06_statusIsMappedToItsErrorClassWithCanonicalCode(
            final int status, final Class<? extends CoffeeMailException> type, final String code) {
        final CoffeeMailException error = map(status, API_ERROR_BODY);

        assertThat(error).isExactlyInstanceOf(type);
        assertThat(error).hasMessage("mensagem");
        assertThat(error.status()).isEqualTo(status);
        assertThat(error.code()).isEqualTo(code);
        assertThat(error.apiCode()).contains("SERVER_CODE");
    }

    @Test
    void CA06_unmappedStatusKeepsServerCode() {
        final CoffeeMailException error = map(418, API_ERROR_BODY);

        assertThat(error).isExactlyInstanceOf(CoffeeMailException.class);
        assertThat(error.status()).isEqualTo(418);
        assertThat(error.code()).isEqualTo("SERVER_CODE");
    }

    @Test
    void CA06_unmappedStatusWithoutServerCodeFallsBackToApiError() {
        final CoffeeMailException error = map(418, "");

        assertThat(error.code()).isEqualTo(ErrorCodes.API_ERROR);
        assertThat(error.apiCode()).isEmpty();
    }

    @Test
    void CA07_serverCodeMetaDetailsAndRequestIdArePreserved() {
        final CoffeeMailException error = map(
                400,
                "{\"error\":{\"code\":\"VALIDATION_FAILED\",\"message\":\"m\",\"meta\":{\"field\":\"to\",\"empty\":null},"
                        + "\"details\":{\"limit\":10}}}",
                Map.of("X-Request-Id", "r1"));

        assertThat(error.code()).isEqualTo(ErrorCodes.VALIDATION_ERROR);
        assertThat(error.apiCode()).contains("VALIDATION_FAILED");
        assertThat(error).hasMessage("m");
        assertThat(error.details())
                .containsEntry("field", "to")
                .containsEntry("limit", 10)
                .doesNotContainKey("empty");
        assertThat(error.requestId()).contains("r1");
    }

    @Test
    void CA08_numericRetryAfterIsExposed() {
        final RateLimitException error = (RateLimitException) map(429, API_ERROR_BODY, Map.of("Retry-After", " 12 "));

        assertThat(error.retryAfterSeconds()).hasValue(12);
    }

    @Test
    void CA08_httpDateRetryAfterIsIgnored() {
        final RateLimitException error =
                (RateLimitException) map(429, API_ERROR_BODY, Map.of("retry-after", "Wed, 21 Oct 2026 07:28:00 GMT"));

        assertThat(error.retryAfterSeconds()).isEmpty();
    }

    @Test
    void topLevelMessageIsUsedWhenErrorObjectIsAbsent() {
        assertThat(map(400, "{\"message\":\"Body inválido\"}")).hasMessage("Body inválido");
    }

    @Test
    void nonJsonBodyBecomesTheMessage() {
        assertThat(map(502, "Bad Gateway")).hasMessage("Bad Gateway");
    }

    @Test
    void emptyBodyFallsBackToLocalizedUnexpectedError() {
        assertThat(map(500, ""))
                .hasMessage("Ocorreu um erro inesperado durante a comunicação com a API do CoffeeMail.");
        assertThat(mapper.map(new HttpResponse(500, Map.of(), "   "), CoffeeMailLocale.EN))
                .hasMessage("An unexpected error occurred while communicating with CoffeeMail API.");
    }

    @Test
    void blankServerMessageIsIgnored() {
        assertThat(map(400, "{\"error\":{\"message\":\"  \"},\"message\":\"fallback\"}"))
                .hasMessage("fallback");
    }
}
