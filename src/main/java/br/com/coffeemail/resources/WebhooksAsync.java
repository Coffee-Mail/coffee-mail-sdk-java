package br.com.coffeemail.resources;

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
import br.com.coffeemail.webhooks.VerifyWebhookInput;
import java.util.concurrent.CompletableFuture;

/** Endpoints de webhook e verificação de assinatura (assíncrono). */
public interface WebhooksAsync {

    /**
     * Cadastra um webhook. É a única resposta que traz o segredo completo.
     *
     * @param input URL, eventos e descrição
     * @return o webhook criado
     */
    CompletableFuture<CoffeeMailResponse<CreatedWebhookDetail>> create(CreateWebhookInput input);

    /**
     * Lista os webhooks.
     *
     * @return os webhooks
     */
    CompletableFuture<CoffeeMailResponse<ListWebhooksResponse>> list();

    /**
     * Lista os webhooks filtrando pela situação.
     *
     * @param query filtros
     * @return os webhooks
     */
    CompletableFuture<CoffeeMailResponse<ListWebhooksResponse>> list(ListWebhooksQuery query);

    /**
     * Busca um webhook.
     *
     * @param id identificador do webhook
     * @return o webhook
     */
    CompletableFuture<CoffeeMailResponse<WebhookDetail>> get(String id);

    /**
     * Atualiza URL, eventos ou descrição de um webhook.
     *
     * @param id identificador do webhook
     * @param input campos alterados
     * @return o webhook atualizado
     */
    CompletableFuture<CoffeeMailResponse<WebhookDetail>> update(String id, UpdateWebhookInput input);

    /**
     * Ativa ou pausa um webhook.
     *
     * @param id identificador do webhook
     * @param status nova situação
     * @return a situação aplicada
     */
    CompletableFuture<CoffeeMailResponse<ToggleWebhookResult>> toggle(String id, ToggleableWebhookStatus status);

    /**
     * Remove um webhook.
     *
     * @param id identificador do webhook
     * @return {@link NoContent} em caso de sucesso
     */
    CompletableFuture<CoffeeMailResponse<NoContent>> delete(String id);

    /**
     * Gera um novo segredo e invalida o anterior. O valor completo só aparece nesta resposta.
     *
     * @param id identificador do webhook
     * @return o novo segredo
     */
    CompletableFuture<CoffeeMailResponse<RotateWebhookSecretResult>> rotateSecret(String id);

    /**
     * Lista as tentativas de entrega de um webhook.
     *
     * @param id identificador do webhook
     * @return as entregas
     */
    CompletableFuture<CoffeeMailResponse<ListWebhookDeliveriesResponse>> listDeliveries(String id);

    /**
     * Lista as tentativas de entrega de um webhook com filtros.
     *
     * @param id identificador do webhook
     * @param query filtros
     * @return as entregas
     */
    CompletableFuture<CoffeeMailResponse<ListWebhookDeliveriesResponse>> listDeliveries(
            String id, ListWebhookDeliveriesQuery query);

    /**
     * Dispara uma entrega de teste para o endpoint.
     *
     * @param id identificador do webhook
     * @return a resposta do endpoint
     */
    CompletableFuture<CoffeeMailResponse<TestWebhookResult>> test(String id);

    /**
     * Verifica a assinatura de uma entrega recebida. Equivale a {@link br.com.coffeemail.webhooks.WebhookVerifier#verify(VerifyWebhookInput)}.
     *
     * @param input corpo bruto, cabeçalhos de assinatura e segredo
     * @return {@code true} quando a assinatura é válida e está dentro da janela de tolerância
     */
    boolean verifySignature(VerifyWebhookInput input);
}
