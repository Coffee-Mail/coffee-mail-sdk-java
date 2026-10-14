package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.audiences.BulkAddContactsResult;
import br.com.coffeemail.models.audiences.ContactDetail;
import br.com.coffeemail.models.audiences.CreateContactInput;
import br.com.coffeemail.models.audiences.ListContactsResponse;
import br.com.coffeemail.models.audiences.UpdateContactInput;
import br.com.coffeemail.models.audiences.UpdateContactResult;
import br.com.coffeemail.models.common.OffsetPaginationQuery;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Contatos de uma audiência (assíncrono). */
public interface ContactsAsync {

    /**
     * Cadastra um contato.
     *
     * @param audienceId audiência
     * @param input dados do contato
     * @return o contato criado
     */
    CompletableFuture<CoffeeMailResponse<ContactDetail>> create(String audienceId, CreateContactInput input);

    /**
     * Lista os contatos de uma audiência.
     *
     * @param audienceId audiência
     * @return a primeira página
     */
    CompletableFuture<CoffeeMailResponse<ListContactsResponse>> list(String audienceId);

    /**
     * Lista os contatos de uma audiência com paginação por offset.
     *
     * @param audienceId audiência
     * @param query paginação
     * @return a página
     */
    CompletableFuture<CoffeeMailResponse<ListContactsResponse>> list(String audienceId, OffsetPaginationQuery query);

    /**
     * Adiciona até 10.000 contatos de uma vez.
     *
     * @param audienceId audiência
     * @param contacts contatos
     * @return inseridos, ignorados e rejeitados
     */
    CompletableFuture<CoffeeMailResponse<BulkAddContactsResult>> bulkAdd(
            String audienceId, List<CreateContactInput> contacts);

    /**
     * Atualiza parcialmente um contato.
     *
     * @param audienceId audiência
     * @param contactId contato
     * @param input campos alterados
     * @return o identificador do contato
     */
    CompletableFuture<CoffeeMailResponse<UpdateContactResult>> update(
            String audienceId, String contactId, UpdateContactInput input);

    /**
     * Remove um contato da audiência.
     *
     * @param audienceId audiência
     * @param contactId contato
     * @return {@link NoContent} em caso de sucesso
     */
    CompletableFuture<CoffeeMailResponse<NoContent>> delete(String audienceId, String contactId);
}
