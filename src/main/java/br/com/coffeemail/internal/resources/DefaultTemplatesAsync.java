package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.http.UrlBuilder;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.models.common.OffsetPaginationQuery;
import br.com.coffeemail.models.templates.CreateTemplateInput;
import br.com.coffeemail.models.templates.FormatTemplateInput;
import br.com.coffeemail.models.templates.FormatTemplateResponse;
import br.com.coffeemail.models.templates.ListTemplateVersionsResponse;
import br.com.coffeemail.models.templates.ListTemplatesQuery;
import br.com.coffeemail.models.templates.ListTemplatesResponse;
import br.com.coffeemail.models.templates.PreviewTemplateInput;
import br.com.coffeemail.models.templates.PreviewTemplateResponse;
import br.com.coffeemail.models.templates.TemplateDetail;
import br.com.coffeemail.models.templates.TemplateVersionDetail;
import br.com.coffeemail.models.templates.TestRenderTemplateInput;
import br.com.coffeemail.models.templates.TestRenderTemplateResponse;
import br.com.coffeemail.models.templates.TestSendTemplateInput;
import br.com.coffeemail.models.templates.TestSendTemplateResponse;
import br.com.coffeemail.models.templates.UpdateTemplateInput;
import br.com.coffeemail.resources.TemplatesAsync;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class DefaultTemplatesAsync implements TemplatesAsync {

    private final RequestExecutor executor;

    public DefaultTemplatesAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<TemplateDetail>> create(final CreateTemplateInput input) {
        return executor.executeAsync(Operation.post(ApiRoutes.TEMPLATES, ResponseType.of(TemplateDetail.class))
                .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListTemplatesResponse>> list() {
        return executor.executeAsync(Operation.get(ApiRoutes.TEMPLATES, ResponseType.of(ListTemplatesResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListTemplatesResponse>> list(final ListTemplatesQuery query) {
        return executor.executeAsync(Operation.get(ApiRoutes.TEMPLATES, ResponseType.of(ListTemplatesResponse.class))
                .withQuery(query));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<TemplateDetail>> get(final String id) {
        return executor.executeAsync(
                Operation.get(UrlBuilder.expand(ApiRoutes.TEMPLATE, id), ResponseType.of(TemplateDetail.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<TemplateDetail>> update(
            final String id, final UpdateTemplateInput input) {
        return executor.executeAsync(
                Operation.patch(UrlBuilder.expand(ApiRoutes.TEMPLATE, id), ResponseType.of(TemplateDetail.class))
                        .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<NoContent>> delete(final String id) {
        return executor.executeAsync(
                Operation.delete(UrlBuilder.expand(ApiRoutes.TEMPLATE, id), ResponseType.noContent()));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<PreviewTemplateResponse>> preview(final PreviewTemplateInput input) {
        return executor.executeAsync(
                Operation.post(ApiRoutes.TEMPLATES_PREVIEW, ResponseType.of(PreviewTemplateResponse.class))
                        .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<PreviewTemplateResponse>> previewById(final String templateId) {
        return previewById(templateId, Optional.empty());
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<PreviewTemplateResponse>> previewById(
            final String templateId, final Map<String, Object> variables) {
        return previewById(templateId, Optional.of(variables));
    }

    private CompletableFuture<CoffeeMailResponse<PreviewTemplateResponse>> previewById(
            final String templateId, final Optional<Map<String, Object>> variables) {
        return get(templateId)
                .thenCompose(template -> template.fold(
                        detail -> preview(previewOf(detail, variables)),
                        error -> CompletableFuture.completedFuture(CoffeeMailResponse.failure(error))));
    }

    private static PreviewTemplateInput previewOf(
            final TemplateDetail detail, final Optional<Map<String, Object>> variables) {
        final PreviewTemplateInput.Builder builder =
                PreviewTemplateInput.builder(detail.html()).format(detail.format());
        return variables.map(builder::variables).orElse(builder).build();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<FormatTemplateResponse>> format(final FormatTemplateInput input) {
        return executor.executeAsync(
                Operation.post(ApiRoutes.TEMPLATES_FORMAT, ResponseType.of(FormatTemplateResponse.class))
                        .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<TestRenderTemplateResponse>> testRender(
            final TestRenderTemplateInput input) {
        return executor.executeAsync(
                Operation.post(ApiRoutes.TEMPLATES_TEST_RENDER, ResponseType.of(TestRenderTemplateResponse.class))
                        .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<TestSendTemplateResponse>> testSend(
            final String id, final TestSendTemplateInput input) {
        return executor.executeAsync(Operation.post(
                        UrlBuilder.expand(ApiRoutes.TEMPLATE_TEST_SEND, id),
                        ResponseType.of(TestSendTemplateResponse.class))
                .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListTemplateVersionsResponse>> listVersions(final String id) {
        return executor.executeAsync(Operation.get(
                UrlBuilder.expand(ApiRoutes.TEMPLATE_VERSIONS, id),
                ResponseType.of(ListTemplateVersionsResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListTemplateVersionsResponse>> listVersions(
            final String id, final OffsetPaginationQuery query) {
        return executor.executeAsync(Operation.get(
                        UrlBuilder.expand(ApiRoutes.TEMPLATE_VERSIONS, id),
                        ResponseType.of(ListTemplateVersionsResponse.class))
                .withQuery(query));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<TemplateVersionDetail>> getVersion(final String id, final int version) {
        return executor.executeAsync(Operation.get(
                UrlBuilder.expand(ApiRoutes.TEMPLATE_VERSION, id, String.valueOf(version)),
                ResponseType.of(TemplateVersionDetail.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<TemplateDetail>> restoreVersion(final String id, final int version) {
        return executor.executeAsync(Operation.post(
                UrlBuilder.expand(ApiRoutes.TEMPLATE_VERSION_RESTORE, id, String.valueOf(version)),
                ResponseType.of(TemplateDetail.class)));
    }
}
