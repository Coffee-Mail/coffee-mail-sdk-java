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

/** Envio e consulta de e-mails transacionais (síncrono). */
public interface Emails {

    /**
     * Envia um e-mail transacional.
     *
     * @param input dados do e-mail
     * @return o e-mail enfileirado
     */
    CoffeeMailResponse<SendEmailResponse> send(SendEmailInput input);

    /**
     * Envia vários e-mails em uma única requisição.
     *
     * @param inputs dados dos e-mails
     * @return o resultado de cada item, na mesma ordem
     */
    CoffeeMailResponse<List<BatchSendEmailResult>> sendBatch(List<SendEmailInput> inputs);

    /**
     * Busca um e-mail pelo identificador.
     *
     * @param id identificador do e-mail
     * @return o e-mail
     */
    CoffeeMailResponse<EmailDetail> get(String id);

    /**
     * Lista os e-mails mais recentes.
     *
     * @return a primeira página
     */
    CoffeeMailResponse<ListEmailsResponse> list();

    /**
     * Lista e-mails com filtros e paginação por cursor.
     *
     * @param query filtros
     * @return a página
     */
    CoffeeMailResponse<ListEmailsResponse> list(ListEmailsQuery query);

    /**
     * Consulta a linha do tempo de eventos de um e-mail.
     *
     * @param id identificador do e-mail
     * @return os eventos
     */
    CoffeeMailResponse<EmailEventsResponse> getEvents(String id);

    /**
     * Lista as tags já usadas pela organização.
     *
     * @return as tags e seus valores
     */
    CoffeeMailResponse<ListEmailTagsResponse> getTags();

    /**
     * Cancela um e-mail agendado.
     *
     * @param id identificador do e-mail
     * @return {@link NoContent} em caso de sucesso
     */
    CoffeeMailResponse<NoContent> cancel(String id);

    /**
     * Reenvia um e-mail existente.
     *
     * @param id identificador do e-mail
     * @return o novo envio enfileirado
     */
    CoffeeMailResponse<SendEmailResponse> resend(String id);
}
