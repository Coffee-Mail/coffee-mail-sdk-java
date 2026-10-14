package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.audiences.BulkAddContactsResult;
import br.com.coffeemail.models.audiences.ContactDetail;
import br.com.coffeemail.models.audiences.CreateContactInput;
import br.com.coffeemail.models.audiences.ListContactsResponse;
import br.com.coffeemail.models.audiences.UpdateContactInput;
import br.com.coffeemail.models.audiences.UpdateContactResult;
import br.com.coffeemail.models.common.OffsetPaginationQuery;
import br.com.coffeemail.resources.Contacts;
import br.com.coffeemail.resources.ContactsAsync;
import java.util.List;

public final class DefaultContacts implements Contacts {

    private final ContactsAsync async;

    public DefaultContacts(final ContactsAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<ContactDetail> create(final String audienceId, final CreateContactInput input) {
        return async.create(audienceId, input).join();
    }

    @Override
    public CoffeeMailResponse<ListContactsResponse> list(final String audienceId) {
        return async.list(audienceId).join();
    }

    @Override
    public CoffeeMailResponse<ListContactsResponse> list(final String audienceId, final OffsetPaginationQuery query) {
        return async.list(audienceId, query).join();
    }

    @Override
    public CoffeeMailResponse<BulkAddContactsResult> bulkAdd(
            final String audienceId, final List<CreateContactInput> contacts) {
        return async.bulkAdd(audienceId, contacts).join();
    }

    @Override
    public CoffeeMailResponse<UpdateContactResult> update(
            final String audienceId, final String contactId, final UpdateContactInput input) {
        return async.update(audienceId, contactId, input).join();
    }

    @Override
    public CoffeeMailResponse<NoContent> delete(final String audienceId, final String contactId) {
        return async.delete(audienceId, contactId).join();
    }
}
