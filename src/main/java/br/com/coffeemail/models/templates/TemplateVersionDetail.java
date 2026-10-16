package br.com.coffeemail.models.templates;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Conteúdo completo de uma versão gravada de um template.
 *
 * @param version número da versão, crescente a partir de 1
 * @param name nome do template nesta versão
 * @param subject assunto nesta versão
 * @param createdBy usuário que gerou a versão; vazio quando veio de uma chave de API
 * @param createdAt quando a versão foi gravada
 * @param templateId identificador do template
 * @param preheader preheader nesta versão
 * @param html HTML nesta versão
 * @param contentJson estado do editor visual nesta versão
 * @param textPayload versão em texto simples nesta versão
 * @param variables variáveis declaradas nesta versão
 * @param format formato do código-fonte
 * @param sourceLocale idioma de origem do conteúdo
 */
public record TemplateVersionDetail(
        int version,
        String name,
        Optional<String> subject,
        Optional<String> createdBy,
        Instant createdAt,
        String templateId,
        Optional<String> preheader,
        String html,
        Optional<Map<String, Object>> contentJson,
        Optional<String> textPayload,
        List<TemplateVariable> variables,
        TemplateFormat format,
        String sourceLocale) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public TemplateVersionDetail {
        subject = Copies.optional(subject);
        createdBy = Copies.optional(createdBy);
        preheader = Copies.optional(preheader);
        contentJson = Copies.optional(contentJson);
        textPayload = Copies.optional(textPayload);
        variables = Copies.list(variables);
    }

    /**
     * Resumo desta versão, no mesmo formato da listagem.
     *
     * @return o resumo
     */
    public TemplateVersionSummary summary() {
        return new TemplateVersionSummary(version, name, subject, createdBy, createdAt);
    }
}
