package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
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
import br.com.coffeemail.resources.Audiences;
import br.com.coffeemail.resources.AudiencesAsync;
import br.com.coffeemail.resources.Contacts;
import java.util.List;

public final class DefaultAudiences implements Audiences {

    private final AudiencesAsync async;
    private final Contacts contacts;

    public DefaultAudiences(final AudiencesAsync async) {
        this.async = async;
        this.contacts = new DefaultContacts(async.contacts());
    }

    @Override
    public Contacts contacts() {
        return contacts;
    }

    @Override
    public CoffeeMailResponse<AudienceDetail> create(final CreateAudienceInput input) {
        return async.create(input).join();
    }

    @Override
    public CoffeeMailResponse<ListAudiencesResponse> list() {
        return async.list().join();
    }

    @Override
    public CoffeeMailResponse<ListAudiencesResponse> list(final OffsetPaginationQuery query) {
        return async.list(query).join();
    }

    @Override
    public CoffeeMailResponse<AudienceDetail> get(final String id) {
        return async.get(id).join();
    }

    @Override
    public CoffeeMailResponse<UpdateAudienceResult> update(final String id, final UpdateAudienceInput input) {
        return async.update(id, input).join();
    }

    @Override
    public CoffeeMailResponse<NoContent> delete(final String id) {
        return async.delete(id).join();
    }

    @Override
    public CoffeeMailResponse<ListContactsResponse> listContacts(final String audienceId) {
        return async.listContacts(audienceId).join();
    }

    @Override
    public CoffeeMailResponse<ListContactsResponse> listContacts(
            final String audienceId, final OffsetPaginationQuery query) {
        return async.listContacts(audienceId, query).join();
    }

    @Override
    public CoffeeMailResponse<ContactDetail> createContact(final String audienceId, final CreateContactInput input) {
        return async.createContact(audienceId, input).join();
    }

    @Override
    public CoffeeMailResponse<BulkAddContactsResult> bulkAddContacts(
            final String audienceId, final List<CreateContactInput> input) {
        return async.bulkAddContacts(audienceId, input).join();
    }
}
