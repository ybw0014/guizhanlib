# AGENTS.md

## 1. Module Map

`settings.gradle.kts` decides which modules are active — judge by it, not by
directories (deleted-module leftovers and non-ignored tooling artifacts like
`.settings/`, `.factorypath`, `bin/` are not source).

- `guizhanlib-common`: platform-agnostic helpers; no Bukkit dependencies.
- `guizhanlib-libraries`: Libby-based runtime dependency loading.
- `guizhanlib-localization`: plugin-owned language loading and lookup.
- `guizhanlib-minecraft`: shared Bukkit/Paper utilities (config, command,
  plugin, compatibility). Must stay free of Slimefun/Rebar compile-time coupling.
- `guizhanlib-rebar`: Rebar addon base abstractions on top of `guizhanlib-minecraft`.
- `guizhanlib-slimefun`: Slimefun addon bases and machine/menu abstractions on
  top of `guizhanlib-minecraft`.
- `guizhanlib-updater`: auto-updaters for Guizhan Resources, Modrinth, Hangar
  and GitHub Releases behind a shared `AbstractUpdater`; deprecated
  `GuizhanBuildsUpdater` retained until a removal decision.
- `guizhanlib-all`: aggregator artifact re-exporting all active modules;
  packaging/docs surface only, not a primary source module.

## 2. Commands

| Task | Command |
|---|---|
| Full build (compiles, runs tests, shadow jar per module) | `./gradlew build` |
| One module's tests | `./gradlew :guizhanlib-updater:test` |
| One test class | `./gradlew :guizhanlib-updater:test --tests "net.guizhanss.guizhanlib.updater.UpdaterTest"` |
| Aggregated Javadoc | `./gradlew :guizhanlib-all:javadoc` |

- Always the wrapper (`./gradlew`, Gradle 9.8.0), never a system Gradle.
- A Gradle `test` task may be UP-TO-DATE and run nothing; force with
  `cleanTest test` when an edit should have re-run tests.
- Aggregated Javadocs are generated from the `guizhanlib-all` module only — it
  re-exports every submodule; never generate library-wide Javadocs from a leaf.

## 3. Publishing

Publishing is manual from this machine or from the manual `Release` GitHub
Action; CI is build-only. One command covers both paths: `./gradlew publish`.

- Do not run `./gradlew publish` unless the user explicitly asks for a release.
- Snapshot versions (`-SNAPSHOT`) upload straight to the Central Portal
  snapshots repo (expire after 90 days without a new upload); release versions
  additionally close and release the Central staging repo automatically.
- The `Release` Action (`.github/workflows/release.yml`) publishes the version
  currently set in `gradle.properties`; non-snapshot versions additionally get
  the `v<version>` tag and a GitHub release from the matching `CHANGELOG.md`
  section.
- CI (`.github/workflows/ci.yml`) runs `./gradlew build` on JDK 25 only, and
  its `paths` filter only matches root-level `*.java`, so module-only changes
  may not trigger it — do not treat CI as the local check gate.
- Sonatype credentials and the signing key come from gradle properties outside
  the repo (`~/.gradle/gradle.properties`); nothing is stored here.

## 4. Conventions

- Java 21 (`options.release = 21`) on the JDK 25 toolchain; JUnit 5 + MockBukkit
  are wired for every module in the root build script.
- Inter-module dependencies must use the `shadow` configuration variant, as the
  existing module scripts do.
- Follow existing Lombok usage in each area (`@UtilityClass`, `@Getter`,
  `@Data`, …) instead of hand-writing the boilerplate it covers.
- Fail-fast argument validation with `Preconditions`; keep nullability annotations.
- Every public or protected class and method MUST have Javadoc — this is a
  library, consumers read them. Javadoc and comments stay in English.
- Respect the layering: `common` stays platform-agnostic; `minecraft` keeps no
  Slimefun/Rebar compile-time coupling. Move reusable Bukkit/plugin behavior
  down into `guizhanlib-minecraft` instead of duplicating it in higher-level
  modules.
- Prefer adding shared behavior to an existing base class (e.g. AbstractJavaPlugin)
  over introducing a new intermediate abstract class for behavior the current
  base can host directly.
- `README.md` and `CHANGELOG.md` are bilingual: write the complete English
  version first, then the complete Chinese version — never interleaved.
