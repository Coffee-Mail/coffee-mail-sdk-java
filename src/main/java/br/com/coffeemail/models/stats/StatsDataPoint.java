package br.com.coffeemail.models.stats;

/** Ponto da série histórica. */
public record StatsDataPoint(
        String period,
        int sent,
        int delivered,
        int bounced,
        int failed,
        int opened,
        int clicked,
        int transactional,
        int broadcast) {}
