package br.com.coffeemail.models.templates;

import br.com.coffeemail.models.support.Copies;
import java.util.List;
import java.util.Optional;

/**
 * Templates da organização.
 *
 * @param templates templates retornados
 * @param data mesmo conteúdo de {@code templates}, mantido por compatibilidade
 * @param nextCursor cursor da próxima página, quando houver
 */
public record ListTemplatesResponse(
        List<TemplateDetail> templates, List<TemplateDetail> data, Optional<String> nextCursor) {

    /**
     * Mesmo conteúdo de {@link #templates()}.
     *
     * @return os templates
     * @deprecated use {@link #templates()}; mantido por compatibilidade com os outros SDKs
     */
    @Deprecated
    @Override
    public List<TemplateDetail> data() {
        return data;
    }

    /**
     * Preenche {@code templates} e {@code data} com o mesmo conteúdo, venha a API com um ou outro campo.
     *
     * @param templates templates retornados
     * @param data mesmo conteúdo de {@code templates}
     * @param nextCursor cursor da próxima página
     */
    public ListTemplatesResponse {
        final List<TemplateDetail> provided = Copies.nullableList(templates);
        final List<TemplateDetail> items = provided == null ? Copies.list(data) : provided;
        templates = items;
        data = items;
        nextCursor = Copies.optional(nextCursor);
    }
}
