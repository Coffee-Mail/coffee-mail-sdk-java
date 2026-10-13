package br.com.coffeemail.models.templates;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;

/** Variável declarada em um template. */
public record TemplateVariable(
        String name,
        Optional<String> description,
        Optional<TemplateVariableType> type,
        Optional<String> fallbackValue) {

    /**
     * Cria uma variável apenas com o nome.
     *
     * @param name nome usado no template, como em {@code {{ nome }}}
     * @return a variável
     */
    public static TemplateVariable of(final String name) {
        return new TemplateVariable(name, Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Copia a variável com uma descrição.
     *
     * @param value a descrição
     * @return a nova variável
     */
    public TemplateVariable withDescription(final String value) {
        return new TemplateVariable(name, Optional.of(value), type, fallbackValue);
    }

    /**
     * Copia a variável com um tipo.
     *
     * @param value o tipo
     * @return a nova variável
     */
    public TemplateVariable withType(final TemplateVariableType value) {
        return new TemplateVariable(name, description, Optional.of(value), fallbackValue);
    }

    /**
     * Copia a variável com um valor padrão.
     *
     * @param value o valor usado quando a variável não for informada
     * @return a nova variável
     */
    public TemplateVariable withFallbackValue(final String value) {
        return new TemplateVariable(name, description, type, Optional.of(value));
    }

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public TemplateVariable {
        description = Copies.optional(description);
        type = Copies.optional(type);
        fallbackValue = Copies.optional(fallbackValue);
    }
}
