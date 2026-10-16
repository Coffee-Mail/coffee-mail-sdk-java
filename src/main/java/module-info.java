module br.com.coffeemail {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.net.http;
    requires static transitive org.jspecify;

    exports br.com.coffeemail;
    exports br.com.coffeemail.errors;
    exports br.com.coffeemail.resources;
    exports br.com.coffeemail.transport;
    exports br.com.coffeemail.webhooks;
    exports br.com.coffeemail.models.common;
    exports br.com.coffeemail.models.emails;
    exports br.com.coffeemail.models.domains;
    exports br.com.coffeemail.models.templates;
    exports br.com.coffeemail.models.audiences;
    exports br.com.coffeemail.models.broadcasts;
    exports br.com.coffeemail.models.senders;
    exports br.com.coffeemail.models.suppressions;
    exports br.com.coffeemail.models.webhooks;
    exports br.com.coffeemail.models.stats;
    exports br.com.coffeemail.models.validation;

    opens br.com.coffeemail.models.common to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.emails to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.domains to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.templates to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.audiences to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.broadcasts to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.senders to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.suppressions to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.webhooks to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.stats to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.models.validation to
            com.fasterxml.jackson.databind;
    opens br.com.coffeemail.internal.wire to
            com.fasterxml.jackson.databind;
}
