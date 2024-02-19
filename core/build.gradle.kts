import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(project(":pokemmo"))

    // Config
    implementation("com.sksamuel.hoplite:hoplite:1.0.3")
    implementation("com.sksamuel.hoplite:hoplite-json:2.7.2")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.6")

    // DB
    implementation("mysql:mysql-connector-java:8.0.32")

    // HTTP
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // XML
    implementation("org.simpleframework:simple-xml:2.7.1")
    
    // JSON
    implementation("com.google.code.gson:gson:2.9.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("ItemScraper")
    manifest {
        attributes("Main-Class" to "de.fiereu.ItemScraperKt")
    }
}

tasks.test {
    useJUnitPlatform()
}