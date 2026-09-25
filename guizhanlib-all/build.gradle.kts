import org.gradle.api.tasks.javadoc.Javadoc

dependencies {
    api(project(":guizhanlib-common", configuration = "shadow"))
    api(project(":guizhanlib-libraries", configuration = "shadow"))
    api(project(":guizhanlib-localization", configuration = "shadow"))
    api(project(":guizhanlib-minecraft", configuration = "shadow"))
    api(project(":guizhanlib-rebar", configuration = "shadow"))
    api(project(":guizhanlib-slimefun", configuration = "shadow"))
    api(project(":guizhanlib-slimefun-cn", configuration = "shadow"))
    api(project(":guizhanlib-updater", configuration = "shadow"))
}

// Aggregate the delomboked sources of all submodules so this module publishes a real javadoc jar.
val documentedModules = listOf(
    ":guizhanlib-common",
    ":guizhanlib-libraries",
    ":guizhanlib-localization",
    ":guizhanlib-minecraft",
    ":guizhanlib-rebar",
    ":guizhanlib-slimefun",
    ":guizhanlib-slimefun-cn",
    ":guizhanlib-updater",
)

documentedModules.forEach { evaluationDependsOn(it) }

tasks.named<Javadoc>("javadoc") {
    documentedModules.forEach { path ->
        val p = project(path)
        val main = p.the<SourceSetContainer>().named("main").get()
        source(p.layout.buildDirectory.dir("generated/sources/delombok/java/main"))
        classpath += main.compileClasspath + main.output
        dependsOn("$path:delombok")
    }
}
