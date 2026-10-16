package br.com.coffeemail.models.broadcasts;

import com.fasterxml.jackson.annotation.JsonValue;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Dados para criar uma campanha. Exatamente um entre template e HTML: use {@link #fromTemplate} ou {@link #fromHtml}.
 * {@link Builder#scheduledAt(Instant)} agenda o envio: a campanha nasce {@code scheduled}.
 */
public final class CreateBroadcastInput {

    private static final String AUDIENCE_ID = "audienceId";
    private static final String FROM_EMAIL = "fromEmail";
    private static final String TEMPLATE_ID = "templateId";
    private static final String SUBJECT = "subject";
    private static final String HTML = "html";

    private final Map<String, Object> fields;

    private CreateBroadcastInput(final Map<String, Object> fields) {
        this.fields = Collections.unmodifiableMap(new LinkedHashMap<>(fields));
    }

    /**
     * Campanha baseada em um template cadastrado, que já define o assunto padrão.
     *
     * @param audienceId audiência destinatária
     * @param fromEmail remetente
     * @param templateId template cadastrado
     * @return o builder
     */
    public static Builder fromTemplate(final String audienceId, final String fromEmail, final String templateId) {
        return new Builder(Map.of(AUDIENCE_ID, audienceId, FROM_EMAIL, fromEmail, TEMPLATE_ID, templateId));
    }

    /**
     * Campanha com HTML próprio; o assunto é obrigatório.
     *
     * @param audienceId audiência destinatária
     * @param fromEmail remetente
     * @param subject assunto
     * @param html conteúdo HTML
     * @return o builder
     */
    public static Builder fromHtml(
            final String audienceId, final String fromEmail, final String subject, final String html) {
        return new Builder(Map.of(AUDIENCE_ID, audienceId, FROM_EMAIL, fromEmail, SUBJECT, subject, HTML, html));
    }

    /**
     * Campos enviados à API.
     *
     * @return mapa imutável na ordem de definição
     */
    @JsonValue
    public Map<String, Object> fields() {
        return fields;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
        return other instanceof CreateBroadcastInput input && fields.equals(input.fields);
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }

    @Override
    public String toString() {
        return "CreateBroadcastInput" + fields;
    }

    /** Builder de {@link CreateBroadcastInput}. */
    public static final class Builder {
        private final Map<String, Object> fields;

        private Builder(final Map<String, Object> required) {
            this.fields = new LinkedHashMap<>(required);
        }

        public Builder subject(final String value) {
            fields.put(SUBJECT, value);
            return this;
        }

        public Builder text(final String value) {
            fields.put("text", value);
            return this;
        }

        public Builder replyTo(final String value) {
            fields.put("replyTo", value);
            return this;
        }

        /**
         * Agenda o envio para um instante futuro.
         *
         * @param value instante do disparo
         * @return o builder
         */
        public Builder scheduledAt(final Instant value) {
            fields.put("scheduledAt", value.toString());
            return this;
        }

        public Builder variables(final Map<String, Object> value) {
            fields.put("variables", Collections.unmodifiableMap(new LinkedHashMap<>(value)));
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public CreateBroadcastInput build() {
            return new CreateBroadcastInput(fields);
        }
    }
}
