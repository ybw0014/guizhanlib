dependencies {
    api(project(":guizhanlib-common", configuration = "shadow"))
    api(project(":guizhanlib-localization", configuration = "shadow"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.1")
}
