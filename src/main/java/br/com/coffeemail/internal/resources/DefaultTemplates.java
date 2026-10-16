package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
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
import br.com.coffeemail.resources.Templates;
import br.com.coffeemail.resources.TemplatesAsync;
import java.util.Map;

public final class DefaultTemplates implements Templates {

    private final TemplatesAsync async;

    public DefaultTemplates(final TemplatesAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<TemplateDetail> create(final CreateTemplateInput input) {
        return async.create(input).join();
    }

    @Override
    public CoffeeMailResponse<ListTemplatesResponse> list() {
        return async.list().join();
    }

    @Override
    public CoffeeMailResponse<ListTemplatesResponse> list(final ListTemplatesQuery query) {
        return async.list(query).join();
    }

    @Override
    public CoffeeMailResponse<TemplateDetail> get(final String id) {
        return async.get(id).join();
    }

    @Override
    public CoffeeMailResponse<TemplateDetail> update(final String id, final UpdateTemplateInput input) {
        return async.update(id, input).join();
    }

    @Override
    public CoffeeMailResponse<NoContent> delete(final String id) {
        return async.delete(id).join();
    }

    @Override
    public CoffeeMailResponse<PreviewTemplateResponse> preview(final PreviewTemplateInput input) {
        return async.preview(input).join();
    }

    @Override
    public CoffeeMailResponse<PreviewTemplateResponse> previewById(final String templateId) {
        return async.previewById(templateId).join();
    }

    @Override
    public CoffeeMailResponse<PreviewTemplateResponse> previewById(
            final String templateId, final Map<String, Object> variables) {
        return async.previewById(templateId, variables).join();
    }

    @Override
    public CoffeeMailResponse<FormatTemplateResponse> format(final FormatTemplateInput input) {
        return async.format(input).join();
    }

    @Override
    public CoffeeMailResponse<TestRenderTemplateResponse> testRender(final TestRenderTemplateInput input) {
        return async.testRender(input).join();
    }

    @Override
    public CoffeeMailResponse<TestSendTemplateResponse> testSend(final String id, final TestSendTemplateInput input) {
        return async.testSend(id, input).join();
    }

    @Override
    public CoffeeMailResponse<ListTemplateVersionsResponse> listVersions(final String id) {
        return async.listVersions(id).join();
    }

    @Override
    public CoffeeMailResponse<ListTemplateVersionsResponse> listVersions(
            final String id, final OffsetPaginationQuery query) {
        return async.listVersions(id, query).join();
    }

    @Override
    public CoffeeMailResponse<TemplateVersionDetail> getVersion(final String id, final int version) {
        return async.getVersion(id, version).join();
    }

    @Override
    public CoffeeMailResponse<TemplateDetail> restoreVersion(final String id, final int version) {
        return async.restoreVersion(id, version).join();
    }
}
