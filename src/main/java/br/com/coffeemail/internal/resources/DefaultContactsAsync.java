package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.http.UrlBuilder;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.internal.wire.NamedBodies.BulkContactsBody;
import br.com.coffeemail.models.audiences.BulkAddContactsResult;
import br.com.coffeemail.models.audiences.ContactDetail;
import br.com.coffeemail.models.audiences.CreateContactInput;
import br.com.coffeemail.models.audiences.ListContactsResponse;
import br.com.coffeemail.models.audiences.UpdateContactInput;
import br.com.coffeemail.models.audiences.UpdateContactResult;
import br.com.coffeemail.models.common.OffsetPaginationQuery;
import br.com.coffeemail.resources.ContactsAsync;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class DefaultContactsAsync implements ContactsAsync {

    private final RequestExecutor executor;

    public DefaultContactsAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ContactDetail>> create(
            final String audienceId, final CreateContactInput input) {
        return executor.executeAsync(Operation.post(
                        UrlBuilder.expand(ApiRoutes.AUDIENCE_CONTACTS, audienceId),
                        ResponseType.of(ContactDetail.class))
                .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListContactsResponse>> list(final String audienceId) {
        return executor.executeAsync(Operation.get(
                UrlBuilder.expand(ApiRoutes.AUDIENCE_CONTACTS, audienceId),
                ResponseType.of(ListContactsResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListContactsResponse>> list(
            final String audienceId, final OffsetPaginationQuery query) {
        return executor.executeAsync(Operation.get(
                        UrlBuilder.expand(ApiRoutes.AUDIENCE_CONTACTS, audienceId),
                        ResponseType.of(ListContactsResponse.class))
                .withQuery(query));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<BulkAddContactsResult>> bulkAdd(
            final String audienceId, final List<CreateContactInput> contacts) {
        return executor.executeAsync(Operation.post(
                        UrlBuilder.expand(ApiRoutes.AUDIENCE_CONTACTS_BULK, audienceId),
                        ResponseType.of(BulkAddContactsResult.class))
                .withBody(new BulkContactsBody(List.copyOf(contacts))));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<UpdateContactResult>> update(
            final String audienceId, final String contactId, final UpdateContactInput input) {
        return executor.executeAsync(Operation.put(
                        UrlBuilder.expand(ApiRoutes.AUDIENCE_CONTACT, audienceId, contactId),
                        ResponseType.of(UpdateContactResult.class))
                .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<NoContent>> delete(final String audienceId, final String contactId) {
        return executor.executeAsync(Operation.delete(
                UrlBuilder.expand(ApiRoutes.AUDIENCE_CONTACT, audienceId, contactId), ResponseType.noContent()));
    }
}
