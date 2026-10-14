package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.emails.BatchSendEmailResult;
import br.com.coffeemail.models.emails.EmailDetail;
import br.com.coffeemail.models.emails.EmailEventsResponse;
import br.com.coffeemail.models.emails.ListEmailTagsResponse;
import br.com.coffeemail.models.emails.ListEmailsQuery;
import br.com.coffeemail.models.emails.ListEmailsResponse;
import br.com.coffeemail.models.emails.SendEmailInput;
import br.com.coffeemail.models.emails.SendEmailResponse;
import br.com.coffeemail.resources.Emails;
import br.com.coffeemail.resources.EmailsAsync;
import java.util.List;

public final class DefaultEmails implements Emails {

    private final EmailsAsync async;

    public DefaultEmails(final EmailsAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<SendEmailResponse> send(final SendEmailInput input) {
        return async.send(input).join();
    }

    @Override
    public CoffeeMailResponse<List<BatchSendEmailResult>> sendBatch(final List<SendEmailInput> inputs) {
        return async.sendBatch(inputs).join();
    }

    @Override
    public CoffeeMailResponse<EmailDetail> get(final String id) {
        return async.get(id).join();
    }

    @Override
    public CoffeeMailResponse<ListEmailsResponse> list() {
        return async.list().join();
    }

    @Override
    public CoffeeMailResponse<ListEmailsResponse> list(final ListEmailsQuery query) {
        return async.list(query).join();
    }

    @Override
    public CoffeeMailResponse<EmailEventsResponse> getEvents(final String id) {
        return async.getEvents(id).join();
    }

    @Override
    public CoffeeMailResponse<ListEmailTagsResponse> getTags() {
        return async.getTags().join();
    }

    @Override
    public CoffeeMailResponse<NoContent> cancel(final String id) {
        return async.cancel(id).join();
    }

    @Override
    public CoffeeMailResponse<SendEmailResponse> resend(final String id) {
        return async.resend(id).join();
    }
}
