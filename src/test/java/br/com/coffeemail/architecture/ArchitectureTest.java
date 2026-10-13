package br.com.coffeemail.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "br.com.coffeemail", importOptions = ImportOption.DoNotIncludeTests.class)
final class ArchitectureTest {

    private static final String RESOURCES = "br.com.coffeemail.resources..";
    private static final String RESOURCE_IMPLEMENTATIONS = "br.com.coffeemail.internal.resources..";
    private static final String MODELS = "br.com.coffeemail.models..";
    private static final String TRANSPORT_LIBRARIES = "java.net.http..";
    private static final String JSON_LIBRARY = "com.fasterxml.jackson.databind..";

    @ArchTest
    static final ArchRule noStandardStreams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    static final ArchRule noGenericExceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    static final ArchRule noJavaUtilLogging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    static final ArchRule publicResourcesAreInterfaces =
            classes().that().resideInAPackage(RESOURCES).should().beInterfaces();

    @ArchTest
    static final ArchRule resourcesDoNotTouchTheTransportOrJson = noClasses()
            .that()
            .resideInAnyPackage(RESOURCES, RESOURCE_IMPLEMENTATIONS)
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(TRANSPORT_LIBRARIES, JSON_LIBRARY);

    @ArchTest
    static final ArchRule modelsAreFreeOfInfrastructure = noClasses()
            .that()
            .resideInAnyPackage(MODELS)
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(TRANSPORT_LIBRARIES, JSON_LIBRARY, "br.com.coffeemail.internal..", RESOURCES);

    @ArchTest
    static final ArchRule publicApiDoesNotDependOnInternalsOfImplementation = noClasses()
            .that()
            .resideInAnyPackage(RESOURCES, MODELS, "br.com.coffeemail.errors..", "br.com.coffeemail.transport..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("br.com.coffeemail.internal.http..", RESOURCE_IMPLEMENTATIONS);

    @ArchTest
    static final ArchRule onlyTheJdkAdapterUsesTheHttpClient = noClasses()
            .that()
            .doNotHaveFullyQualifiedName("br.com.coffeemail.internal.http.JdkHttpTransport")
            .and()
            .doNotHaveFullyQualifiedName("br.com.coffeemail.internal.http.RequestExecutor")
            .should()
            .dependOnClassesThat()
            .resideInAPackage(TRANSPORT_LIBRARIES);

    private ArchitectureTest() {}
}
