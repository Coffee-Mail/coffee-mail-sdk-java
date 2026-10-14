package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.templates.CreateTemplateInput;
import br.com.coffeemail.models.templates.FormatTemplateInput;
import br.com.coffeemail.models.templates.FormatTemplateResponse;
import br.com.coffeemail.models.templates.ListTemplatesQuery;
import br.com.coffeemail.models.templates.ListTemplatesResponse;
import br.com.coffeemail.models.templates.PreviewTemplateInput;
import br.com.coffeemail.models.templates.PreviewTemplateResponse;
import br.com.coffeemail.models.templates.TemplateDetail;
import br.com.coffeemail.models.templates.TestRenderTemplateInput;
import br.com.coffeemail.models.templates.TestRenderTemplateResponse;
import br.com.coffeemail.models.templates.TestSendTemplateInput;
import br.com.coffeemail.models.templates.TestSendTemplateResponse;
import br.com.coffeemail.models.templates.UpdateTemplateInput;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/** Gestão e renderização de templates (assíncrono). */
public interface TemplatesAsync {

    /**
     * Cria um template.
     *
     * @param input dados do template
     * @return o template criado
     */
    CompletableFuture<CoffeeMailResponse<TemplateDetail>> create(CreateTemplateInput input);

    /**
     * Lista os templates da organização.
     *
     * @return os templates
     */
    CompletableFuture<CoffeeMailResponse<ListTemplatesResponse>> list();

    /**
     * Lista os templates com paginação.
     *
     * @param query paginação
     * @return os templates
     */
    CompletableFuture<CoffeeMailResponse<ListTemplatesResponse>> list(ListTemplatesQuery query);

    /**
     * Busca um template.
     *
     * @param id identificador do template
     * @return o template
     */
    CompletableFuture<CoffeeMailResponse<TemplateDetail>> get(String id);

    /**
     * Atualiza parcialmente um template.
     *
     * @param id identificador do template
     * @param input campos alterados
     * @return o template atualizado
     */
    CompletableFuture<CoffeeMailResponse<TemplateDetail>> update(String id, UpdateTemplateInput input);

    /**
     * Remove um template.
     *
     * @param id identificador do template
     * @return {@link NoContent} em caso de sucesso
     */
    CompletableFuture<CoffeeMailResponse<NoContent>> delete(String id);

    /**
     * Renderiza HTML/JSX com variáveis, sem persistir e sem sanitizar.
     *
     * @param input código e variáveis
     * @return o HTML e o texto renderizados
     */
    CompletableFuture<CoffeeMailResponse<PreviewTemplateResponse>> preview(PreviewTemplateInput input);

    /**
     * Renderiza a pré-visualização de um template cadastrado.
     *
     * @param templateId identificador do template
     * @return o HTML e o texto renderizados
     */
    CompletableFuture<CoffeeMailResponse<PreviewTemplateResponse>> previewById(String templateId);

    /**
     * Renderiza a pré-visualização de um template cadastrado com variáveis.
     *
     * @param templateId identificador do template
     * @param variables valores das variáveis
     * @return o HTML e o texto renderizados
     */
    CompletableFuture<CoffeeMailResponse<PreviewTemplateResponse>> previewById(
            String templateId, Map<String, Object> variables);

    /**
     * Formata o código-fonte de um template, sem persistir.
     *
     * @param input código a formatar
     * @return o código formatado
     */
    CompletableFuture<CoffeeMailResponse<FormatTemplateResponse>> format(FormatTemplateInput input);

    /**
     * Renderiza com variáveis e sanitiza o HTML resultante.
     *
     * @param input código e variáveis
     * @return o HTML sanitizado e o relatório da sanitização
     */
    CompletableFuture<CoffeeMailResponse<TestRenderTemplateResponse>> testRender(TestRenderTemplateInput input);

    /**
     * Dispara um e-mail de teste real com um template cadastrado.
     *
     * @param id identificador do template
     * @param input destinatário e variáveis
     * @return o resultado do envio
     */
    CompletableFuture<CoffeeMailResponse<TestSendTemplateResponse>> testSend(String id, TestSendTemplateInput input);
}
