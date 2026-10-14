import java.net.URI
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

plugins {
    `java-library`
    checkstyle
    jacoco
    alias(libs.plugins.spotless)
    alias(libs.plugins.errorprone)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.japicmp)
    alias(libs.plugins.cyclonedx)
}

val compileJdkVersion = 21
val targetJdkVersion = 17
val testJdkVersion = providers.gradleProperty("testJavaVersion").map(String::toInt).orElse(targetJdkVersion)
val sdkVersion = project.version.toString()

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(compileJdkVersion)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jdk8)
    implementation(libs.jackson.datatype.jsr310)
    compileOnlyApi(libs.jspecify)

    errorprone(libs.errorprone.core)
    errorprone(libs.nullaway)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.archunit.junit5)
    testImplementation(libs.swagger.request.validator.core)
    testCompileOnly(libs.jspecify)
    testRuntimeOnly(libs.junit.platform.launcher)
}

val generateSdkVersion = tasks.register("generateSdkVersion") {
    val outputDir = layout.buildDirectory.dir("generated/sources/sdkVersion/java/main")
    val versionValue = sdkVersion
    inputs.property("version", versionValue)
    outputs.dir(outputDir)
    doLast {
        val target = outputDir.get().file("br/com/coffeemail/internal/constants/SdkVersion.java").asFile
        target.parentFile.mkdirs()
        target.writeText(
            """
            |package br.com.coffeemail.internal.constants;
            |
            |public final class SdkVersion {
            |    public static final String VALUE = "$versionValue";
            |
            |    private SdkVersion() {}
            |}
            |""".trimMargin(),
        )
    }
}

sourceSets.main {
    java.srcDir(generateSdkVersion)
}

tasks.withType<JavaCompile>().configureEach {
    options.release = targetJdkVersion
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror", "-XDaddTypeAnnotationsToSymbol=true"))
    options.errorprone {
        disableWarningsInGeneratedCode = true
        excludedPaths = ".*/build/generated/.*"
        allErrorsAsWarnings = false
        check("NullAway", CheckSeverity.ERROR)
        option("NullAway:AnnotatedPackages", "br.com.coffeemail")
        option("NullAway:JSpecifyMode", "true")
    }
}

tasks.named<JavaCompile>("compileTestJava") {
    options.errorprone {
        check("NullAway", CheckSeverity.OFF)
    }
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        addBooleanOption("Xdoclint:all,-missing", true)
        addBooleanOption("Werror", true)
    }
}

tasks.test {
    useJUnitPlatform()
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = testJdkVersion.map(JavaLanguageVersion::of)
    }
    systemProperty("coffeemail.expectedSdkVersion", sdkVersion)
    finalizedBy(tasks.jacocoTestReport)
}

checkstyle {
    toolVersion = libs.versions.checkstyle.get()
    configDirectory = layout.projectDirectory.dir("config/checkstyle")
    maxWarnings = 0
    isIgnoreFailures = false
}

tasks.named<Checkstyle>("checkstyleMain") {
    setSource(fileTree("src/main/java"))
}

spotless {
    java {
        target("src/**/*.java", "examples/src/**/*.java")
        palantirJavaFormat(libs.versions.palantirJavaFormat.get())
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

val coverageExclusions = listOf("br/com/coffeemail/internal/constants/SdkVersion.class")

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(sourceSets.main.get().output.asFileTree.matching { exclude(coverageExclusions) })
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    classDirectories.setFrom(sourceSets.main.get().output.asFileTree.matching { exclude(coverageExclusions) })
    violationRules {
        rule {
            limit {
                counter = "LINE"
                minimum = "0.85".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                minimum = "0.78".toBigDecimal()
            }
        }
    }
}

val verifyVersionSync = tasks.register("verifyVersionSync") {
    group = "verification"
    description = "Valida o formato semver da versão e, com -PreleaseTag, que a tag corresponde a ela."
    val versionValue = sdkVersion
    val releaseTag = providers.gradleProperty("releaseTag")
    doLast {
        val semver = Regex("""\d+\.\d+\.\d+(-[0-9A-Za-z.-]+)?""")
        check(semver.matches(versionValue)) { "Versão '$versionValue' não segue semver." }
        val tag = releaseTag.orNull
        check(tag == null || tag == "v$versionValue") { "A tag '$tag' não corresponde à versão v$versionValue." }
    }
}

val openApiSourceOverride = providers.environmentVariable("COFFEEMAIL_OPENAPI_SPEC")
val openApiSource = openApiSourceOverride.orElse("https://api.coffeemail.com.br/v1/product/docs/json")
val openApiSnapshot = layout.projectDirectory.file("spec/product.json")

val readOpenApi: (String) -> ByteArray? = { location ->
    val remote = location.startsWith("http://") || location.startsWith("https://")
    val file = File(location)
    when {
        remote -> URI.create(location).toURL().readBytes()
        file.isFile -> file.readBytes()
        else -> null
    }
}

val syncOpenApi = tasks.register("syncOpenApi") {
    group = "openapi"
    description = "Atualiza spec/product.json a partir de COFFEEMAIL_OPENAPI_SPEC ou do spec público da API."
    val source = openApiSource
    val target = openApiSnapshot
    val reader = readOpenApi
    doLast {
        val content = reader(source.get()) ?: throw GradleException("Spec OpenAPI não encontrado em ${source.get()}.")
        target.asFile.writeBytes(content)
    }
}

val verifyOpenApiSnapshot = tasks.register("verifyOpenApiSnapshot") {
    group = "verification"
    description = "Falha se spec/product.json divergir do spec em COFFEEMAIL_OPENAPI_SPEC. Pula quando a variável não está definida (CI)."
    val source = openApiSourceOverride
    val target = openApiSnapshot
    val reader = readOpenApi
    doLast {
        val location = source.orNull
        val content = location?.let(reader)
        if (content == null) {
            logger.lifecycle("Spec OpenAPI de origem indisponível (${location ?: "COFFEEMAIL_OPENAPI_SPEC não definida"}); verificação do snapshot pulada.")
            return@doLast
        }
        check(content.contentEquals(target.asFile.readBytes())) {
            "spec/product.json está desatualizado em relação a ${source.get()}. Rode ./gradlew syncOpenApi."
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }
    coordinates(project.group.toString(), "coffeemail-java", sdkVersion)
    pom {
        name = providers.gradleProperty("pomName")
        description =
            "SDK oficial da CoffeeMail para Java: envio transacional, domínios, templates, audiências, campanhas," +
                " supressões, webhooks e estatísticas."
        url = providers.gradleProperty("pomUrl")
        inceptionYear = "2026"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/license/mit"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "dev-etto"
                name = providers.gradleProperty("pomDeveloperName")
                organization = "CoffeeMail"
                organizationUrl = "https://github.com/Coffee-Mail"
            }
        }
        scm {
            url = providers.gradleProperty("pomUrl")
            connection = "scm:git:https://github.com/Coffee-Mail/coffee-mail-sdk-java.git"
            developerConnection = "scm:git:ssh://git@github.com/Coffee-Mail/coffee-mail-sdk-java.git"
        }
        issueManagement {
            system = "GitHub"
            url = "https://github.com/Coffee-Mail/coffee-mail-sdk-java/issues"
        }
    }
}

tasks.named<org.cyclonedx.gradle.CyclonedxDirectTask>("cyclonedxDirectBom") {
    includeConfigs = listOf("runtimeClasspath")
    projectType = org.cyclonedx.model.Component.Type.LIBRARY
    componentName = "coffeemail-java"
}

val apiBaselineVersion = providers.gradleProperty("apiBaselineVersion").orElse("")
val apiBaseline = configurations.detachedConfiguration().apply {
    dependencies.addLater(apiBaselineVersion.filter(String::isNotBlank).map { baseline ->
        project.dependencies.create("br.com.coffeemail:coffeemail-java:$baseline") as ExternalModuleDependency
    })
    isTransitive = false
}

val verifyApiCompatibility = tasks.register<me.champeau.gradle.japicmp.JapicmpTask>("verifyApiCompatibility") {
    group = "verification"
    description = "Falha se a API pública quebrar a compatibilidade com a versão em apiBaselineVersion."
    val baseline = apiBaselineVersion
    onlyIf { baseline.get().isNotBlank() }
    oldClasspath.from(apiBaseline)
    newClasspath.from(tasks.jar)
    onlyModified = true
    failOnSourceIncompatibility = true
    failOnModification = false
    ignoreMissingClasses = true
    packageExcludes = listOf("br.com.coffeemail.internal*", "br.com.coffeemail.models.support")
    compatibilityChangeExcludes = listOf("METHOD_ADDED_TO_INTERFACE")
    methodExcludes = listOf(
        "br.com.coffeemail.CoffeeMailBuilder#baseUrl(java.lang.String)",
        "br.com.coffeemail.CoffeeMailBuilder#baseUrl(java.net.URI)",
    )
    htmlOutputFile = layout.buildDirectory.file("reports/japicmp/index.html")
}

tasks.check {
    dependsOn(verifyApiCompatibility)
    dependsOn(
        tasks.jacocoTestCoverageVerification,
        tasks.javadoc,
        verifyVersionSync,
        verifyOpenApiSnapshot,
    )
}
