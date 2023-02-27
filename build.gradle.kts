tasks.wrapper {
    gradleVersion = "7.5.1"
    distributionType = Wrapper.DistributionType.ALL
}

group = "moe.aspirin"
version = "3.0.1"
description = "An anime pics bot for Telegram, written on java, taking data from reddit."
java.sourceCompatibility = JavaVersion.VERSION_14
application {
    mainClass.set("moe.aspirin.Main")
}

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.0.0"
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
        attributes["Main-Class"] = "moe.aspirin.Main"
    }
}