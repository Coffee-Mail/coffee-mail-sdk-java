package br.com.coffeemail.models.senders;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Remetentes da organização. */
public record ListSendersResponse(List<SenderDetail> senders) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ListSendersResponse {
        senders = Copies.list(senders);
    }
}
