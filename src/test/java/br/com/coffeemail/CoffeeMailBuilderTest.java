package br.com.coffeemail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import br.com.coffeemail.errors.AuthenticationException;
import br.com.coffeemail.errors.ErrorCodes;
import br.com.coffeemail.support.FakeTransport;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

final class CoffeeMailBuilderTest {

    private static final String INTROSPECTION_BODY =
            "{\"id\":\"key_1\",\"name\":\"Produção\",\"organisationId\":\"org_1\",\"scopes\":[\"full_access\"]}";

    @Test
    void CA01_withoutApiKeyBuildFailsWithLocalizedAuthenticationError() {
        final CoffeeMailBuilder<CoffeeMail> builder = CoffeeMail.builder().environment(name -> Optional.empty());

        assertThatThrownBy(builder::build).isInstanceOfSatisfying(AuthenticationException.class, exception -> {
            assertThat(exception).hasMessageStartingWith("A chave de API não foi informada");
            assertThat(exception.status()).isEqualTo(401);
            assertThat(exception.code()).isEqualTo(ErrorCodes.UNAUTHORIZED);
        });
    }

    @Test
    void CA01_missingApiKeyMessageFollowsTheConfiguredLocale() {
        final CoffeeMailBuilder<CoffeeMail> builder =
                CoffeeMail.builder().locale(CoffeeMailLocale.EN).environment(name -> Optional.empty());

        assertThatThrownBy(builder::build).hasMessageStartingWith("API key was not provided");
    }

    @Test
    void CA01_blankApiKeyIsRejected() {
        final CoffeeMailBuilder<CoffeeMail> builder =
                CoffeeMail.builder().apiKey("   ").environment(name -> Optional.empty());

        assertThatThrownBy(builder::build).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void CA02_apiKeyFromEnvironmentIsStrippedAndSent() {
        final FakeTransport transport = FakeTransport.replying(200, INTROSPECTION_BODY);
        final CoffeeMail client = CoffeeMail.builder()
                .environment(name -> Map.of("COFFEEMAIL_API_KEY", "  cm_live_env  ").entrySet().stream()
                        .filter(entry -> entry.getKey().equals(name))
                        .map(Map.Entry::getValue)
                        .findFirst())
                .transport(transport)
                .build();

        client.introspect();

        assertThat(transport.lastRequest().headers()).containsEntry("authorization", "Bearer cm_live_env");
    }

    @Test
    void explicitApiKeyTakesPrecedenceOverEnvironment() {
        final FakeTransport transport = FakeTransport.replying(200, INTROSPECTION_BODY);
        final CoffeeMail client = CoffeeMail.builder()
                .apiKey("cm_live_explicit")
                .environment(name -> Optional.of("cm_live_env"))
                .transport(transport)
                .build();

        client.introspect();

        assertThat(transport.lastRequest().headers()).containsEntry("authorization", "Bearer cm_live_explicit");
    }

    @Test
    void defaultsToProductionBaseUrlAndTenSecondTimeout() {
        final FakeTransport transport = FakeTransport.replying(200, INTROSPECTION_BODY);
        final CoffeeMail client =
                CoffeeMail.builder().apiKey("cm_live_x").transport(transport).build();

        client.introspect();

        assertThat(transport.lastRequest().uri())
                .hasToString("https://api.coffeemail.com.br/v1/product/auth/me/api-key");
        assertThat(transport.lastTimeout()).isEqualTo(Duration.ofSeconds(10));
    }

    @Test
    void customBaseUrlLosesTrailingSlashAndTimeoutIsForwarded() {
        final FakeTransport transport = FakeTransport.replying(200, INTROSPECTION_BODY);
        final CoffeeMail client = CoffeeMail.builder()
                .apiKey("cm_live_x")
                .baseUrl("http://localhost:3030/")
                .timeout(Duration.ofMillis(1500))
                .transport(transport)
                .build();

        client.introspect();

        assertThat(transport.lastRequest().uri()).hasToString("http://localhost:3030/v1/product/auth/me/api-key");
        assertThat(transport.lastTimeout()).isEqualTo(Duration.ofMillis(1500));
    }

    @Test
    void createWithoutEnvironmentKeyFails() {
        final boolean hasEnvironmentKey = Optional.ofNullable(System.getenv("COFFEEMAIL_API_KEY"))
                .filter(key -> !key.isBlank())
                .isPresent();

        assumeFalse(hasEnvironmentKey);
        assertThatThrownBy(CoffeeMail::create).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void versionIsExposedByBothClients() {
        assertThat(CoffeeMail.version()).isEqualTo(System.getProperty("coffeemail.expectedSdkVersion"));
        assertThat(CoffeeMailAsync.version()).isEqualTo(CoffeeMail.version());
    }
}
