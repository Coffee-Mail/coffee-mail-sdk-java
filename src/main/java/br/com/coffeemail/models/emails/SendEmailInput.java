package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Dados de um e-mail transacional. Use {@link #builder(String, String)}.
 *
 * @param from remetente; o domínio deve estar verificado
 * @param to destinatário único
 * @param cc destinatários em cópia
 * @param bcc destinatários em cópia oculta
 * @param replyTo endereço de resposta
 * @param subject assunto; obrigatório quando não há {@code templateId}
 * @param html conteúdo HTML
 * @param text conteúdo em texto puro
 * @param templateId id ou alias de um template cadastrado; não pode ser usado com {@code html}/{@code text}
 * @param variables variáveis do template
 * @param scheduledAt envio agendado
 * @param headers cabeçalhos MIME adicionais
 * @param attachments anexos
 * @param tags tags de métricas e segmentação
 * @param idempotencyKey chave de idempotência, enviada no cabeçalho {@code x-idempotency-key}
 * @param emailType classificação transacional ou broadcast
 * @param suppressionCategory categoria de supressão considerada no envio
 * @param metadata metadados livres
 * @param sandbox simula a entrega sem enviar, via cabeçalho {@code x-coffeemail-sandbox}
 */
public record SendEmailInput(
        EmailAddress from,
        EmailAddress to,
        @Nullable List<EmailAddress> cc,
        @Nullable List<EmailAddress> bcc,
        @Nullable EmailAddress replyTo,
        @Nullable String subject,
        @Nullable String html,
        @Nullable String text,
        @Nullable String templateId,
        @Nullable Map<String, Object> variables,
        @Nullable Instant scheduledAt,
        @Nullable Map<String, String> headers,
        @Nullable List<EmailAttachment> attachments,
        @Nullable List<EmailTag> tags,
        @Nullable String idempotencyKey,
        @Nullable EmailType emailType,
        @Nullable String suppressionCategory,
        @Nullable Map<String, Object> metadata,
        boolean sandbox) {

    /**
     * Inicia a construção com remetente e destinatário em texto ({@code a@b.com} ou {@code Nome <a@b.com>}).
     *
     * @param from remetente
     * @param to destinatário
     * @return o builder
     */
    public static Builder builder(final String from, final String to) {
        return builder(EmailAddress.parse(from), EmailAddress.parse(to));
    }

    /**
     * Inicia a construção com remetente e destinatário estruturados.
     *
     * @param from remetente
     * @param to destinatário
     * @return o builder
     */
    public static Builder builder(final EmailAddress from, final EmailAddress to) {
        return new Builder(from, to);
    }

    /** Builder de {@link SendEmailInput}. */
    public static final class Builder {
        private final EmailAddress from;
        private final EmailAddress to;
        private @Nullable List<EmailAddress> cc;
        private @Nullable List<EmailAddress> bcc;
        private @Nullable EmailAddress replyTo;
        private @Nullable String subject;
        private @Nullable String html;
        private @Nullable String text;
        private @Nullable String templateId;
        private @Nullable Map<String, Object> variables;
        private @Nullable Instant scheduledAt;
        private @Nullable Map<String, String> headers;
        private @Nullable List<EmailAttachment> attachments;
        private @Nullable List<EmailTag> tags;
        private @Nullable String idempotencyKey;
        private @Nullable EmailType emailType;
        private @Nullable String suppressionCategory;
        private @Nullable Map<String, Object> metadata;
        private boolean sandbox;

        private Builder(final EmailAddress from, final EmailAddress to) {
            this.from = from;
            this.to = to;
        }

        public Builder cc(final String... values) {
            return cc(Arrays.stream(values).map(EmailAddress::parse).toList());
        }

        public Builder cc(final List<EmailAddress> values) {
            this.cc = List.copyOf(values);
            return this;
        }

        public Builder bcc(final String... values) {
            return bcc(Arrays.stream(values).map(EmailAddress::parse).toList());
        }

        public Builder bcc(final List<EmailAddress> values) {
            this.bcc = List.copyOf(values);
            return this;
        }

        public Builder replyTo(final String value) {
            return replyTo(EmailAddress.parse(value));
        }

        public Builder replyTo(final EmailAddress value) {
            this.replyTo = value;
            return this;
        }

        public Builder subject(final String value) {
            this.subject = value;
            return this;
        }

        public Builder html(final String value) {
            this.html = value;
            return this;
        }

        public Builder text(final String value) {
            this.text = value;
            return this;
        }

        public Builder templateId(final String value) {
            this.templateId = value;
            return this;
        }

        public Builder variables(final Map<String, Object> value) {
            this.variables = Copies.map(value);
            return this;
        }

        public Builder scheduledAt(final Instant value) {
            this.scheduledAt = value;
            return this;
        }

        public Builder headers(final Map<String, String> value) {
            this.headers = Copies.map(value);
            return this;
        }

        public Builder attachments(final EmailAttachment... values) {
            return attachments(List.of(values));
        }

        public Builder attachments(final List<EmailAttachment> values) {
            this.attachments = List.copyOf(values);
            return this;
        }

        public Builder tags(final EmailTag... values) {
            return tags(List.of(values));
        }

        public Builder tags(final List<EmailTag> values) {
            this.tags = List.copyOf(values);
            return this;
        }

        public Builder idempotencyKey(final String value) {
            this.idempotencyKey = value;
            return this;
        }

        public Builder emailType(final EmailType value) {
            this.emailType = value;
            return this;
        }

        public Builder suppressionCategory(final String value) {
            this.suppressionCategory = value;
            return this;
        }

        public Builder metadata(final Map<String, Object> value) {
            this.metadata = Copies.map(value);
            return this;
        }

        public Builder sandbox(final boolean value) {
            this.sandbox = value;
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public SendEmailInput build() {
            return new SendEmailInput(
                    from,
                    to,
                    cc,
                    bcc,
                    replyTo,
                    subject,
                    html,
                    text,
                    templateId,
                    variables,
                    scheduledAt,
                    headers,
                    attachments,
                    tags,
                    idempotencyKey,
                    emailType,
                    suppressionCategory,
                    metadata,
                    sandbox);
        }
    }
}
