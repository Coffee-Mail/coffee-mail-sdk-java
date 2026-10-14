package br.com.coffeemail.models.broadcasts;

import java.time.Instant;
import org.jspecify.annotations.Nullable;

/** Filtros da listagem de campanhas. */
public record ListBroadcastsQuery(
        @Nullable BroadcastStatus status,
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

    /** Builder de {@link ListBroadcastsQuery}. */
    public static final class Builder {
        private @Nullable BroadcastStatus status;
        private @Nullable Instant from;
        private @Nullable Instant to;
        private @Nullable String after;
        private @Nullable Integer limit;

        private Builder() {}

        public Builder status(final BroadcastStatus value) {
            this.status = value;
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
        public ListBroadcastsQuery build() {
            return new ListBroadcastsQuery(status, from, to, after, limit);
        }
    }
}
