package br.com.coffeemail.internal.http;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.coffeemail.CoffeeMailLocale;
import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.errors.CoffeeMailException;
import br.com.coffeemail.errors.ErrorCodes;
import br.com.coffeemail.errors.NetworkException;
import br.com.coffeemail.errors.ValidationException;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.support.FakeTransport;
import br.com.coffeemail.support.TestClients;
import br.com.coffeemail.transport.HttpMethod;
import br.com.coffeemail.transport.HttpRequest;
import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

final class RequestExecutorTest {

    private static final String SDK_VERSION = System.getProperty("coffeemail.expectedSdkVersion");

    record Sample(String id, int count) {}

    record SampleQuery(
            @Nullable String status,
            @Nullable Integer limit,
            @Nullable String after) {}

    record SampleBody(String name, @Nullable String description) {}

    @Test
    void CA03_sendsDefaultHeadersWithoutContentTypeOnBodylessRequests() {
        final FakeTransport transport = FakeTransport.replying(200, "{\"id\":\"a\",\"count\":1}");

        TestClients.executor(transport).execute(Operation.get("/v1/product/x", ResponseType.of(Sample.class)));

        final HttpRequest request = transport.lastRequest();
        assertThat(request.method()).isEqualTo(HttpMethod.GET);
        assertThat(request.body()).isEmpty();
        assertThat(request.headers())
                .containsEntry("authorization", "Bearer cm_live_test")
                .containsEntry("accept", "application/json")
                .containsEntry("accept-language", "pt-BR")
                .containsEntry("user-agent", "coffeemail-java/" + SDK_VERSION)
                .doesNotContainKey("content-type");
    }

    @Test
    void CA03_sendsJsonBodyWithContentTypeAndOmitsNullFields() {
        final FakeTransport transport = FakeTransport.replying(201, "{\"id\":\"a\",\"count\":1}");

        TestClients.executor(transport)
                .execute(Operation.post("/v1/product/x", ResponseType.of(Sample.class))
                        .withBody(new SampleBody("Clientes", null)));

        final HttpRequest request = transport.lastRequest();
        assertThat(request.headers()).containsEntry("content-type", "application/json");
        assertThat(request.body()).contains("{\"name\":\"Clientes\"}");
    }

    @Test
    void CA03_acceptLanguageFollowsLocaleAndOperationHeadersAreMerged() {
        final FakeTransport transport = FakeTransport.replying(200, "{\"id\":\"a\",\"count\":1}");

        TestClients.executor(TestClients.config(transport, CoffeeMailLocale.EN))
                .execute(Operation.get("/v1/product/x", ResponseType.of(Sample.class))
                        .withHeaders(Map.of("x-idempotency-key", "pedido-1")));

        assertThat(transport.lastRequest().headers())
                .containsEntry("accept-language", "en")
                .containsEntry("x-idempotency-key", "pedido-1");
    }

    @Test
    void queryRecordIsSerializedInOrderWithoutNullsAndEncoded() {
        final FakeTransport transport = FakeTransport.replying(200, "{\"id\":\"a\",\"count\":1}");

        TestClients.executor(transport)
                .execute(Operation.get("/v1/product/emails", ResponseType.of(Sample.class))
                        .withQuery(new SampleQuery("delivered em lote", 50, null)));

        assertThat(transport.lastRequest().uri())
                .hasToString("https://api.test.local/v1/product/emails?status=delivered%20em%20lote&limit=50");
    }

    @Test
    void CA04_successfulResponseIsDecoded() {
        final FakeTransport transport = FakeTransport.replying(200, "{\"id\":\"eml_1\",\"count\":3,\"novo\":true}");

        final CoffeeMailResponse<Sample> response =
                TestClients.executor(transport).execute(Operation.get("/x", ResponseType.of(Sample.class)));

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.data()).contains(new Sample("eml_1", 3));
        assertThat(response.error()).isEmpty();
    }

    @Test
    void listResponsesAreDecoded() {
        final FakeTransport transport =
                FakeTransport.replying(202, "[{\"id\":\"a\",\"count\":1},{\"id\":\"b\",\"count\":2}]");

        final CoffeeMailResponse<List<Sample>> response =
                TestClients.executor(transport).execute(Operation.post("/x", ResponseType.listOf(Sample.class)));

        assertThat(response.unwrap()).containsExactly(new Sample("a", 1), new Sample("b", 2));
    }

    @Test
    void CA05_noContentResponsesSucceed() {
        final FakeTransport transport = FakeTransport.replying(204, "");

        final CoffeeMailResponse<NoContent> response =
                TestClients.executor(transport).execute(Operation.delete("/x", ResponseType.noContent()));

        assertThat(response.unwrap()).isEqualTo(NoContent.INSTANCE);
    }

    @Test
    void CA05_noContentIgnoresAnyReturnedBody() {
        final FakeTransport transport = FakeTransport.replying(200, "{\"ok\":true}");

        final CoffeeMailResponse<NoContent> response =
                TestClients.executor(transport).execute(Operation.delete("/x", ResponseType.noContent()));

        assertThat(response.unwrap()).isEqualTo(NoContent.INSTANCE);
    }

    @Test
    void CA05_optionalResponsesAreEmptyForBlankOrNullBodies() {
        final RequestExecutor blank = TestClients.executor(FakeTransport.replying(200, ""));
        final RequestExecutor nullBody = TestClients.executor(FakeTransport.replying(200, "null"));
        final RequestExecutor present = TestClients.executor(FakeTransport.replying(200, "{\"id\":\"a\",\"count\":1}"));
        final Operation<Optional<Sample>> operation = Operation.get("/x", ResponseType.optionalOf(Sample.class));

        assertThat(blank.execute(operation).unwrap()).isEmpty();
        assertThat(nullBody.execute(operation).unwrap()).isEmpty();
        assertThat(present.execute(operation).unwrap()).contains(new Sample("a", 1));
    }

    @Test
    void successWithBlankBodyForRequiredDataIsAnInvalidResponse() {
        final FakeTransport transport = FakeTransport.replying(200, "", Map.of("X-Request-Id", "req_9"));

        final CoffeeMailResponse<Sample> response =
                TestClients.executor(transport).execute(Operation.get("/x", ResponseType.of(Sample.class)));

        final CoffeeMailException error = response.error().orElseThrow();
        assertThat(error.code()).isEqualTo(ErrorCodes.INVALID_RESPONSE);
        assertThat(error.status()).isEqualTo(200);
        assertThat(error.requestId()).contains("req_9");
        assertThat(error).hasMessage("A resposta do servidor não pôde ser interpretada como JSON válido.");
    }

    @Test
    void successWithMalformedJsonIsAnInvalidResponse() {
        final FakeTransport transport = FakeTransport.replying(200, "<html>gateway</html>");

        final CoffeeMailResponse<Sample> response =
                TestClients.executor(transport).execute(Operation.get("/x", ResponseType.of(Sample.class)));

        assertThat(response.error())
                .hasValueSatisfying(error -> assertThat(error.code()).isEqualTo(ErrorCodes.INVALID_RESPONSE));
    }

    @Test
    void errorStatusesAreMappedToTypedErrors() {
        final FakeTransport transport =
                FakeTransport.replying(400, "{\"error\":{\"code\":\"VALIDATION_FAILED\",\"message\":\"inválido\"}}");

        final CoffeeMailResponse<Sample> response =
                TestClients.executor(transport).execute(Operation.get("/x", ResponseType.of(Sample.class)));

        assertThat(response.error()).containsInstanceOf(ValidationException.class);
    }

    @Test
    void CA09_timeoutBecomesNetworkErrorWithLocalizedMessage() {
        final FakeTransport transport = FakeTransport.failingWith(new HttpTimeoutException("request timed out"));

        final CoffeeMailResponse<Sample> response =
                TestClients.executor(transport).execute(Operation.get("/x", ResponseType.of(Sample.class)));

        final CoffeeMailException error = response.error().orElseThrow();
        assertThat(error).isInstanceOf(NetworkException.class);
        assertThat(error.status()).isZero();
        assertThat(error.code()).isEqualTo(ErrorCodes.NETWORK_ERROR);
        assertThat(error).hasMessage("A requisição excedeu o tempo limite configurado de 10000ms.");
        assertThat(error).hasCauseInstanceOf(HttpTimeoutException.class);
    }

    @Test
    void CA09_ioFailureWrappedInCompletionExceptionBecomesNetworkError() {
        final FakeTransport transport =
                FakeTransport.failingWith(new CompletionException(new IOException("connection refused")));

        final CoffeeMailResponse<Sample> response =
                TestClients.executor(transport).execute(Operation.get("/x", ResponseType.of(Sample.class)));

        assertThat(response.error()).hasValueSatisfying(error -> {
            assertThat(error).isInstanceOf(NetworkException.class);
            assertThat(error)
                    .hasMessage(
                            "Falha de rede ao tentar se comunicar com os servidores do CoffeeMail: connection refused");
            assertThat(error).hasCauseInstanceOf(IOException.class);
        });
    }

    @Test
    void CA09_transportThrowingSynchronouslyNeverEscapes() {
        final FakeTransport transport = FakeTransport.throwingSynchronously(new IllegalStateException());

        final CoffeeMailResponse<Sample> response =
                TestClients.executor(transport).execute(Operation.get("/x", ResponseType.of(Sample.class)));

        assertThat(response.error()).hasValueSatisfying(error -> {
            assertThat(error).isInstanceOf(NetworkException.class);
            assertThat(error).hasMessageEndingWith("IllegalStateException");
        });
    }

    @Test
    void asyncExecutionNeverCompletesExceptionally() {
        final FakeTransport transport = FakeTransport.failingWith(new IOException("reset"));

        final CoffeeMailResponse<Sample> response = TestClients.executor(transport)
                .executeAsync(Operation.get("/x", ResponseType.of(Sample.class)))
                .join();

        assertThat(response.error()).containsInstanceOf(NetworkException.class);
    }
}
