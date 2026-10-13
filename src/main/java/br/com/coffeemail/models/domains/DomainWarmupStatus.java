package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;

/** Progresso do aquecimento (warmup) do domínio. */
public record DomainWarmupStatus(
        String domainId,
        int currentDay,
        int dailyQuota,
        int sentToday,
        int remainingToday,
        int quotaUsedPercent,
        Optional<String> lastSendDate) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainWarmupStatus {
        lastSendDate = Copies.optional(lastSendDate);
    }
}
