package br.com.coffeemail.models.templates;

/** Contagem do que a sanitização removeu. */
public record SanitizeReport(int scripts, int iframes, int javascriptHrefs, int eventHandlers) {}
