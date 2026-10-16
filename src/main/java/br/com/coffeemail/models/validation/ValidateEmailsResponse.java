package br.com.coffeemail.models.validation;

import br.com.coffeemail.models.support.Copies;
import java.util.List;

/**
 * Resultado de uma validação em lote.
 *
 * @param items resultado de cada endereço, na ordem enviada
 * @param validCount quantidade de endereços aprovados
 * @param invalidCount quantidade de endereços reprovados
 * @param credits saldo de créditos de validação após esta chamada
 */
public record ValidateEmailsResponse(
        List<EmailValidationResult> items, int validCount, int invalidCount, ValidationCredits credits) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public ValidateEmailsResponse {
        items = Copies.list(items);
    }
}
