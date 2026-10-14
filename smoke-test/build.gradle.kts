plugins {
    application
}

val sdkVersion = providers.gradleProperty("sdkVersion").orElse("0.1.0")
val modular = providers.gradleProperty("modular").map(String::toBoolean).orElse(true)

repositories {
    mavenLocal()
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    modularity.inferModulePath = modular
}

dependencies {
    implementation(sdkVersion.map { "br.com.coffeemail:coffeemail-java:$it" })
}

sourceSets.main {
    java {
        if (!modular.get()) {
            exclude("module-info.java")
        }
    }
}

application {
    mainClass = "smoke.Main"
    if (modular.get()) {
        mainModule = "coffeemail.smoke"
    }
}
