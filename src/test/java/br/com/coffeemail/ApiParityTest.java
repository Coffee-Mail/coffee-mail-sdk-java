package br.com.coffeemail;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.coffeemail.resources.Audiences;
import br.com.coffeemail.resources.AudiencesAsync;
import br.com.coffeemail.resources.Broadcasts;
import br.com.coffeemail.resources.BroadcastsAsync;
import br.com.coffeemail.resources.Contacts;
import br.com.coffeemail.resources.ContactsAsync;
import br.com.coffeemail.resources.Domains;
import br.com.coffeemail.resources.DomainsAsync;
import br.com.coffeemail.resources.Emails;
import br.com.coffeemail.resources.EmailsAsync;
import br.com.coffeemail.resources.Senders;
import br.com.coffeemail.resources.SendersAsync;
import br.com.coffeemail.resources.Stats;
import br.com.coffeemail.resources.StatsAsync;
import br.com.coffeemail.resources.Suppressions;
import br.com.coffeemail.resources.SuppressionsAsync;
import br.com.coffeemail.resources.Templates;
import br.com.coffeemail.resources.TemplatesAsync;
import br.com.coffeemail.resources.Webhooks;
import br.com.coffeemail.resources.WebhooksAsync;
import br.com.coffeemail.support.FakeTransport;
import br.com.coffeemail.support.Fixtures;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class ApiParityTest {

    static Stream<Arguments> resourcePairs() {
        return Stream.of(
                Arguments.of(Emails.class, EmailsAsync.class),
                Arguments.of(Domains.class, DomainsAsync.class),
                Arguments.of(Templates.class, TemplatesAsync.class),
                Arguments.of(Audiences.class, AudiencesAsync.class),
                Arguments.of(Contacts.class, ContactsAsync.class),
                Arguments.of(Broadcasts.class, BroadcastsAsync.class),
                Arguments.of(Senders.class, SendersAsync.class),
                Arguments.of(Suppressions.class, SuppressionsAsync.class),
                Arguments.of(Webhooks.class, WebhooksAsync.class),
                Arguments.of(Stats.class, StatsAsync.class));
    }

    private static Set<String> signatures(final Class<?> type, final boolean unwrapFuture) {
        return Arrays.stream(type.getMethods())
                .map(method -> method.getName() + Arrays.toString(method.getGenericParameterTypes()) + " → "
                        + returnType(method, unwrapFuture))
                .collect(Collectors.toUnmodifiableSet());
    }

    private static String returnType(final Method method, final boolean unwrapFuture) {
        final Type type = method.getGenericReturnType();
        if (unwrapFuture
                && type instanceof ParameterizedType parameterized
                && parameterized.getRawType() == CompletableFuture.class) {
            return parameterized.getActualTypeArguments()[0].getTypeName();
        }
        return type.getTypeName().replace("Async", "");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resourcePairs")
    void CA30_syncAndAsyncResourcesExposeTheSameOperations(final Class<?> sync, final Class<?> async) {
        assertThat(signatures(sync, false)).isEqualTo(signatures(async, true));
    }

    @Test
    void CA30_clientsExposeTheSameResources() {
        final Set<String> sync = Arrays.stream(CoffeeMail.class.getMethods())
                .filter(method -> method.getDeclaringClass() == CoffeeMail.class)
                .map(Method::getName)
                .collect(Collectors.toUnmodifiableSet());
        final Set<String> async = Arrays.stream(CoffeeMailAsync.class.getMethods())
                .filter(method -> method.getDeclaringClass() == CoffeeMailAsync.class)
                .map(Method::getName)
                .collect(Collectors.toUnmodifiableSet());

        assertThat(sync).isEqualTo(async);
        assertThat(sync).contains("emails", "webhooks", "stats", "introspect");
    }

    @Test
    void CA30_asyncClientReturnsTheSameEnvelopeWithoutCompletingExceptionally() {
        final CoffeeMailAsync client = CoffeeMailAsync.builder()
                .apiKey("cm_live_test")
                .transport(FakeTransport.replying(200, Fixtures.body("emails.get")))
                .build();
        final CoffeeMailAsync failing = CoffeeMailAsync.builder()
                .apiKey("cm_live_test")
                .transport(FakeTransport.replying(404, "{\"error\":{\"message\":\"x\"}}"))
                .build();

        assertThat(client.emails().get("e").join().unwrap().subject()).contains("Pedido confirmado");
        assertThat(failing.emails().get("e"))
                .succeedsWithin(java.time.Duration.ofSeconds(1))
                .satisfies(response -> assertThat(response.isSuccess()).isFalse());
        assertThat(client.introspect()).isNotNull();
    }
}
