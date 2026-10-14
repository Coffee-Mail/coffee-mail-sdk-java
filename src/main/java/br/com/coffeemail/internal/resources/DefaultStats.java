package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.stats.GetStatsQuery;
import br.com.coffeemail.models.stats.StatsResponse;
import br.com.coffeemail.resources.Stats;
import br.com.coffeemail.resources.StatsAsync;

public final class DefaultStats implements Stats {

    private final StatsAsync async;

    public DefaultStats(final StatsAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<StatsResponse> get() {
        return async.get().join();
    }

    @Override
    public CoffeeMailResponse<StatsResponse> get(final GetStatsQuery query) {
        return async.get(query).join();
    }
}
