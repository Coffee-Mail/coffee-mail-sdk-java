package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Map;

/** Evento da linha do tempo de um e-mail. */
public record EmailTimelineEvent(String type, Instant timestamp, Map<String, Object> metadata) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public EmailTimelineEvent {
        metadata = Copies.map(metadata);
    }
}
