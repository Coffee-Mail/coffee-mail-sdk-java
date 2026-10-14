package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.stats.GetStatsQuery;
import br.com.coffeemail.models.stats.StatsResponse;
import java.util.concurrent.CompletableFuture;

/** Estatísticas e métricas de entregabilidade (assíncrono). */
public interface StatsAsync {

    /**
     * Retorna totais, taxas e série histórica do período padrão.
     *
     * @return as estatísticas
     */
    CompletableFuture<CoffeeMailResponse<StatsResponse>> get();

    /**
     * Retorna totais, taxas e série histórica com filtros.
     *
     * @param query filtros
     * @return as estatísticas
     */
    CompletableFuture<CoffeeMailResponse<StatsResponse>> get(GetStatsQuery query);
}
