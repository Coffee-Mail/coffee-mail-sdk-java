package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/** Progresso do aquecimento (warmup) do domínio. */
public record DomainWarmupStatus(
        String domainId,
        int currentDay,
        int dailyQuota,
        int sentToday,
        int remainingToday,
        int quotaUsedPercent,
        Optional<String> lastSendDate,
        Optional<DomainWarmupState> status,
        Optional<Integer> totalDays,
        Optional<Instant> startedAt,
        Optional<Instant> completedAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainWarmupStatus {
        lastSendDate = Copies.optional(lastSendDate);
        status = Copies.optional(status);
        totalDays = Copies.optional(totalDays);
        startedAt = Copies.optional(startedAt);
        completedAt = Copies.optional(completedAt);
    }

    /**
     * Construtor sem fase, duração e datas do aquecimento, mantido por compatibilidade.
     *
     * @param domainId identificador do domínio
     * @param currentDay dia atual do aquecimento
     * @param dailyQuota limite diário de envios
     * @param sentToday envios de hoje
     * @param remainingToday envios restantes hoje
     * @param quotaUsedPercent percentual da cota usado
     * @param lastSendDate data do último envio
     */
    public DomainWarmupStatus(
            final String domainId,
            final int currentDay,
            final int dailyQuota,
            final int sentToday,
            final int remainingToday,
            final int quotaUsedPercent,
            final Optional<String> lastSendDate) {
        this(
                domainId,
                currentDay,
                dailyQuota,
                sentToday,
                remainingToday,
                quotaUsedPercent,
                lastSendDate,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
}
