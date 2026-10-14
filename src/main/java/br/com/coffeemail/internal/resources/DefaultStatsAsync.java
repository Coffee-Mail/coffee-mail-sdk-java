package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.models.stats.GetStatsQuery;
import br.com.coffeemail.models.stats.StatsResponse;
import br.com.coffeemail.resources.StatsAsync;
import java.util.concurrent.CompletableFuture;

public final class DefaultStatsAsync implements StatsAsync {

    private final RequestExecutor executor;

    public DefaultStatsAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<StatsResponse>> get() {
        return executor.executeAsync(Operation.get(ApiRoutes.STATS, ResponseType.of(StatsResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<StatsResponse>> get(final GetStatsQuery query) {
        return executor.executeAsync(Operation.get(ApiRoutes.STATS, ResponseType.of(StatsResponse.class))
                .withQuery(query));
    }
}
