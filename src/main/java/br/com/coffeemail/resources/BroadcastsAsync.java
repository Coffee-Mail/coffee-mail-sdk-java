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
import java.util.concurrent.CompletableFuture;

/**
 * Campanhas enviadas a audiências (assíncrono). Exige chave com permissão {@code full_access}; um 403 vira
 * {@link br.com.coffeemail.errors.PermissionException}.
 */
public interface BroadcastsAsync {

    /**
     * Cria uma campanha.
     *
     * @param input dados da campanha
     * @return a campanha criada
     */
    CompletableFuture<CoffeeMailResponse<BroadcastDetail>> create(CreateBroadcastInput input);

    /**
     * Lista as campanhas mais recentes.
     *
     * @return a primeira página
     */
    CompletableFuture<CoffeeMailResponse<ListBroadcastsResponse>> list();

    /**
     * Lista as campanhas com filtros e paginação por cursor.
     *
     * @param query filtros
     * @return a página
     */
    CompletableFuture<CoffeeMailResponse<ListBroadcastsResponse>> list(ListBroadcastsQuery query);

    /**
     * Busca uma campanha.
     *
     * @param id identificador da campanha
     * @return a campanha
     */
    CompletableFuture<CoffeeMailResponse<BroadcastDetail>> get(String id);

    /**
     * Dispara a campanha para todos os contatos da audiência.
     *
     * @param id identificador da campanha
     * @return o resultado do disparo
     */
    CompletableFuture<CoffeeMailResponse<SendBroadcastResult>> send(String id);

    /**
     * Cancela uma campanha.
     *
     * @param id identificador da campanha
     * @return o resultado do cancelamento
     */
    CompletableFuture<CoffeeMailResponse<CancelBroadcastResult>> cancel(String id);

    /**
     * Dispara um envio de teste da campanha.
     *
     * @param id identificador da campanha
     * @param input destinatário e variáveis
     * @return o resultado do envio
     */
    CompletableFuture<CoffeeMailResponse<TestSendBroadcastResult>> testSend(String id, TestSendBroadcastInput input);
}
