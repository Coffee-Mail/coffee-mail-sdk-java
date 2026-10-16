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
import br.com.coffeemail.models.webhooks.WebhookDeliveryDetail;
import br.com.coffeemail.models.webhooks.WebhookDetail;
import br.com.coffeemail.webhooks.VerifyWebhookInput;

/** Endpoints de webhook e verificação de assinatura (síncrono). */
public interface Webhooks {

    /**
     * Cadastra um webhook. É a única resposta que traz o segredo completo.
     *
     * @param input URL, eventos e descrição
     * @return o webhook criado
     */
    CoffeeMailResponse<CreatedWebhookDetail> create(CreateWebhookInput input);

    /**
     * Lista os webhooks.
     *
     * @return os webhooks
     */
    CoffeeMailResponse<ListWebhooksResponse> list();

    /**
     * Lista os webhooks filtrando pela situação.
     *
     * @param query filtros
     * @return os webhooks
     */
    CoffeeMailResponse<ListWebhooksResponse> list(ListWebhooksQuery query);

    /**
     * Busca um webhook.
     *
     * @param id identificador do webhook
     * @return o webhook
     */
    CoffeeMailResponse<WebhookDetail> get(String id);

    /**
     * Atualiza URL, eventos ou descrição de um webhook.
     *
     * @param id identificador do webhook
     * @param input campos alterados
     * @return o webhook atualizado
     */
    CoffeeMailResponse<WebhookDetail> update(String id, UpdateWebhookInput input);

    /**
     * Ativa ou pausa um webhook.
     *
     * @param id identificador do webhook
     * @param status nova situação
     * @return a situação aplicada
     */
    CoffeeMailResponse<ToggleWebhookResult> toggle(String id, ToggleableWebhookStatus status);

    /**
     * Remove um webhook.
     *
     * @param id identificador do webhook
     * @return {@link NoContent} em caso de sucesso
     */
    CoffeeMailResponse<NoContent> delete(String id);

    /**
     * Gera um novo segredo e invalida o anterior. O valor completo só aparece nesta resposta.
     *
     * @param id identificador do webhook
     * @return o novo segredo
     */
    CoffeeMailResponse<RotateWebhookSecretResult> rotateSecret(String id);

    /**
     * Lista as tentativas de entrega de um webhook.
     *
     * @param id identificador do webhook
     * @return as entregas
     */
    CoffeeMailResponse<ListWebhookDeliveriesResponse> listDeliveries(String id);

    /**
     * Lista as tentativas de entrega de um webhook com filtros.
     *
     * @param id identificador do webhook
     * @param query filtros
     * @return as entregas
     */
    CoffeeMailResponse<ListWebhookDeliveriesResponse> listDeliveries(String id, ListWebhookDeliveriesQuery query);

    /**
     * Dispara uma entrega de teste para o endpoint.
     *
     * @param id identificador do webhook
     * @return a resposta do endpoint
     */
    CoffeeMailResponse<TestWebhookResult> test(String id);

    /**
     * Busca uma entrega com o corpo enviado ao endpoint.
     *
     * @param id identificador do webhook
     * @param deliveryId identificador da entrega
     * @return a entrega; falha com {@code WEBHOOK_NOT_FOUND} ou {@code WEBHOOK_DELIVERY_NOT_FOUND} (404) em
     *     {@link br.com.coffeemail.errors.CoffeeMailException#apiCode()}
     */
    CoffeeMailResponse<WebhookDeliveryDetail> getDelivery(String id, String deliveryId);

    /**
     * Reenvia uma entrega concluída com o mesmo corpo. A API cria uma entrega nova e a enfileira (HTTP 202).
     *
     * @param id identificador do webhook
     * @param deliveryId identificador da entrega original
     * @return a entrega nova; falha com {@code WEBHOOK_NOT_FOUND} ou {@code WEBHOOK_DELIVERY_NOT_FOUND} (404),
     *     {@code WEBHOOK_DELIVERY_PENDING} (409, a entrega ainda está em andamento) ou {@code WEBHOOK_NOT_ACTIVE}
     *     (409, webhook pausado) em {@link br.com.coffeemail.errors.CoffeeMailException#apiCode()}
     */
    CoffeeMailResponse<WebhookDeliveryDetail> replayDelivery(String id, String deliveryId);

    /**
     * Verifica a assinatura de uma entrega recebida. Equivale a {@link br.com.coffeemail.webhooks.WebhookVerifier#verify(VerifyWebhookInput)}.
     *
     * @param input corpo bruto, cabeçalhos de assinatura e segredo
     * @return {@code true} quando a assinatura é válida e está dentro da janela de tolerância
     */
    boolean verifySignature(VerifyWebhookInput input);
}
