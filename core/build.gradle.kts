import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.fiereu"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":pokemmo"))

    // Config
    implementation("com.sksamuel.hoplite:hoplite:1.0.3")
    implementation("com.sksamuel.hoplite:hoplite-json:2.7.5")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    // DB
    implementation("mysql:mysql-connector-java:8.0.32")

    // HTTP
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // XML
    implementation("org.simpleframework:simple-xml:2.7.1")
    
    // JSON
    implementation("com.google.code.gson:gson:2.10")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("ItemScraper")
    manifest {
        attributes("Main-Class" to "de.fiereu.ItemScraperKt")
    }
}