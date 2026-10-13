package br.com.coffeemail.models.templates;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Template de e-mail cadastrado na plataforma. */
public record TemplateDetail(
        String id,
        String organisationId,
        String name,
        Optional<String> alias,
        Optional<String> subject,
        Optional<String> preheader,
        String html,
        Optional<Map<String, Object>> contentJson,
        Optional<String> textPayload,
        List<TemplateVariable> variables,
        boolean isActive,
        TemplateFormat format,
        String sourceLocale,
        Instant createdAt,
        Instant updatedAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public TemplateDetail {
        alias = Copies.optional(alias);
        subject = Copies.optional(subject);
        preheader = Copies.optional(preheader);
        contentJson = Copies.optional(contentJson);
        textPayload = Copies.optional(textPayload);
        variables = Copies.list(variables);
    }
}
