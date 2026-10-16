package br.com.coffeemail.models.broadcasts;

/**
 * Métricas agregadas de entrega de uma campanha.
 *
 * @param delivered e-mails entregues
 * @param opened e-mails abertos ao menos uma vez
 * @param clicked e-mails com ao menos um clique
 * @param bounced e-mails devolvidos
 * @param failed e-mails com falha definitiva
 * @param suppressed destinatários pulados por supressão
 */
public record BroadcastMetrics(int delivered, int opened, int clicked, int bounced, int failed, int suppressed) {}
