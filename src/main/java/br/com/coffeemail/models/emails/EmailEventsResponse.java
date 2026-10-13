package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/** Linha do tempo de um e-mail. */
public record EmailEventsResponse(String id, List<EmailTimelineEvent> events) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public EmailEventsResponse {
        events = Copies.list(events);
    }
}
