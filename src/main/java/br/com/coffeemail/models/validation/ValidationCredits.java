package br.com.coffeemail.models.validation;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;

/**
 * Saldo mensal de créditos de validação da organização, já descontada a chamada atual.
 *
 * @param limit créditos do mês; vazio quando o plano é ilimitado
 * @param used créditos usados no mês
 * @param remaining créditos restantes no mês; vazio quando o plano é ilimitado
 */
public record ValidationCredits(Optional<Integer> limit, int used, Optional<Integer> remaining) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ValidationCredits {
        limit = Copies.optional(limit);
        remaining = Copies.optional(remaining);
    }

    /**
     * Indica se o plano não tem limite mensal de validações.
     *
     * @return {@code true} quando não há limite
     */
    public boolean isUnlimited() {
        return limit.isEmpty();
    }
}
