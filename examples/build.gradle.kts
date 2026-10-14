plugins {
    application
    checkstyle
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":"))
    compileOnly(libs.spring.web)
    compileOnly(libs.spring.context)
}

application {
    mainClass = "br.com.coffeemail.examples.SendEmailExample"
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 17
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror"))
}

checkstyle {
    toolVersion = libs.versions.checkstyle.get()
    configDirectory = rootProject.layout.projectDirectory.dir("config/checkstyle")
    maxWarnings = 0
}

tasks.javadoc {
    enabled = false
}
