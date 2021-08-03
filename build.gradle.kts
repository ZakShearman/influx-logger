plugins {
    `java-library`
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    mavenLocal()
    mavenCentral()

    // Used for query types
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

version = "1.2.2"
group = "pink.zak"

dependencies {
    compileOnly("com.google.guava:guava:30.1.1-jre")
    compileOnly("com.google.code.gson:gson:2.8.7")
    api("com.influxdb:influxdb-client-java:2.3.0")

    // Used for query types
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}
