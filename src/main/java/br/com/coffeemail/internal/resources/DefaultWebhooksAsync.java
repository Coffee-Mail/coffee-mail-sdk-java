package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.http.UrlBuilder;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.internal.wire.NamedBodies.ToggleWebhookBody;
import br.com.coffeemail.models.webhooks.CreateWebhookInput;
import br.com.coffeemail.models.webhooks.CreatedWebhookDetail;
import br.com.coffeemail.models.webhooks.ListWebhookDeliveriesQuery;
import br.com.coffeemail.models.webhooks.ListWebhookDeliveriesResponse;
import br.com.coffeemail.models.webhooks.ListWebhooksQuery;
import br.com.coffeemail.models.webhooks.ListWebhooksResponse;
import br.com.coffeemail.models.webhooks.RotateWebhookSecretResult;
import br.com.coffeemail.models.webhooks.TestWebhookResult;
import br.com.coffeemail.models.webhooks.ToggleWebhookResult;
import br.com.coffeemail.models.webhooks.ToggleableWebhookStatus;
import br.com.coffeemail.models.webhooks.UpdateWebhookInput;
import br.com.coffeemail.models.webhooks.WebhookDeliveryDetail;
import br.com.coffeemail.models.webhooks.WebhookDetail;
import br.com.coffeemail.resources.WebhooksAsync;
import br.com.coffeemail.webhooks.VerifyWebhookInput;
import br.com.coffeemail.webhooks.WebhookVerifier;
import java.util.concurrent.CompletableFuture;

public final class DefaultWebhooksAsync implements WebhooksAsync {

    private final RequestExecutor executor;
    private final WebhookVerifier verifier;

    public DefaultWebhooksAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
        this.verifier =
                WebhookVerifier.builder().clock(runtime.config().clock()).build();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<CreatedWebhookDetail>> create(final CreateWebhookInput input) {
        return executor.executeAsync(Operation.post(ApiRoutes.WEBHOOKS, ResponseType.of(CreatedWebhookDetail.class))
                .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListWebhooksResponse>> list() {
        return executor.executeAsync(Operation.get(ApiRoutes.WEBHOOKS, ResponseType.of(ListWebhooksResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListWebhooksResponse>> list(final ListWebhooksQuery query) {
        return executor.executeAsync(Operation.get(ApiRoutes.WEBHOOKS, ResponseType.of(ListWebhooksResponse.class))
                .withQuery(query));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<WebhookDetail>> get(final String id) {
        return executor.executeAsync(
                Operation.get(UrlBuilder.expand(ApiRoutes.WEBHOOK, id), ResponseType.of(WebhookDetail.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<WebhookDetail>> update(
            final String id, final UpdateWebhookInput input) {
        return executor.executeAsync(
                Operation.put(UrlBuilder.expand(ApiRoutes.WEBHOOK, id), ResponseType.of(WebhookDetail.class))
                        .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ToggleWebhookResult>> toggle(
            final String id, final ToggleableWebhookStatus status) {
        return executor.executeAsync(
                Operation.patch(UrlBuilder.expand(ApiRoutes.WEBHOOK, id), ResponseType.of(ToggleWebhookResult.class))
                        .withBody(new ToggleWebhookBody(status)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<NoContent>> delete(final String id) {
        return executor.executeAsync(
                Operation.delete(UrlBuilder.expand(ApiRoutes.WEBHOOK, id), ResponseType.noContent()));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<RotateWebhookSecretResult>> rotateSecret(final String id) {
        return executor.executeAsync(Operation.post(
                UrlBuilder.expand(ApiRoutes.WEBHOOK_ROTATE_SECRET, id),
                ResponseType.of(RotateWebhookSecretResult.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListWebhookDeliveriesResponse>> listDeliveries(final String id) {
        return executor.executeAsync(Operation.get(
                UrlBuilder.expand(ApiRoutes.WEBHOOK_DELIVERIES, id),
                ResponseType.of(ListWebhookDeliveriesResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListWebhookDeliveriesResponse>> listDeliveries(
            final String id, final ListWebhookDeliveriesQuery query) {
        return executor.executeAsync(Operation.get(
                        UrlBuilder.expand(ApiRoutes.WEBHOOK_DELIVERIES, id),
                        ResponseType.of(ListWebhookDeliveriesResponse.class))
                .withQuery(query));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<TestWebhookResult>> test(final String id) {
        return executor.executeAsync(Operation.post(
                UrlBuilder.expand(ApiRoutes.WEBHOOK_TEST, id), ResponseType.of(TestWebhookResult.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<WebhookDeliveryDetail>> getDelivery(
            final String id, final String deliveryId) {
        return executor.executeAsync(Operation.get(
                UrlBuilder.expand(ApiRoutes.WEBHOOK_DELIVERY, id, deliveryId),
                ResponseType.of(WebhookDeliveryDetail.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<WebhookDeliveryDetail>> replayDelivery(
            final String id, final String deliveryId) {
        return executor.executeAsync(Operation.post(
                UrlBuilder.expand(ApiRoutes.WEBHOOK_DELIVERY_REPLAY, id, deliveryId),
                ResponseType.of(WebhookDeliveryDetail.class)));
    }

    @Override
    public boolean verifySignature(final VerifyWebhookInput input) {
        return verifier.verify(input);
    }
}
