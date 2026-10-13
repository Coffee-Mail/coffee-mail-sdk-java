package br.com.coffeemail.models.common;

import java.util.List;

/**
 * Metadados da chave de API em uso.
 *
 * @param id identificador da chave
 * @param name nome dado à chave
 * @param organisationId organização dona da chave
 * @param scopes escopos concedidos, como {@code full_access}
 */
public record ApiKeyIntrospection(String id, String name, String organisationId, List<String> scopes) {

    /**
     * Normaliza os escopos para uma cópia imutável.
     *
     * @param id identificador da chave
     * @param name nome dado à chave
     * @param organisationId organização dona da chave
     * @param scopes escopos concedidos
     */
    public ApiKeyIntrospection {
        scopes = List.copyOf(scopes);
    }
}
