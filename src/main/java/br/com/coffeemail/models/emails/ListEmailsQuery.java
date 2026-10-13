package br.com.coffeemail.models.emails;

import java.time.Instant;
import org.jspecify.annotations.Nullable;

/** Filtros da listagem de e-mails. */
public record ListEmailsQuery(
        @Nullable EmailStatus status,
        @Nullable String recipient,
        @Nullable String fromEmail,
        @Nullable String subjectContains,
        @Nullable String apiKeyId,
        @Nullable String tag,
        @Nullable Instant from,
        @Nullable Instant to,
        @Nullable String after,
        @Nullable Integer limit) {

    /**
     * Inicia a construção.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder de {@link ListEmailsQuery}. */
    public static final class Builder {
        private @Nullable EmailStatus status;
        private @Nullable String recipient;
        private @Nullable String fromEmail;
        private @Nullable String subjectContains;
        private @Nullable String apiKeyId;
        private @Nullable String tag;
        private @Nullable Instant from;
        private @Nullable Instant to;
        private @Nullable String after;
        private @Nullable Integer limit;

        private Builder() {}

        public Builder status(final EmailStatus value) {
            this.status = value;
            return this;
        }

        public Builder recipient(final String value) {
            this.recipient = value;
            return this;
        }

        public Builder fromEmail(final String value) {
            this.fromEmail = value;
            return this;
        }

        public Builder subjectContains(final String value) {
            this.subjectContains = value;
            return this;
        }

        public Builder apiKeyId(final String value) {
            this.apiKeyId = value;
            return this;
        }

        public Builder tag(final String value) {
            this.tag = value;
            return this;
        }

        public Builder from(final Instant value) {
            this.from = value;
            return this;
        }

        public Builder to(final Instant value) {
            this.to = value;
            return this;
        }

        public Builder after(final String value) {
            this.after = value;
            return this;
        }

        public Builder limit(final Integer value) {
            this.limit = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public ListEmailsQuery build() {
            return new ListEmailsQuery(
                    status, recipient, fromEmail, subjectContains, apiKeyId, tag, from, to, after, limit);
        }
    }
}
