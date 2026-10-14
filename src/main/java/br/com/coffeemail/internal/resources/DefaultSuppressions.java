package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.suppressions.BulkCreateSuppressionsResult;
import br.com.coffeemail.models.suppressions.BulkSuppressionEntry;
import br.com.coffeemail.models.suppressions.CreateSuppressionInput;
import br.com.coffeemail.models.suppressions.ListSuppressionsQuery;
import br.com.coffeemail.models.suppressions.ListSuppressionsResponse;
import br.com.coffeemail.models.suppressions.SuppressionDetail;
import br.com.coffeemail.resources.Suppressions;
import br.com.coffeemail.resources.SuppressionsAsync;
import java.util.List;

public final class DefaultSuppressions implements Suppressions {

    private final SuppressionsAsync async;

    public DefaultSuppressions(final SuppressionsAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<ListSuppressionsResponse> list() {
        return async.list().join();
    }

    @Override
    public CoffeeMailResponse<ListSuppressionsResponse> list(final ListSuppressionsQuery query) {
        return async.list(query).join();
    }

    @Override
    public CoffeeMailResponse<SuppressionDetail> get(final String id) {
        return async.get(id).join();
    }

    @Override
    public CoffeeMailResponse<SuppressionDetail> create(final CreateSuppressionInput input) {
        return async.create(input).join();
    }

    @Override
    public CoffeeMailResponse<NoContent> delete(final String id) {
        return async.delete(id).join();
    }

    @Override
    public CoffeeMailResponse<NoContent> reactivate(final String id) {
        return async.reactivate(id).join();
    }

    @Override
    public CoffeeMailResponse<BulkCreateSuppressionsResult> bulkCreate(final List<BulkSuppressionEntry> entries) {
        return async.bulkCreate(entries).join();
    }
}
