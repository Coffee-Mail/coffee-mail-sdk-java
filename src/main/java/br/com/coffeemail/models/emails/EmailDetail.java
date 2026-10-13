package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Detalhes de um e-mail processado ou em andamento. */
public record EmailDetail(
        String id,
        String from,
        List<String> to,
        List<String> cc,
        List<String> bcc,
        Optional<String> subject,
        EmailStatus status,
        int attempts,
        Optional<String> lastError,
        Optional<String> messageId,
        Instant createdAt,
        Optional<Instant> scheduledAt,
        Optional<Instant> sentAt,
        Optional<Instant> deliveredAt,
        Optional<Instant> bouncedAt,
        Optional<Instant> failedAt,
        Optional<Instant> complainedAt,
        Optional<Instant> suppressedAt,
        Optional<Instant> openedAt,
        Optional<Instant> firstClickedAt,
        int openCount,
        int clickCount,
        int proxyOpenCount,
        List<EmailTag> tags,
        Optional<String> html,
        Optional<String> text,
        Optional<ApiKeyRef> apiKey) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public EmailDetail {
        to = Copies.list(to);
        cc = Copies.list(cc);
        bcc = Copies.list(bcc);
        subject = Copies.optional(subject);
        lastError = Copies.optional(lastError);
        messageId = Copies.optional(messageId);
        scheduledAt = Copies.optional(scheduledAt);
        sentAt = Copies.optional(sentAt);
        deliveredAt = Copies.optional(deliveredAt);
        bouncedAt = Copies.optional(bouncedAt);
        failedAt = Copies.optional(failedAt);
        complainedAt = Copies.optional(complainedAt);
        suppressedAt = Copies.optional(suppressedAt);
        openedAt = Copies.optional(openedAt);
        firstClickedAt = Copies.optional(firstClickedAt);
        tags = Copies.list(tags);
        html = Copies.optional(html);
        text = Copies.optional(text);
        apiKey = Copies.optional(apiKey);
    }
}
