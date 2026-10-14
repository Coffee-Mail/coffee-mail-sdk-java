package br.com.coffeemail.examples;

import br.com.coffeemail.CoffeeMailAsync;
import br.com.coffeemail.models.emails.SendEmailInput;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class AsyncExample {

    private static final Logger LOGGER = System.getLogger(AsyncExample.class.getName());

    private AsyncExample() {}

    public static void main(final String[] args) {
        final CoffeeMailAsync coffeemail = CoffeeMailAsync.create();
        final List<String> recipients = List.of("ana@gmail.com", "bruno@gmail.com");

        final List<CompletableFuture<Void>> sends = recipients.stream()
                .map(recipient -> coffeemail
                        .emails()
                        .send(SendEmailInput.builder("contato@loja.com.br", recipient)
                                .templateId("7c8d3f2b-8c1e-4a7d-be5f-3e4f5a601c2d")
                                .build())
                        .thenAccept(response -> response.data()
                                .ifPresent(sent ->
                                        LOGGER.log(Level.INFO, "Enviado para " + recipient + ": " + sent.id()))))
                .toList();

        CompletableFuture.allOf(sends.toArray(CompletableFuture[]::new)).join();
    }
}
