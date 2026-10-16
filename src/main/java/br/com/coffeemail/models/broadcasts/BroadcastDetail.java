package br.com.coffeemail.models.broadcasts;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/** Campanha enviada a uma audiência. */
public record BroadcastDetail(
        String id,
        String audienceId,
        Optional<String> audienceName,
        Optional<String> templateId,
        String subject,
        String fromEmail,
        Optional<String> replyTo,
        BroadcastStatus status,
        Optional<Instant> scheduledAt,
        Optional<Instant> sentAt,
        Optional<Instant> cancelledAt,
        int recipientsTotal,
        int recipientsQueued,
        int recipientsFailed,
        Instant createdAt,
        Instant updatedAt,
        Optional<BroadcastMetrics> metrics) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public BroadcastDetail {
        audienceName = Copies.optional(audienceName);
        templateId = Copies.optional(templateId);
        replyTo = Copies.optional(replyTo);
        scheduledAt = Copies.optional(scheduledAt);
        sentAt = Copies.optional(sentAt);
        cancelledAt = Copies.optional(cancelledAt);
        metrics = Copies.optional(metrics);
    }

    /**
     * Construtor sem métricas, mantido por compatibilidade.
     *
     * @param id identificador
     * @param audienceId audiência
     * @param audienceName nome da audiência
     * @param templateId template
     * @param subject assunto
     * @param fromEmail remetente
     * @param replyTo endereço de resposta
     * @param status status
     * @param scheduledAt agendamento
     * @param sentAt conclusão do envio
     * @param cancelledAt cancelamento
     * @param recipientsTotal total de destinatários
     * @param recipientsQueued destinatários enfileirados
     * @param recipientsFailed destinatários com falha
     * @param createdAt criação
     * @param updatedAt última atualização
     */
    public BroadcastDetail(
            final String id,
            final String audienceId,
            final Optional<String> audienceName,
            final Optional<String> templateId,
            final String subject,
            final String fromEmail,
            final Optional<String> replyTo,
            final BroadcastStatus status,
            final Optional<Instant> scheduledAt,
            final Optional<Instant> sentAt,
            final Optional<Instant> cancelledAt,
            final int recipientsTotal,
            final int recipientsQueued,
            final int recipientsFailed,
            final Instant createdAt,
            final Instant updatedAt) {
        this(
                id,
                audienceId,
                audienceName,
                templateId,
                subject,
                fromEmail,
                replyTo,
                status,
                scheduledAt,
                sentAt,
                cancelledAt,
                recipientsTotal,
                recipientsQueued,
                recipientsFailed,
                createdAt,
                updatedAt,
                Optional.empty());
    }
}
