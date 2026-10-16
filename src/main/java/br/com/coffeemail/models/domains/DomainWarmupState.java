package br.com.coffeemail.models.domains;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Fase do aquecimento (warmup) do domínio. */
public enum DomainWarmupState {
    /** {@code warming}: a rampa está ativa e a cota diária vale. */
    WARMING("warming"),
    /** {@code completed}: o aquecimento terminou e não há mais limite diário. */
    COMPLETED("completed"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    DomainWarmupState(final String value) {
        this.value = value;
    }

    /**
     * Valor usado pela API.
     *
     * @return o valor serializado
     */
    @JsonValue
    public String value() {
        return value;
    }
}
