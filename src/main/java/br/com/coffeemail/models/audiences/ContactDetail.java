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
        Instant createdAt) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ContactDetail {
        firstName = Copies.optional(firstName);
        lastName = Copies.optional(lastName);
        metadata = Copies.map(metadata);
    }
}
