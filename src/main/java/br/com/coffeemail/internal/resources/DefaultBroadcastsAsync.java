package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailLocale;
import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.errors.PermissionRemapper;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.http.UrlBuilder;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.models.broadcasts.BroadcastDetail;
import br.com.coffeemail.models.broadcasts.CancelBroadcastResult;
import br.com.coffeemail.models.broadcasts.CreateBroadcastInput;
import br.com.coffeemail.models.broadcasts.ListBroadcastsQuery;
import br.com.coffeemail.models.broadcasts.ListBroadcastsResponse;
import br.com.coffeemail.models.broadcasts.SendBroadcastResult;
import br.com.coffeemail.models.broadcasts.TestSendBroadcastInput;
import br.com.coffeemail.models.broadcasts.TestSendBroadcastResult;
import br.com.coffeemail.resources.BroadcastsAsync;
import java.util.concurrent.CompletableFuture;

public final class DefaultBroadcastsAsync implements BroadcastsAsync {

    private static final String CREATE = "broadcasts.create";
    private static final String LIST = "broadcasts.list";
    private static final String GET = "broadcasts.get";
    private static final String SEND = "broadcasts.send";
    private static final String CANCEL = "broadcasts.cancel";
    private static final String TEST_SEND = "broadcasts.testSend";

    private final RequestExecutor executor;
    private final CoffeeMailLocale locale;

    public DefaultBroadcastsAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
        this.locale = runtime.config().locale();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<BroadcastDetail>> create(final CreateBroadcastInput input) {
        return run(
                Operation.post(ApiRoutes.BROADCASTS, ResponseType.of(BroadcastDetail.class))
                        .withBody(input),
                CREATE);
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListBroadcastsResponse>> list() {
        return run(Operation.get(ApiRoutes.BROADCASTS, ResponseType.of(ListBroadcastsResponse.class)), LIST);
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListBroadcastsResponse>> list(final ListBroadcastsQuery query) {
        return run(
                Operation.get(ApiRoutes.BROADCASTS, ResponseType.of(ListBroadcastsResponse.class))
                        .withQuery(query),
                LIST);
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<BroadcastDetail>> get(final String id) {
        return run(
                Operation.get(UrlBuilder.expand(ApiRoutes.BROADCAST, id), ResponseType.of(BroadcastDetail.class)), GET);
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<SendBroadcastResult>> send(final String id) {
        return run(
                Operation.post(
                        UrlBuilder.expand(ApiRoutes.BROADCAST_SEND, id), ResponseType.of(SendBroadcastResult.class)),
                SEND);
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<CancelBroadcastResult>> cancel(final String id) {
        return run(
                Operation.post(
                        UrlBuilder.expand(ApiRoutes.BROADCAST_CANCEL, id),
                        ResponseType.of(CancelBroadcastResult.class)),
                CANCEL);
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<TestSendBroadcastResult>> testSend(
            final String id, final TestSendBroadcastInput input) {
        return run(
                Operation.post(
                                UrlBuilder.expand(ApiRoutes.BROADCAST_TEST_SEND, id),
                                ResponseType.of(TestSendBroadcastResult.class))
                        .withBody(input),
                TEST_SEND);
    }

    private <T> CompletableFuture<CoffeeMailResponse<T>> run(final Operation<T> operation, final String name) {
        return executor.executeAsync(operation.withErrorMapping(PermissionRemapper.requireFullAccess(locale, name)));
    }
}
