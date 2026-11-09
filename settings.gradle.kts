plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "guizhanlib"

include(
    "guizhanlib-common",
    "guizhanlib-libraries",
    "guizhanlib-localization",
    "guizhanlib-minecraft",
    "guizhanlib-rebar",
    "guizhanlib-slimefun",
    "guizhanlib-updater",
    "guizhanlib-all",
)
