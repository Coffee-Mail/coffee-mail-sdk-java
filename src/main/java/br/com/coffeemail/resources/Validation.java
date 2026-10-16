package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.validation.ValidateEmailsResponse;
import java.util.List;

/** Validação de endereços de e-mail antes do envio (síncrono). */
public interface Validation {

    /**
     * Valida até 100 endereços: sintaxe, servidor de e-mail (MX) do domínio e domínio descartável. Cada endereço
     * consome um crédito de validação do mês.
     *
     * <p>Com os créditos do mês esgotados, falha com {@link br.com.coffeemail.errors.RateLimitException} e
     * {@code VALIDATION_CREDITS_EXHAUSTED} em {@link br.com.coffeemail.errors.CoffeeMailException#apiCode()}. Esse
     * 429 não se resolve com nova tentativa: o saldo só volta no mês seguinte ou com troca de plano.
     *
     * @param emails de 1 a 100 endereços
     * @return o resultado de cada endereço, os totais e o saldo de créditos
     */
    CoffeeMailResponse<ValidateEmailsResponse> validate(List<String> emails);
}
