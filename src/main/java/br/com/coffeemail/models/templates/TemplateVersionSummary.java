package br.com.coffeemail.models.templates;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/**
 * Resumo de uma versão gravada de um template.
 *
 * @param version número da versão, crescente a partir de 1
 * @param name nome do template nesta versão
 * @param subject assunto do template nesta versão
 * @param createdBy usuário que gerou a versão; vazio quando veio de uma chave de API
 * @param createdAt quando a versão foi gravada
 */
public record TemplateVersionSummary(
        int version, String name, Optional<String> subject, Optional<String> createdBy, Instant createdAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public TemplateVersionSummary {
        subject = Copies.optional(subject);
        createdBy = Copies.optional(createdBy);
    }
}
