package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.http.UrlBuilder;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.internal.wire.NamedBodies.VerifySenderBody;
import br.com.coffeemail.models.senders.CreateSenderInput;
import br.com.coffeemail.models.senders.ListSendersResponse;
import br.com.coffeemail.models.senders.SenderDetail;
import br.com.coffeemail.models.senders.VerifySenderResult;
import br.com.coffeemail.resources.SendersAsync;
import java.util.concurrent.CompletableFuture;

public final class DefaultSendersAsync implements SendersAsync {

    private final RequestExecutor executor;

    public DefaultSendersAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<SenderDetail>> create(final CreateSenderInput input) {
        return executor.executeAsync(Operation.post(ApiRoutes.SENDERS, ResponseType.of(SenderDetail.class))
                .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListSendersResponse>> list() {
        return executor.executeAsync(Operation.get(ApiRoutes.SENDERS, ResponseType.of(ListSendersResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<VerifySenderResult>> verify(final String token) {
        return executor.executeAsync(Operation.post(ApiRoutes.SENDERS_VERIFY, ResponseType.of(VerifySenderResult.class))
                .withBody(new VerifySenderBody(token)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<NoContent>> resendVerification(final String id) {
        return executor.executeAsync(
                Operation.post(UrlBuilder.expand(ApiRoutes.SENDER_RESEND_VERIFICATION, id), ResponseType.noContent()));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<NoContent>> delete(final String id) {
        return executor.executeAsync(
                Operation.delete(UrlBuilder.expand(ApiRoutes.SENDER, id), ResponseType.noContent()));
    }
}
