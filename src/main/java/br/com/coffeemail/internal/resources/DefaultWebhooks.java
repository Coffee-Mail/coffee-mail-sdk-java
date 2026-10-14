package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
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
import br.com.coffeemail.models.webhooks.WebhookDetail;
import br.com.coffeemail.resources.Webhooks;
import br.com.coffeemail.resources.WebhooksAsync;
import br.com.coffeemail.webhooks.VerifyWebhookInput;

public final class DefaultWebhooks implements Webhooks {

    private final WebhooksAsync async;

    public DefaultWebhooks(final WebhooksAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<CreatedWebhookDetail> create(final CreateWebhookInput input) {
        return async.create(input).join();
    }

    @Override
    public CoffeeMailResponse<ListWebhooksResponse> list() {
        return async.list().join();
    }

    @Override
    public CoffeeMailResponse<ListWebhooksResponse> list(final ListWebhooksQuery query) {
        return async.list(query).join();
    }

    @Override
    public CoffeeMailResponse<WebhookDetail> get(final String id) {
        return async.get(id).join();
    }

    @Override
    public CoffeeMailResponse<WebhookDetail> update(final String id, final UpdateWebhookInput input) {
        return async.update(id, input).join();
    }

    @Override
    public CoffeeMailResponse<ToggleWebhookResult> toggle(final String id, final ToggleableWebhookStatus status) {
        return async.toggle(id, status).join();
    }

    @Override
    public CoffeeMailResponse<NoContent> delete(final String id) {
        return async.delete(id).join();
    }

    @Override
    public CoffeeMailResponse<RotateWebhookSecretResult> rotateSecret(final String id) {
        return async.rotateSecret(id).join();
    }

    @Override
    public CoffeeMailResponse<ListWebhookDeliveriesResponse> listDeliveries(final String id) {
        return async.listDeliveries(id).join();
    }

    @Override
    public CoffeeMailResponse<ListWebhookDeliveriesResponse> listDeliveries(
            final String id, final ListWebhookDeliveriesQuery query) {
        return async.listDeliveries(id, query).join();
    }

    @Override
    public CoffeeMailResponse<TestWebhookResult> test(final String id) {
        return async.test(id).join();
    }

    @Override
    public boolean verifySignature(final VerifyWebhookInput input) {
        return async.verifySignature(input);
    }
}
