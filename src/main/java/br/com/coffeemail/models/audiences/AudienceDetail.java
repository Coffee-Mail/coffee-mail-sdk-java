package br.com.coffeemail.models.audiences;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Optional;

/** Audiência (lista segmentada de contatos). */
public record AudienceDetail(
        String id,
        String name,
        Optional<String> description,
        boolean active,
        int totalContacts,
        int contactsCount,
        Instant createdAt,
        Instant updatedAt) {

    /**
     * Mesmo valor de {@link #totalContacts()}.
     *
     * @return a quantidade de contatos
     * @deprecated use {@link #totalContacts()}; mantido por compatibilidade com os outros SDKs
     */
    @Deprecated
    @Override
    public int contactsCount() {
        return contactsCount;
    }

    /** Normaliza campos ausentes e iguala {@code totalContacts} e {@code contactsCount}, venha a API com um ou outro. */
    public AudienceDetail {
        description = Copies.optional(description);
        final int count = totalContacts != 0 ? totalContacts : contactsCount;
        totalContacts = count;
        contactsCount = count;
    }
}
