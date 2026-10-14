package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.broadcasts.BroadcastDetail;
import br.com.coffeemail.models.broadcasts.CancelBroadcastResult;
import br.com.coffeemail.models.broadcasts.CreateBroadcastInput;
import br.com.coffeemail.models.broadcasts.ListBroadcastsQuery;
import br.com.coffeemail.models.broadcasts.ListBroadcastsResponse;
import br.com.coffeemail.models.broadcasts.SendBroadcastResult;
import br.com.coffeemail.models.broadcasts.TestSendBroadcastInput;
import br.com.coffeemail.models.broadcasts.TestSendBroadcastResult;

/**
 * Campanhas enviadas a audiências (síncrono). Exige chave com permissão {@code full_access}; um 403 vira
 * {@link br.com.coffeemail.errors.PermissionException}.
 */
public interface Broadcasts {

    /**
     * Cria uma campanha.
     *
     * @param input dados da campanha
     * @return a campanha criada
     */
    CoffeeMailResponse<BroadcastDetail> create(CreateBroadcastInput input);

    /**
     * Lista as campanhas mais recentes.
     *
     * @return a primeira página
     */
    CoffeeMailResponse<ListBroadcastsResponse> list();

    /**
     * Lista as campanhas com filtros e paginação por cursor.
     *
     * @param query filtros
     * @return a página
     */
    CoffeeMailResponse<ListBroadcastsResponse> list(ListBroadcastsQuery query);

    /**
     * Busca uma campanha.
     *
     * @param id identificador da campanha
     * @return a campanha
     */
    CoffeeMailResponse<BroadcastDetail> get(String id);

    /**
     * Dispara a campanha para todos os contatos da audiência.
     *
     * @param id identificador da campanha
     * @return o resultado do disparo
     */
    CoffeeMailResponse<SendBroadcastResult> send(String id);

    /**
     * Cancela uma campanha.
     *
     * @param id identificador da campanha
     * @return o resultado do cancelamento
     */
    CoffeeMailResponse<CancelBroadcastResult> cancel(String id);

    /**
     * Dispara um envio de teste da campanha.
     *
     * @param id identificador da campanha
     * @param input destinatário e variáveis
     * @return o resultado do envio
     */
    CoffeeMailResponse<TestSendBroadcastResult> testSend(String id, TestSendBroadcastInput input);
}
