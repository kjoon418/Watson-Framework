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
    implementation("ch.qos.logback:logback-classic:1.5.20")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2")

    // Reflections
    implementation("org.reflections:reflections:0.10.2")

    // JWT
    implementation("com.auth0:java-jwt:4.4.0")

    //
    implementation("org.mindrot:jbcrypt:0.4")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
