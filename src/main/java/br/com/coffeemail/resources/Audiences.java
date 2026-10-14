package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.audiences.AudienceDetail;
import br.com.coffeemail.models.audiences.BulkAddContactsResult;
import br.com.coffeemail.models.audiences.ContactDetail;
import br.com.coffeemail.models.audiences.CreateAudienceInput;
import br.com.coffeemail.models.audiences.CreateContactInput;
import br.com.coffeemail.models.audiences.ListAudiencesResponse;
import br.com.coffeemail.models.audiences.ListContactsResponse;
import br.com.coffeemail.models.audiences.UpdateAudienceInput;
import br.com.coffeemail.models.audiences.UpdateAudienceResult;
import br.com.coffeemail.models.common.OffsetPaginationQuery;
import java.util.List;

/** Audiências e seus contatos (síncrono). */
public interface Audiences {

    /**
     * Contatos das audiências.
     *
     * @return o recurso de contatos
     */
    Contacts contacts();

    /**
     * Cria uma audiência.
     *
     * @param input dados da audiência
     * @return a audiência criada
     */
    CoffeeMailResponse<AudienceDetail> create(CreateAudienceInput input);

    /**
     * Lista as audiências.
     *
     * @return a primeira página
     */
    CoffeeMailResponse<ListAudiencesResponse> list();

    /**
     * Lista as audiências com paginação por offset.
     *
     * @param query paginação
     * @return a página
     */
    CoffeeMailResponse<ListAudiencesResponse> list(OffsetPaginationQuery query);

    /**
     * Busca uma audiência.
     *
     * @param id identificador da audiência
     * @return a audiência
     */
    CoffeeMailResponse<AudienceDetail> get(String id);

    /**
     * Atualiza parcialmente uma audiência.
     *
     * @param id identificador da audiência
     * @param input campos alterados
     * @return o identificador da audiência
     */
    CoffeeMailResponse<UpdateAudienceResult> update(String id, UpdateAudienceInput input);

    /**
     * Remove a audiência e seus contatos.
     *
     * @param id identificador da audiência
     * @return {@link NoContent} em caso de sucesso
     */
    CoffeeMailResponse<NoContent> delete(String id);

    /**
     * Atalho para {@code contacts().list(audienceId)}.
     *
     * @param audienceId audiência
     * @return a primeira página de contatos
     */
    CoffeeMailResponse<ListContactsResponse> listContacts(String audienceId);

    /**
     * Atalho para {@code contacts().list(audienceId, query)}.
     *
     * @param audienceId audiência
     * @param query paginação
     * @return a página de contatos
     */
    CoffeeMailResponse<ListContactsResponse> listContacts(String audienceId, OffsetPaginationQuery query);

    /**
     * Atalho para {@code contacts().create(audienceId, input)}.
     *
     * @param audienceId audiência
     * @param input dados do contato
     * @return o contato criado
     */
    CoffeeMailResponse<ContactDetail> createContact(String audienceId, CreateContactInput input);

    /**
     * Atalho para {@code contacts().bulkAdd(audienceId, contacts)}.
     *
     * @param audienceId audiência
     * @param input contatos
     * @return inseridos, ignorados e rejeitados
     */
    CoffeeMailResponse<BulkAddContactsResult> bulkAddContacts(String audienceId, List<CreateContactInput> input);
}
