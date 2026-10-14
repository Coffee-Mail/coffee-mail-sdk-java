package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.http.UrlBuilder;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.internal.wire.SendEmailBody;
import br.com.coffeemail.internal.wire.SendEmailHeaders;
import br.com.coffeemail.models.emails.BatchSendEmailResult;
import br.com.coffeemail.models.emails.EmailDetail;
import br.com.coffeemail.models.emails.EmailEventsResponse;
import br.com.coffeemail.models.emails.ListEmailTagsResponse;
import br.com.coffeemail.models.emails.ListEmailsQuery;
import br.com.coffeemail.models.emails.ListEmailsResponse;
import br.com.coffeemail.models.emails.SendEmailInput;
import br.com.coffeemail.models.emails.SendEmailResponse;
import br.com.coffeemail.resources.EmailsAsync;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class DefaultEmailsAsync implements EmailsAsync {

    private final RequestExecutor executor;

    public DefaultEmailsAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<SendEmailResponse>> send(final SendEmailInput input) {
        return executor.executeAsync(Operation.post(ApiRoutes.EMAILS, ResponseType.of(SendEmailResponse.class))
                .withBody(SendEmailBody.from(input))
                .withHeaders(SendEmailHeaders.of(input)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<List<BatchSendEmailResult>>> sendBatch(
            final List<SendEmailInput> inputs) {
        return executor.executeAsync(
                Operation.post(ApiRoutes.EMAILS_BATCH, ResponseType.listOf(BatchSendEmailResult.class))
                        .withBody(inputs.stream().map(SendEmailBody::from).toList())
                        .withHeaders(SendEmailHeaders.merged(inputs)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<EmailDetail>> get(final String id) {
        return executor.executeAsync(
                Operation.get(UrlBuilder.expand(ApiRoutes.EMAIL, id), ResponseType.of(EmailDetail.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListEmailsResponse>> list() {
        return executor.executeAsync(Operation.get(ApiRoutes.EMAILS, ResponseType.of(ListEmailsResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListEmailsResponse>> list(final ListEmailsQuery query) {
        return executor.executeAsync(Operation.get(ApiRoutes.EMAILS, ResponseType.of(ListEmailsResponse.class))
                .withQuery(query));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<EmailEventsResponse>> getEvents(final String id) {
        return executor.executeAsync(Operation.get(
                UrlBuilder.expand(ApiRoutes.EMAIL_EVENTS, id), ResponseType.of(EmailEventsResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListEmailTagsResponse>> getTags() {
        return executor.executeAsync(
                Operation.get(ApiRoutes.EMAILS_TAGS, ResponseType.of(ListEmailTagsResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<NoContent>> cancel(final String id) {
        return executor.executeAsync(
                Operation.post(UrlBuilder.expand(ApiRoutes.EMAIL_CANCEL, id), ResponseType.noContent()));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<SendEmailResponse>> resend(final String id) {
        return executor.executeAsync(Operation.post(
                UrlBuilder.expand(ApiRoutes.EMAIL_RESEND, id), ResponseType.of(SendEmailResponse.class)));
    }
}
