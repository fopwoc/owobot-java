group = "io.github.fopwoc"
version = "3.0.3"
description = "An anime pics bot for Telegram, written on java, taking data from reddit."
java.sourceCompatibility = JavaVersion.VERSION_17
application {
    mainClass.set("io.github.fopwoc.Main")
}

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.github.mattbdean:JRAW:1.1.0")
    implementation("org.telegram:telegrambots:6.5.0")
    implementation("org.telegram:telegrambots-abilities:6.5.0")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "io.github.fopwoc.Main"
    }
}
