import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm").version("1.3.10")
    id("com.github.johnrengelman.shadow").version("4.0.3")
    application
}

group = "org.discordlist"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("io.javalin:javalin:2.4.0")
    implementation("com.github.Carleslc:Simple-YAML:1.3")
    implementation("org.json:json:20180813")
    implementation("org.apache.logging.log4j:log4j-api:2.11.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.11.1")
    implementation("org.apache.logging.log4j:log4j-core:2.11.1")
    implementation("commons-io:commons-io:2.6")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.6")
    implementation("com.datastax.cassandra:cassandra-driver-core:3.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "org.discordlist.api.BootstrapperKt"
}

tasks {
    "shadowJar"(ShadowJar::class) {
        baseName = project.name
        version = version
        archiveName = "$baseName.$extension"
    }
}