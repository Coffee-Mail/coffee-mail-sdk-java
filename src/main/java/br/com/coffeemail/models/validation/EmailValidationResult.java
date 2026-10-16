package br.com.coffeemail.models.validation;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;

/**
 * Resultado da validação de um endereço.
 *
 * @param email endereço validado, como foi enviado
 * @param formatValid indica se a sintaxe do endereço é válida
 * @param mxValid indica se o domínio tem servidor de e-mail (MX) que aceita mensagens
 * @param isDisposable indica se o domínio é de e-mail descartável; endereço descartável é reprovado
 * @param reason motivo da reprovação, quando houver: {@code invalid_format}, {@code disposable_email} ou
 *     {@code no_mx_record}
 */
public record EmailValidationResult(
        String email, boolean formatValid, boolean mxValid, boolean isDisposable, Optional<String> reason) {

    /** Normaliza campos ausentes para valores vazios e imutáveis. */
    public EmailValidationResult {
        reason = Copies.optional(reason);
    }
}
