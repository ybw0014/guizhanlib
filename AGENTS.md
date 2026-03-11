# AGENTS.md

## 1. Overview
GuizhanLib is a Gradle multi-project Java library for Slimefun and Rebar addon development. This root guide covers the whole repository and defines the structural, coding, and day-to-day development workflow rules agents should follow.

## 2. Folder Structure
- `settings.gradle.kts`: source of truth for active modules; use it instead of directory names when deciding what is part of the build.
- `build.gradle.kts`: shared Gradle logic for all active modules, including Java 17, Lombok, Shadow, publishing, and common test dependencies.
- `gradle/`: wrapper and shared Gradle infrastructure.
- `guizhanlib-common`: platform-agnostic helpers, primitives, and utility classes.
- `guizhanlib-libraries`: Libby-based runtime library loading support.
- `guizhanlib-localization`: plugin-owned language loading and lookup abstractions.
- `guizhanlib-minecraft`: shared Bukkit/Paper-facing config, command, plugin, compatibility, and environment utilities.
- `guizhanlib-rebar`: Rebar addon base abstractions built on top of `guizhanlib-minecraft`.
- `guizhanlib-slimefun`: Slimefun addon bases plus machine/menu abstractions built on top of `guizhanlib-minecraft`.
- `guizhanlib-slimefun-cn`: Slimefun CN compatibility helpers.
- `guizhanlib-updater`: Guizhan Builds updater helpers.
- `guizhanlib-all`: aggregator artifact that re-exports active modules; treat it as packaging rather than a primary source module.
- `.sisyphus/`: planning artifacts for agent workflows, not published library sources.

## 3. Working Agreements
- Treat this file as the only repository-level AGENTS guide unless the user explicitly asks for a more granular layout.
- Respect the current layering: `common` stays platform-agnostic, `minecraft` stays free of Slimefun/Rebar compile-time coupling, and higher-level modules own their platform-specific adapters and lifecycle hooks.
- Keep changes inside the owning module when possible, and prefer moving reusable Bukkit/plugin behavior down into `guizhanlib-minecraft` instead of duplicating it in higher-level modules.
- For normal debugging, local compile checks, and artifact generation, prefer `./gradlew clean shadowJar` over `./gradlew build`.
- Avoid using `build` as the default iteration command in this repo: it is broader than needed for most debug loops and the current workspace also contains non-ignored IDE artifacts such as `.settings/`, `.factorypath`, and `bin/` that should not be treated as source changes.
- If workspace artifacts such as `.settings/`, `.factorypath`, or `bin/` appear in searches or diffs, treat them as tooling noise unless the user explicitly asks to work on IDE/project metadata.
- Preserve shared code conventions observed across the repo: Java 17, Lombok usage, explicit nullability annotations, fail-fast argument validation with `Preconditions`, and concise English Javadocs/comments.
- Because this repository is a library project, every public or protected class and method MUST have Javadocs.
- Follow the existing Lombok style instead of hand-writing repetitive boilerplate when the repository already uses annotations such as `@UtilityClass`, `@Getter`, `@Setter`, `@Data`, `@Builder`, or controlled `AccessLevel` variants in the same area.
- Keep Java source comments, Javadocs, and in-repo technical documentation in English.
- Keep `README.md` and `CHANGELOG.md` bilingual in English and Chinese; the existing pattern is English text paired with Chinese text rather than English-only release notes.
- Use existing patterns before introducing new abstractions: utility classes commonly use `@UtilityClass`, plugin-scoped wrappers are thin objects around a plugin instance, and behavior-heavy shared code should gain or keep focused tests.
