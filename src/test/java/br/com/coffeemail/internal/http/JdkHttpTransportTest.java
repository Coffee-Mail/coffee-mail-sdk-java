package br.com.coffeemail.internal.http;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.coffeemail.support.StubHttpServer;
import br.com.coffeemail.support.StubHttpServer.RecordedRequest;
import br.com.coffeemail.support.StubHttpServer.StubResponse;
import br.com.coffeemail.transport.HttpMethod;
import br.com.coffeemail.transport.HttpRequest;
import br.com.coffeemail.transport.HttpResponse;
import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class JdkHttpTransportTest {

    private static final String SILENT_PATH = "/silent";
    private static final StubResponse ECHO_RESPONSE =
            new StubResponse(201, Map.of("X-Request-Id", List.of("req_1"), "Vary", List.of("a", "b")), "{\"ok\":true}");

    private final JdkHttpTransport transport = new JdkHttpTransport(Duration.ofSeconds(2));
    private final AtomicReference<StubHttpServer> server = new AtomicReference<>();

    @BeforeEach
    void startServer() throws IOException {
        server.set(StubHttpServer.start(
                request -> request.path().equals(SILENT_PATH) ? Optional.empty() : Optional.of(ECHO_RESPONSE)));
    }

    @AfterEach
    void stopServer() throws IOException {
        server.get().close();
    }

    private RecordedRequest received() {
        return server.get().requests().get(0);
    }

    @Test
    void sendsMethodHeadersAndBodyAndReadsTheResponse() {
        final HttpResponse response = transport
                .send(
                        new HttpRequest(
                                HttpMethod.PATCH,
                                server.get().uri("/echo"),
                                Map.of("x-custom", "valor"),
                                Optional.of("{\"a\":1}")),
                        Duration.ofSeconds(2))
                .join();

        assertThat(received().method()).isEqualTo("PATCH");
        assertThat(received().body()).isEqualTo("{\"a\":1}");
        assertThat(received().headers()).containsEntry("x-custom", "valor");
        assertThat(response.status()).isEqualTo(201);
        assertThat(response.body()).isEqualTo("{\"ok\":true}");
        assertThat(response.header("X-REQUEST-ID")).contains("req_1");
        assertThat(response.header("vary")).contains("a, b");
    }

    @Test
    void bodylessRequestSendsEmptyBody() {
        transport
                .send(
                        new HttpRequest(HttpMethod.DELETE, server.get().uri("/echo"), Map.of(), Optional.empty()),
                        Duration.ofSeconds(2))
                .join();

        assertThat(received().method()).isEqualTo("DELETE");
        assertThat(received().body()).isEmpty();
    }

    @Test
    void slowServerCompletesExceptionallyWithTimeout() {
        final CompletableFuture<HttpResponse> future = transport.send(
                new HttpRequest(HttpMethod.GET, server.get().uri(SILENT_PATH), Map.of(), Optional.empty()),
                Duration.ofMillis(200));

        assertThat(future)
                .failsWithin(Duration.ofSeconds(5))
                .withThrowableThat()
                .havingRootCause()
                .isInstanceOf(HttpTimeoutException.class);
    }
}
