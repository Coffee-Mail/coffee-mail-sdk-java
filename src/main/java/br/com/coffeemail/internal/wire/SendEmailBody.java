package br.com.coffeemail.internal.wire;

import br.com.coffeemail.models.emails.EmailAddress;
import br.com.coffeemail.models.emails.EmailAttachment;
import br.com.coffeemail.models.emails.EmailTag;
import br.com.coffeemail.models.emails.EmailType;
import br.com.coffeemail.models.emails.SendEmailInput;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public record SendEmailBody(
        EmailAddress from,
        List<EmailAddress> to,
        @Nullable List<EmailAddress> cc,
        @Nullable List<EmailAddress> bcc,
        @Nullable EmailAddress replyTo,
        @Nullable String subject,
        @Nullable String html,
        @Nullable String text,
        @Nullable Map<String, String> headers,
        @Nullable List<EmailAttachment> attachments,
        @Nullable String templateId,
        @Nullable Map<String, Object> variables,
        @Nullable EmailType emailType,
        @Nullable String suppressionCategory,
        @Nullable List<EmailTag> tags,
        @Nullable Instant scheduledAt,
        @Nullable Map<String, Object> metadata) {

    public static SendEmailBody from(final SendEmailInput input) {
        return new SendEmailBody(
                input.from(),
                List.of(input.to()),
                Present.list(input.cc()),
                Present.list(input.bcc()),
                input.replyTo(),
                input.subject(),
                Present.text(input.html()),
                Present.text(input.text()),
                Present.map(input.headers()),
                Present.list(input.attachments()),
                Present.text(input.templateId()),
                Present.map(input.variables()),
                input.emailType(),
                Present.text(input.suppressionCategory()),
                Present.list(input.tags()),
                input.scheduledAt(),
                Present.map(input.metadata()));
    }
}
