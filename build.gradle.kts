plugins {
    java
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    mavenLocal()
    mavenCentral()

    // Used for query types
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

version = "1.2.0"
group = "pink.zak"

dependencies {
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("com.google.code.gson:gson:2.8.7")
    implementation("com.influxdb:influxdb-client-java:2.3.0")

    // Used for query types
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}