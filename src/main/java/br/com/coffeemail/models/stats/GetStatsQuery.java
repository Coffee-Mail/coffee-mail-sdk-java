package br.com.coffeemail.models.stats;

import org.jspecify.annotations.Nullable;

/** Filtros das estatísticas. */
public record GetStatsQuery(
        @Nullable String domainId,
        @Nullable String tag,
        @Nullable String startDate,
        @Nullable String endDate,
        @Nullable StatsGranularity granularity,
        @Nullable StatsPeriod period,
        @Nullable StatsEmailType emailType) {

    /**
     * Inicia a construção.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder de {@link GetStatsQuery}. */
    public static final class Builder {
        private @Nullable String domainId;
        private @Nullable String tag;
        private @Nullable String startDate;
        private @Nullable String endDate;
        private @Nullable StatsGranularity granularity;
        private @Nullable StatsPeriod period;
        private @Nullable StatsEmailType emailType;

        private Builder() {}

        public Builder domainId(final String value) {
            this.domainId = value;
            return this;
        }

        public Builder tag(final String value) {
            this.tag = value;
            return this;
        }

        public Builder startDate(final String value) {
            this.startDate = value;
            return this;
        }

        public Builder endDate(final String value) {
            this.endDate = value;
            return this;
        }

        public Builder granularity(final StatsGranularity value) {
            this.granularity = value;
            return this;
        }

        public Builder period(final StatsPeriod value) {
            this.period = value;
            return this;
        }

        public Builder emailType(final StatsEmailType value) {
            this.emailType = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public GetStatsQuery build() {
            return new GetStatsQuery(domainId, tag, startDate, endDate, granularity, period, emailType);
        }
    }
}
