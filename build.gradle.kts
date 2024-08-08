plugins {
    `java-library`
    id("io.freefair.lombok") version "8.7.1"
}

group = "net.guizhanss"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io/")
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven(url = "https://papermc.io/repo/repository/maven-public")
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.freefair.lombok")

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
        api("io.papermc:paperlib:1.0.8")
        api("com.google.code.findbugs:jsr305:3.0.2")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    }
}
