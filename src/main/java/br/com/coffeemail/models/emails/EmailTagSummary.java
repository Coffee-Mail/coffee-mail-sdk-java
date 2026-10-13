package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Tag usada pela organização e seus valores. */
public record EmailTagSummary(String name, List<String> values) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public EmailTagSummary {
        values = Copies.list(values);
    }
}
