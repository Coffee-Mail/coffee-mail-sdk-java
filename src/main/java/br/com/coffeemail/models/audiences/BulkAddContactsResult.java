package br.com.coffeemail.models.audiences;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Resultado da inserção de contatos em lote. */
public record BulkAddContactsResult(int inserted, int skipped, List<BulkAddContactError> errors) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public BulkAddContactsResult {
        errors = Copies.list(errors);
    }
}
