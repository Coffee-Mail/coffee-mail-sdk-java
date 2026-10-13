package br.com.coffeemail.models.domains;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;

/** Resultado de uma verificação de saúde. */
public record DomainHealthCheck(boolean ok, String expected, Optional<String> got) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public DomainHealthCheck {
        got = Copies.optional(got);
    }
}
