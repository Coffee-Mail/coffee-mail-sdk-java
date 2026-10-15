package br.com.coffeemail.internal.constants;

public final class ApiRoutes {

    private static final String ID = "/{id}";
    private static final String CANCEL = "/cancel";
    private static final String VERIFY = "/verify";
    private static final String TEST_SEND = "/test-send";
    private static final String BULK = "/bulk";

    public static final String PRODUCT = "/v1/product";
    public static final String API_KEY_INTROSPECTION = PRODUCT + "/auth/me/api-key";

    public static final String EMAILS = PRODUCT + "/emails";
    public static final String EMAILS_BATCH = EMAILS + "/batch";
    public static final String EMAILS_TAGS = EMAILS + "/tags";
    public static final String EMAIL = EMAILS + ID;
    public static final String EMAIL_EVENTS = EMAIL + "/events";
    public static final String EMAIL_CANCEL = EMAIL + CANCEL;
    public static final String EMAIL_RESEND = EMAIL + "/resend";

    public static final String DOMAINS = PRODUCT + "/domains";
    public static final String DOMAIN = DOMAINS + ID;
    public static final String DOMAIN_VERIFY = DOMAIN + VERIFY;
    public static final String DOMAIN_HEALTH = DOMAIN + "/health";
    public static final String DOMAIN_WARMUP = DOMAIN + "/warmup";
    public static final String DOMAIN_DKIM_ROTATE = DOMAIN + "/dkim/rotate";
    public static final String DOMAIN_TRACKING_DOMAIN = DOMAIN + "/tracking-domain";

    public static final String TEMPLATES = PRODUCT + "/templates";
    public static final String TEMPLATE = TEMPLATES + ID;
    public static final String TEMPLATES_PREVIEW = TEMPLATES + "/preview";
    public static final String TEMPLATES_FORMAT = TEMPLATES + "/format";
    public static final String TEMPLATES_TEST_RENDER = TEMPLATES + "/test-render";
    public static final String TEMPLATE_TEST_SEND = TEMPLATE + TEST_SEND;

    public static final String AUDIENCES = PRODUCT + "/audiences";
    public static final String AUDIENCE = AUDIENCES + ID;
    public static final String AUDIENCE_CONTACTS = AUDIENCE + "/contacts";
    public static final String AUDIENCE_CONTACTS_BULK = AUDIENCE_CONTACTS + BULK;
    public static final String AUDIENCE_CONTACT = AUDIENCE_CONTACTS + "/{contactId}";

    public static final String BROADCASTS = PRODUCT + "/broadcasts";
    public static final String BROADCAST = BROADCASTS + ID;
    public static final String BROADCAST_SEND = BROADCAST + "/send";
    public static final String BROADCAST_TEST_SEND = BROADCAST + TEST_SEND;
    public static final String BROADCAST_CANCEL = BROADCAST + CANCEL;

    public static final String SENDERS = PRODUCT + "/senders";
    public static final String SENDERS_VERIFY = SENDERS + VERIFY;
    public static final String SENDER = SENDERS + ID;
    public static final String SENDER_RESEND_VERIFICATION = SENDER + "/resend-verification";

    public static final String SUPPRESSIONS = PRODUCT + "/suppressions";
    public static final String SUPPRESSIONS_BULK = SUPPRESSIONS + BULK;
    public static final String SUPPRESSION = SUPPRESSIONS + ID;
    public static final String SUPPRESSION_REACTIVATE = SUPPRESSION + "/reactivate";

    public static final String WEBHOOKS = PRODUCT + "/webhooks";
    public static final String WEBHOOK = WEBHOOKS + ID;
    public static final String WEBHOOK_TEST = WEBHOOK + "/test";
    public static final String WEBHOOK_DELIVERIES = WEBHOOK + "/deliveries";
    public static final String WEBHOOK_ROTATE_SECRET = WEBHOOK + "/rotate-secret";

    public static final String STATS = PRODUCT + "/stats";

    private ApiRoutes() {}
}
