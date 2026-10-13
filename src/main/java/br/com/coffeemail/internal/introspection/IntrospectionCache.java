package br.com.coffeemail.internal.introspection;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.constants.Defaults;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.models.common.ApiKeyIntrospection;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public final class IntrospectionCache {

    private static final Operation<ApiKeyIntrospection> OPERATION =
            Operation.get(ApiRoutes.API_KEY_INTROSPECTION, ResponseType.of(ApiKeyIntrospection.class));

    private final RequestExecutor executor;
    private final Clock clock;
    private final AtomicReference<Optional<CachedIntrospection>> cache = new AtomicReference<>(Optional.empty());

    public IntrospectionCache(final RequestExecutor executor, final Clock clock) {
        this.executor = executor;
        this.clock = clock;
    }

    public CompletableFuture<CoffeeMailResponse<ApiKeyIntrospection>> introspect(final boolean forceRefresh) {
        final Instant now = clock.instant();
        return cache.get()
                .filter(cached -> !forceRefresh)
                .filter(cached -> cached.expiresAt().isAfter(now))
                .map(cached -> CompletableFuture.completedFuture(CoffeeMailResponse.success(cached.value())))
                .orElseGet(this::fetch);
    }

    public void invalidate() {
        cache.set(Optional.empty());
    }

    private CompletableFuture<CoffeeMailResponse<ApiKeyIntrospection>> fetch() {
        return executor.executeAsync(OPERATION).thenApply(this::remember);
    }

    private CoffeeMailResponse<ApiKeyIntrospection> remember(final CoffeeMailResponse<ApiKeyIntrospection> response) {
        response.data()
                .ifPresent(value -> cache.set(Optional.of(
                        new CachedIntrospection(value, clock.instant().plus(Defaults.INTROSPECTION_TTL)))));
        return response;
    }

    private record CachedIntrospection(ApiKeyIntrospection value, Instant expiresAt) {}
}
