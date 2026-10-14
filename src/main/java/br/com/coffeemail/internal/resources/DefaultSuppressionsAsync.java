package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.http.UrlBuilder;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.internal.wire.NamedBodies.BulkSuppressionsBody;
import br.com.coffeemail.models.suppressions.BulkCreateSuppressionsResult;
import br.com.coffeemail.models.suppressions.BulkSuppressionEntry;
import br.com.coffeemail.models.suppressions.CreateSuppressionInput;
import br.com.coffeemail.models.suppressions.ListSuppressionsQuery;
import br.com.coffeemail.models.suppressions.ListSuppressionsResponse;
import br.com.coffeemail.models.suppressions.SuppressionDetail;
import br.com.coffeemail.resources.SuppressionsAsync;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class DefaultSuppressionsAsync implements SuppressionsAsync {

    private final RequestExecutor executor;

    public DefaultSuppressionsAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListSuppressionsResponse>> list() {
        return executor.executeAsync(
                Operation.get(ApiRoutes.SUPPRESSIONS, ResponseType.of(ListSuppressionsResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListSuppressionsResponse>> list(final ListSuppressionsQuery query) {
        return executor.executeAsync(
                Operation.get(ApiRoutes.SUPPRESSIONS, ResponseType.of(ListSuppressionsResponse.class))
                        .withQuery(query));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<SuppressionDetail>> get(final String id) {
        return executor.executeAsync(
                Operation.get(UrlBuilder.expand(ApiRoutes.SUPPRESSION, id), ResponseType.of(SuppressionDetail.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<SuppressionDetail>> create(final CreateSuppressionInput input) {
        return executor.executeAsync(Operation.post(ApiRoutes.SUPPRESSIONS, ResponseType.of(SuppressionDetail.class))
                .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<NoContent>> delete(final String id) {
        return executor.executeAsync(
                Operation.delete(UrlBuilder.expand(ApiRoutes.SUPPRESSION, id), ResponseType.noContent()));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<NoContent>> reactivate(final String id) {
        return executor.executeAsync(
                Operation.post(UrlBuilder.expand(ApiRoutes.SUPPRESSION_REACTIVATE, id), ResponseType.noContent()));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<BulkCreateSuppressionsResult>> bulkCreate(
            final List<BulkSuppressionEntry> entries) {
        return executor.executeAsync(
                Operation.post(ApiRoutes.SUPPRESSIONS_BULK, ResponseType.of(BulkCreateSuppressionsResult.class))
                        .withBody(new BulkSuppressionsBody(List.copyOf(entries))));
    }
}
