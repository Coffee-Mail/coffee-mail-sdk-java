package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.emails.BatchSendEmailResult;
import br.com.coffeemail.models.emails.EmailDetail;
import br.com.coffeemail.models.emails.EmailEventsResponse;
import br.com.coffeemail.models.emails.ListEmailTagsResponse;
import br.com.coffeemail.models.emails.ListEmailsQuery;
import br.com.coffeemail.models.emails.ListEmailsResponse;
import br.com.coffeemail.models.emails.SendEmailInput;
import br.com.coffeemail.models.emails.SendEmailResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Envio e consulta de e-mails transacionais (assíncrono). */
public interface EmailsAsync {

    /**
     * Envia um e-mail transacional.
     *
     * @param input dados do e-mail
     * @return o e-mail enfileirado
     */
    CompletableFuture<CoffeeMailResponse<SendEmailResponse>> send(SendEmailInput input);

    /**
     * Envia vários e-mails em uma única requisição.
     *
     * @param inputs dados dos e-mails
     * @return o resultado de cada item, na mesma ordem
     */
    CompletableFuture<CoffeeMailResponse<List<BatchSendEmailResult>>> sendBatch(List<SendEmailInput> inputs);

    /**
     * Busca um e-mail pelo identificador.
     *
     * @param id identificador do e-mail
     * @return o e-mail
     */
    CompletableFuture<CoffeeMailResponse<EmailDetail>> get(String id);

    /**
     * Lista os e-mails mais recentes.
     *
     * @return a primeira página
     */
    CompletableFuture<CoffeeMailResponse<ListEmailsResponse>> list();

    /**
     * Lista e-mails com filtros e paginação por cursor.
     *
     * @param query filtros
     * @return a página
     */
    CompletableFuture<CoffeeMailResponse<ListEmailsResponse>> list(ListEmailsQuery query);

    /**
     * Consulta a linha do tempo de eventos de um e-mail.
     *
     * @param id identificador do e-mail
     * @return os eventos
     */
    CompletableFuture<CoffeeMailResponse<EmailEventsResponse>> getEvents(String id);

    /**
     * Lista as tags já usadas pela organização.
     *
     * @return as tags e seus valores
     */
    CompletableFuture<CoffeeMailResponse<ListEmailTagsResponse>> getTags();

    /**
     * Cancela um e-mail agendado.
     *
     * @param id identificador do e-mail
     * @return {@link NoContent} em caso de sucesso
     */
    CompletableFuture<CoffeeMailResponse<NoContent>> cancel(String id);

    /**
     * Reenvia um e-mail existente.
     *
     * @param id identificador do e-mail
     * @return o novo envio enfileirado
     */
    CompletableFuture<CoffeeMailResponse<SendEmailResponse>> resend(String id);
}
