package br.com.coffeemail.models.stats;

/** Totais de um tipo de envio. */
public record StatsChannel(int sent, int delivered, int bounced, int opened, int clicked) {}
