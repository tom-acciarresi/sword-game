plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.gradleup.shadow") version "9.6.1"
}

javafx {
    version = "25"
    modules = listOf(
        "javafx.controls"
        )
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jspecify:jspecify:1.0.0")
    implementation("com.google.code.gson:gson:2.14.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = "it.unicam.cs.mpgc.rpg130730.Main"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

version = "1.0.4"
