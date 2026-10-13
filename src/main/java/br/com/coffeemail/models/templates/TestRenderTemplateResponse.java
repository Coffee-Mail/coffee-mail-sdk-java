package br.com.coffeemail.models.templates;

/** HTML sanitizado, texto e relatório da sanitização. */
public record TestRenderTemplateResponse(String html, String text, SanitizeReport sanitizeReport) {}
