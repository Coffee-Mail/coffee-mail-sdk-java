package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.stats.GetStatsQuery;
import br.com.coffeemail.models.stats.StatsResponse;

/** Estatísticas e métricas de entregabilidade (síncrono). */
public interface Stats {

    /**
     * Retorna totais, taxas e série histórica do período padrão.
     *
     * @return as estatísticas
     */
    CoffeeMailResponse<StatsResponse> get();

    /**
     * Retorna totais, taxas e série histórica com filtros.
     *
     * @param query filtros
     * @return as estatísticas
     */
    CoffeeMailResponse<StatsResponse> get(GetStatsQuery query);
}
