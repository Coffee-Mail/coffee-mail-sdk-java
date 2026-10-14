package br.com.coffeemail.models.stats;

import br.com.coffeemail.models.support.Copies;
import java.util.List;
import java.util.Optional;

/** Totais, taxas e série histórica de envio. */
public record StatsResponse(
        int totalSent,
        int totalDelivered,
        int totalBounced,
        int totalFailed,
        int totalOpened,
        int totalClicked,
        double deliveryRate,
        double openRate,
        double clickRate,
        Optional<StatsBreakdown> breakdown,
        List<StatsTopClient> topClients,
        List<StatsDataPoint> data) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public StatsResponse {
        breakdown = Copies.optional(breakdown);
        topClients = Copies.list(topClients);
        data = Copies.list(data);
    }
}
