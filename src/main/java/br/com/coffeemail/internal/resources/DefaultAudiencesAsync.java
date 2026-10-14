package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.http.UrlBuilder;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.models.audiences.AudienceDetail;
import br.com.coffeemail.models.audiences.BulkAddContactsResult;
import br.com.coffeemail.models.audiences.ContactDetail;
import br.com.coffeemail.models.audiences.CreateAudienceInput;
import br.com.coffeemail.models.audiences.CreateContactInput;
import br.com.coffeemail.models.audiences.ListAudiencesResponse;
import br.com.coffeemail.models.audiences.ListContactsResponse;
import br.com.coffeemail.models.audiences.UpdateAudienceInput;
import br.com.coffeemail.models.audiences.UpdateAudienceResult;
import br.com.coffeemail.models.common.OffsetPaginationQuery;
import br.com.coffeemail.resources.AudiencesAsync;
import br.com.coffeemail.resources.ContactsAsync;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class DefaultAudiencesAsync implements AudiencesAsync {

    private final RequestExecutor executor;
    private final ContactsAsync contacts;

    public DefaultAudiencesAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
        this.contacts = new DefaultContactsAsync(runtime);
    }

    @Override
    public ContactsAsync contacts() {
        return contacts;
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<AudienceDetail>> create(final CreateAudienceInput input) {
        return executor.executeAsync(Operation.post(ApiRoutes.AUDIENCES, ResponseType.of(AudienceDetail.class))
                .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListAudiencesResponse>> list() {
        return executor.executeAsync(Operation.get(ApiRoutes.AUDIENCES, ResponseType.of(ListAudiencesResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListAudiencesResponse>> list(final OffsetPaginationQuery query) {
        return executor.executeAsync(Operation.get(ApiRoutes.AUDIENCES, ResponseType.of(ListAudiencesResponse.class))
                .withQuery(query));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<AudienceDetail>> get(final String id) {
        return executor.executeAsync(
                Operation.get(UrlBuilder.expand(ApiRoutes.AUDIENCE, id), ResponseType.of(AudienceDetail.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<UpdateAudienceResult>> update(
            final String id, final UpdateAudienceInput input) {
        return executor.executeAsync(
                Operation.put(UrlBuilder.expand(ApiRoutes.AUDIENCE, id), ResponseType.of(UpdateAudienceResult.class))
                        .withBody(input));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<NoContent>> delete(final String id) {
        return executor.executeAsync(
                Operation.delete(UrlBuilder.expand(ApiRoutes.AUDIENCE, id), ResponseType.noContent()));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListContactsResponse>> listContacts(final String audienceId) {
        return contacts.list(audienceId);
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListContactsResponse>> listContacts(
            final String audienceId, final OffsetPaginationQuery query) {
        return contacts.list(audienceId, query);
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ContactDetail>> createContact(
            final String audienceId, final CreateContactInput input) {
        return contacts.create(audienceId, input);
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<BulkAddContactsResult>> bulkAddContacts(
            final String audienceId, final List<CreateContactInput> input) {
        return contacts.bulkAdd(audienceId, input);
    }
}
