package br.com.coffeemail.models.audiences;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Página de contatos. */
public record ListContactsResponse(List<ContactDetail> contacts, int total) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListContactsResponse {
        contacts = Copies.list(contacts);
    }
}
