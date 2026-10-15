package br.com.coffeemail.models.audiences;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/** Contato de uma audiência. */
public record ContactDetail(
        String id,
        String email,
        Optional<String> firstName,
        Optional<String> lastName,
        Map<String, Object> metadata,
        Instant createdAt,
        Optional<Boolean> subscribed) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ContactDetail {
        firstName = Copies.optional(firstName);
        lastName = Copies.optional(lastName);
        metadata = Copies.map(metadata);
        subscribed = Copies.optional(subscribed);
    }

    /**
     * Construtor sem o estado de inscrição, mantido por compatibilidade.
     *
     * @param id identificador
     * @param email endereço
     * @param firstName primeiro nome
     * @param lastName sobrenome
     * @param metadata dados livres
     * @param createdAt criação
     */
    public ContactDetail(
            final String id,
            final String email,
            final Optional<String> firstName,
            final Optional<String> lastName,
            final Map<String, Object> metadata,
            final Instant createdAt) {
        this(id, email, firstName, lastName, metadata, createdAt, Optional.empty());
    }

    /**
     * Indica se o contato ainda recebe broadcasts; ausente equivale a inscrito.
     *
     * @return {@code false} quando o contato se descadastrou
     */
    public boolean isSubscribed() {
        return subscribed.orElse(true);
    }
}
