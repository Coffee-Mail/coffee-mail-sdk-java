package br.com.coffeemail.models.stats;

/** Cliente de e-mail mais usado pelos destinatários. */
public record StatsTopClient(String name, int count, double percentage, String deviceType) {}
