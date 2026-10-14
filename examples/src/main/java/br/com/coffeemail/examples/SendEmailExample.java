package br.com.coffeemail.examples;

import br.com.coffeemail.CoffeeMail;
import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.errors.RateLimitException;
import br.com.coffeemail.errors.ValidationException;
import br.com.coffeemail.models.emails.EmailTag;
import br.com.coffeemail.models.emails.SendEmailInput;
import br.com.coffeemail.models.emails.SendEmailResponse;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public final class SendEmailExample {

    private static final Logger LOGGER = System.getLogger(SendEmailExample.class.getName());

    private SendEmailExample() {}

    public static void main(final String[] args) {
        final CoffeeMail coffeemail = CoffeeMail.create();

        final CoffeeMailResponse<SendEmailResponse> response = coffeemail
                .emails()
                .send(SendEmailInput.builder("Loja <contato@loja.com.br>", "cliente@gmail.com")
                        .subject("Pedido #123 confirmado")
                        .html("<h1>Obrigado pela compra!</h1>")
                        .tags(new EmailTag("pedido", "123"))
                        .idempotencyKey("pedido-123")
                        .sandbox(true)
                        .build());

        final String outcome = response.fold(
                sent -> "E-mail " + sent.id() + " enfileirado como "
                        + sent.status().value(),
                SendEmailExample::describe);
        LOGGER.log(Level.INFO, outcome);
    }

    private static String describe(final Exception error) {
        if (error instanceof RateLimitException rateLimit) {
            return "Limite atingido; tente novamente em "
                    + rateLimit.retryAfterSeconds().orElse(1) + "s";
        }
        if (error instanceof ValidationException validation) {
            return "Payload inválido: " + validation.getMessage() + " " + validation.details();
        }
        return "Falha no envio: " + error.getMessage();
    }
}
