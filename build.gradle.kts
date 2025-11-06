plugins {
    kotlin("jvm") version "2.0.0"
}

group = "goodspace"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Tomcat
    implementation("org.apache.tomcat.embed:tomcat-embed-core:11.0.12")

    // SLF4J
    implementation("org.slf4j:slf4j-api:2.0.13")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
