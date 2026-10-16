package br.com.coffeemail.models.templates;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/**
 * Página de versões de um template.
 *
 * @param data versões, da mais recente para a mais antiga
 * @param total total de versões do template
 */
public record ListTemplateVersionsResponse(List<TemplateVersionSummary> data, int total) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListTemplateVersionsResponse {
        data = Copies.list(data);
    }
}
