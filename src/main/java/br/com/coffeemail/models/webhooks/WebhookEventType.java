package br.com.coffeemail.models.webhooks;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Evento emitido pela plataforma via webhook. */
public enum WebhookEventType {
    /** {@code email.queued}. */
    EMAIL_QUEUED("email.queued"),
    /** {@code email.processing}. */
    EMAIL_PROCESSING("email.processing"),
    /** {@code email.sent}. */
    EMAIL_SENT("email.sent"),
    /** {@code email.delivered}. */
    EMAIL_DELIVERED("email.delivered"),
    /** {@code email.delivery_delayed}. */
    EMAIL_DELIVERY_DELAYED("email.delivery_delayed"),
    /** {@code email.opened}. */
    EMAIL_OPENED("email.opened"),
    /** {@code email.clicked}. */
    EMAIL_CLICKED("email.clicked"),
    /** {@code email.bounced}. */
    EMAIL_BOUNCED("email.bounced"),
    /** {@code email.complained}. */
    EMAIL_COMPLAINED("email.complained"),
    /** {@code email.failed}. */
    EMAIL_FAILED("email.failed"),
    /** {@code email.suppressed}. */
    EMAIL_SUPPRESSED("email.suppressed"),
    /** {@code email.scheduled}. */
    EMAIL_SCHEDULED("email.scheduled"),
    /** {@code email.cancelled}. */
    EMAIL_CANCELLED("email.cancelled"),
    /** {@code email.unsubscribed}. */
    EMAIL_UNSUBSCRIBED("email.unsubscribed"),
    /** {@code billing.plan_changed}. */
    BILLING_PLAN_CHANGED("billing.plan_changed"),
    /** Valor ainda desconhecido por esta versão do SDK. */
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String value;

    WebhookEventType(final String value) {
        this.value = value;
    }

    /**
     * Valor usado pela API.
     *
     * @return o valor serializado
     */
    @JsonValue
    public String value() {
        return value;
    }
}
