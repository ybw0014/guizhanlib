import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    api(project(":guizhanlib-common"))
    api(project(":guizhanlib-localization"))
    api("io.papermc:paperlib:1.0.8")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.93.2")
}

tasks.withType<ShadowJar> {
    relocate("io.papermc.lib", "net.guizhanss.guizhanlib.libs.paperlib")
}
