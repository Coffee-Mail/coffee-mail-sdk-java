package br.com.coffeemail;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.coffeemail.errors.NotFoundException;
import br.com.coffeemail.models.common.ApiKeyIntrospection;
import br.com.coffeemail.support.FakeTransport;
import br.com.coffeemail.support.MutableClock;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

final class IntrospectionTest {

    private static final String BODY =
            "{\"id\":\"key_1\",\"name\":\"Produção\",\"organisationId\":\"org_1\",\"scopes\":[\"full_access\"]}";

    private final MutableClock clock = new MutableClock(Instant.parse("2026-10-14T12:00:00Z"));

    private CoffeeMail clientWith(final FakeTransport transport) {
        return CoffeeMail.builder()
                .apiKey("cm_live_x")
                .transport(transport)
                .clock(clock)
                .build();
    }

    @Test
    void CA10_introspectionIsDecodedAndCachedForFiveMinutes() {
        final FakeTransport transport = FakeTransport.replying(200, BODY);
        final CoffeeMail client = clientWith(transport);

        final CoffeeMailResponse<ApiKeyIntrospection> first = client.introspect();
        clock.advance(Duration.ofMinutes(4));
        final CoffeeMailResponse<ApiKeyIntrospection> second = client.introspect();

        assertThat(first.unwrap())
                .isEqualTo(new ApiKeyIntrospection("key_1", "Produção", "org_1", java.util.List.of("full_access")));
        assertThat(second.unwrap()).isEqualTo(first.unwrap());
        assertThat(transport.requests()).hasSize(1);
        assertThat(transport.lastRequest().uri().getPath()).isEqualTo("/v1/product/auth/me/api-key");
    }

    @Test
    void CA10_cacheExpiresAfterFiveMinutes() {
        final FakeTransport transport = FakeTransport.replying(200, BODY);
        final CoffeeMail client = clientWith(transport);

        client.introspect();
        clock.advance(Duration.ofMinutes(5));
        client.introspect();

        assertThat(transport.requests()).hasSize(2);
    }

    @Test
    void CA10_forceRefreshAndInvalidationBypassTheCache() {
        final FakeTransport transport = FakeTransport.replying(200, BODY);
        final CoffeeMail client = clientWith(transport);

        client.introspect();
        client.introspect(true);
        client.invalidateApiKeyCache();
        client.introspect();

        assertThat(transport.requests()).hasSize(3);
    }

    @Test
    void CA10_failuresAreNotCached() {
        final FakeTransport transport =
                FakeTransport.replying(404, "{\"error\":{\"code\":\"NOT_FOUND\",\"message\":\"x\"}}");
        final CoffeeMail client = clientWith(transport);

        final CoffeeMailResponse<ApiKeyIntrospection> response = client.introspect();
        client.introspect();

        assertThat(response.error()).containsInstanceOf(NotFoundException.class);
        assertThat(transport.requests()).hasSize(2);
    }
}
