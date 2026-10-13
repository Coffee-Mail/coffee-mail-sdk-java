package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.util.List;
import java.util.Optional;

/** Página da listagem de e-mails. */
public record ListEmailsResponse(List<EmailDetail> emails, Optional<String> nextCursor) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListEmailsResponse {
        emails = Copies.list(emails);
        nextCursor = Copies.optional(nextCursor);
    }
}
