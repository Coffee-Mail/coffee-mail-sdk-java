package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.suppressions.BulkCreateSuppressionsResult;
import br.com.coffeemail.models.suppressions.BulkSuppressionEntry;
import br.com.coffeemail.models.suppressions.CreateSuppressionInput;
import br.com.coffeemail.models.suppressions.ListSuppressionsQuery;
import br.com.coffeemail.models.suppressions.ListSuppressionsResponse;
import br.com.coffeemail.models.suppressions.SuppressionDetail;
import java.util.List;

/** Lista de supressão: endereços que não devem receber e-mails (síncrono). */
public interface Suppressions {

    /**
     * Lista as supressões mais recentes.
     *
     * @return a primeira página
     */
    CoffeeMailResponse<ListSuppressionsResponse> list();

    /**
     * Lista as supressões com filtros e paginação por cursor (máximo de 200 por página).
     *
     * @param query filtros
     * @return a página
     */
    CoffeeMailResponse<ListSuppressionsResponse> list(ListSuppressionsQuery query);

    /**
     * Busca uma supressão.
     *
     * @param id identificador da supressão
     * @return a supressão
     */
    CoffeeMailResponse<SuppressionDetail> get(String id);

    /**
     * Cria uma supressão manual.
     *
     * @param input endereço, motivo e expiração
     * @return a supressão criada
     */
    CoffeeMailResponse<SuppressionDetail> create(CreateSuppressionInput input);

    /**
     * Remove uma supressão definitivamente.
     *
     * @param id identificador da supressão
     * @return {@link NoContent} em caso de sucesso
     */
    CoffeeMailResponse<NoContent> delete(String id);

    /**
     * Desfaz a supressão, permitindo novos envios ao endereço.
     *
     * @param id identificador da supressão
     * @return {@link NoContent} em caso de sucesso
     */
    CoffeeMailResponse<NoContent> reactivate(String id);

    /**
     * Cria até 1.000 supressões de uma vez.
     *
     * @param entries endereços e motivos
     * @return a quantidade inserida
     */
    CoffeeMailResponse<BulkCreateSuppressionsResult> bulkCreate(List<BulkSuppressionEntry> entries);
}
