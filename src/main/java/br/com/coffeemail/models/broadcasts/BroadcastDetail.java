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
        Instant updatedAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public BroadcastDetail {
        audienceName = Copies.optional(audienceName);
        templateId = Copies.optional(templateId);
        replyTo = Copies.optional(replyTo);
        scheduledAt = Copies.optional(scheduledAt);
        sentAt = Copies.optional(sentAt);
        cancelledAt = Copies.optional(cancelledAt);
    }
}
