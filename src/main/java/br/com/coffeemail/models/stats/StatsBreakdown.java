package br.com.coffeemail.models.stats;

/** Totais separados por tipo de envio. */
public record StatsBreakdown(StatsChannel transactional, StatsChannel broadcast) {}
