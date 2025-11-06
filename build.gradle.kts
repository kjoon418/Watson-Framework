plugins {
    kotlin("jvm") version "2.0.0"
}

group = "goodspace"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.tomcat.embed:tomcat-embed-core:10.1.30")
    implementation("org.apache.tomcat.embed:tomcat-embed-logging-juli:10.1.30")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
