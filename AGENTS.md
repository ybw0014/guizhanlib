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
- `guizhanlib-updater`: Guizhan Builds updater integration.
- `guizhanlib-all`: aggregator artifact re-exporting all active modules;
  packaging/docs surface only, not a primary source module.

## 2. Build

- Default iteration command: `./gradlew build` — compiles, runs tests, and
  produces the publishable shadow jar (un classifier) plus an `-original` unshaded
  jar per module.
- Aggregated Javadocs are generated from the `guizhanlib-all` module only — it
  re-exports every submodule; never generate library-wide Javadocs from a leaf.

## 3. Publishing

Publishing is manual from this machine or from the manual `Release` GitHub
Action; CI is build-only. One command covers both paths:
`./gradlew publish`.

- Snapshot versions (`-SNAPSHOT`): uploads straight to the Central Portal
  snapshots repo (expires after 90 days without a new upload); close/release
  tasks self-skip.
- Release versions (no suffix): publishes, then closes and releases the
  Central staging repo automatically.
- Releases can be triggered from the manual `Release` GitHub Action
  (`.github/workflows/release.yml`): it publishes the version currently set in
  gradle.properties via `./gradlew publish`; non-snapshot versions additionally
  get the `v<version>` tag and a GitHub release from the matching CHANGELOG
  section.
- Sonatype credentials and the signing key come from gradle properties outside
  the repo (`~/.gradle/gradle.properties`); nothing is stored here.

## 4. Conventions

- Java 17; follow existing Lombok usage in each area (`@UtilityClass`, `@Getter`,
  `@Data`, …) instead of hand-writing the boilerplate it covers.
- Fail-fast argument validation with `Preconditions`; keep nullability annotations.
- Every public or protected class and method MUST have Javadoc — this is a
  library, consumers read them. Javadoc and comments stay in English.
- Respect the layering: `common` stays platform-agnostic; `minecraft` keeps no
  Slimefun/Rebar compile-time coupling; higher-level modules own their platform
  adapters. Move reusable Bukkit/plugin behavior down into `guizhanlib-minecraft`
  instead of duplicating it in higher-level modules.
- Prefer adding shared behavior to an existing base class (e.g. AbstractJavaPlugin)
  over introducing a new intermediate abstract class for behavior the current
  base can host directly.
- `README.md` and `CHANGELOG.md` are bilingual: English text paired with Chinese
  text, not English-only.
