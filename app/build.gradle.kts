plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
    version = "25"
    modules = listOf(
        "javafx.controls"
        )
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit test framework.
    testImplementation(libs.junit)

    // This dependency is used by the application.
    implementation(libs.guava)

    implementation("com.google.code.gson:gson:2.14.0")

    compileOnly("org.jspecify:jspecify:1.0.0")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    // Define the main class for the application.
    mainClass = "it.unicam.cs.mpgc.rpg130730.Main"
}
