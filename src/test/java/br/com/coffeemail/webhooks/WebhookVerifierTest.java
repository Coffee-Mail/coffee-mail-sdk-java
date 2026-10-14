package br.com.coffeemail.webhooks;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.coffeemail.CoffeeMail;
import br.com.coffeemail.internal.result.TryCatch;
import br.com.coffeemail.support.FakeTransport;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class WebhookVerifierTest {

    private static final JsonNode VECTORS = TryCatch.of(() ->
                    new ObjectMapper().readTree(Files.readString(Path.of("src/test/resources/webhooks/vectors.json"))))
            .fold(node -> node.path("vectors"), error -> MissingNode.getInstance());

    record Vector(
            String name,
            String secret,
            String body,
            String timestamp,
            String idempotencyKey,
            String signature,
            long nowEpochSeconds,
            boolean expected) {

        @Override
        public String toString() {
            return name;
        }

        VerifyWebhookInput input() {
            return new VerifyWebhookInput(body, signature, timestamp, idempotencyKey, secret);
        }

        WebhookVerifier verifier() {
            return WebhookVerifier.builder()
                    .clock(Clock.fixed(Instant.ofEpochSecond(nowEpochSeconds), ZoneOffset.UTC))
                    .build();
        }
    }

    static Stream<Vector> vectors() {
        return StreamSupport.stream(VECTORS.spliterator(), false)
                .map(node -> new Vector(
                        node.path("name").asText(),
                        node.path("secret").asText(),
                        node.path("body").asText(),
                        node.path("timestamp").asText(),
                        node.path("idempotencyKey").asText(),
                        node.path("signature").asText(),
                        node.path("nowEpochSeconds").asLong(),
                        node.path("expected").asBoolean()));
    }

    private static Vector vector(final String name) {
        return vectors().filter(item -> item.name().equals(name)).findFirst().orElseThrow();
    }

    @Test
    void vectorsWereGeneratedByTheServerSigner() {
        assertThat(vectors()).hasSize(11).anyMatch(Vector::expected).anyMatch(item -> !item.expected());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("vectors")
    void CA25_to_CA28_verifierAgreesWithTheServerSigner(final Vector vector) {
        assertThat(vector.verifier().verify(vector.input())).isEqualTo(vector.expected());
    }

    @Test
    void CA29_headersAreReadCaseInsensitively() {
        final Vector valid = vector("valid_unicode");
        final Map<String, String> headers = Map.of(
                "X-Webhook-Signature", valid.signature(),
                "X-WEBHOOK-TIMESTAMP", valid.timestamp(),
                "x-webhook-idempotency-key", valid.idempotencyKey(),
                "Content-Type", "application/json");

        final VerifyWebhookInput fromText = VerifyWebhookInput.fromHeaders(valid.body(), headers, valid.secret());
        final VerifyWebhookInput fromBytes =
                VerifyWebhookInput.fromHeaders(valid.body().getBytes(StandardCharsets.UTF_8), headers, valid.secret());

        assertThat(valid.verifier().verify(fromText)).isTrue();
        assertThat(valid.verifier().verify(fromBytes)).isTrue();
    }

    @Test
    void CA29_missingHeadersAreRejected() {
        final Vector valid = vector("valid_ascii");

        assertThat(valid.verifier().verify(VerifyWebhookInput.fromHeaders(valid.body(), Map.of(), valid.secret())))
                .isFalse();
    }

    @Test
    void emptySecretIsRejectedWithoutException() {
        final Vector valid = vector("valid_ascii");

        assertThat(valid.verifier()
                        .verify(new VerifyWebhookInput(
                                valid.body(), valid.signature(), valid.timestamp(), valid.idempotencyKey(), "")))
                .isFalse();
    }

    @Test
    void customToleranceIsApplied() {
        final Vector expired = vector("expired_timestamp");
        final WebhookVerifier lenient = WebhookVerifier.builder()
                .tolerance(Duration.ofMinutes(10))
                .clock(Clock.fixed(Instant.ofEpochSecond(expired.nowEpochSeconds()), ZoneOffset.UTC))
                .build();

        assertThat(expired.verifier().verify(expired.input())).isFalse();
        assertThat(lenient.verify(expired.input())).isTrue();
    }

    @Test
    void staticVerificationUsesTheSystemClock() {
        assertThat(WebhookVerifier.verifySignature(vector("valid_ascii").input()))
                .isFalse();
        assertThat(WebhookVerifier.DEFAULT_TOLERANCE.toSeconds()).isEqualTo(300);
    }

    @Test
    void clientResourceDelegatesToTheVerifierWithTheClientClock() {
        final Vector valid = vector("valid_ascii");
        final CoffeeMail client = CoffeeMail.builder()
                .apiKey("cm_live_test")
                .transport(FakeTransport.replying(200, "{}"))
                .build();

        assertThat(client.webhooks().verifySignature(valid.input())).isFalse();
        assertThat(WebhookHeaders.SIGNATURE).isEqualTo("x-webhook-signature");
    }
}
