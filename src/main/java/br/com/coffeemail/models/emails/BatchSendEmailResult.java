package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;

/** Resultado de um item do envio em lote: {@code ok} indica se foi enfileirado. */
public record BatchSendEmailResult(boolean ok, Optional<SendEmailResponse> data, Optional<String> error) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public BatchSendEmailResult {
        data = Copies.optional(data);
        error = Copies.optional(error);
    }
}
