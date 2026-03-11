dependencies {
    api(project(":guizhanlib-common", configuration = "shadow"))
    api(project(":guizhanlib-localization", configuration = "shadow"))
    api(project(":guizhanlib-minecraft", configuration = "shadow"))
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("io.github.pylonmc:rebar:0.35.0")
    testImplementation("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    testImplementation("com.github.MockBukkit:MockBukkit:v1.21-SNAPSHOT")
    testImplementation("io.github.pylonmc:rebar:0.35.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.1")
}
