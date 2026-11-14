package net.guizhanss.guizhanlib.updater;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Updates a plugin from Modrinth for a specific loader and game version. Since the version list
 * has no guaranteed order, the updater selects the latest published candidate client-side using
 * {@code date_published}. A candidate must have exactly one primary file, and installation
 * requires its SHA-512 checksum. Anonymous requests are limited to 300 per minute; a unique
 * User-Agent is recommended.
 */
public final class ModrinthUpdater extends AbstractUpdater {
    private static final URI ORIGIN = URI.create("https://api.modrinth.com");
    private final String slug;
    private final String loader;
    private final String gameVersion;

    /**
     * Schedules an update check with the default configuration.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param slug the Modrinth project slug or ID
     * @param loader the required loader
     * @param gameVersion the required Minecraft version
     */
    public static void start(Plugin plugin, File file, String slug, String loader, String gameVersion) {
        start(plugin, file, slug, loader, gameVersion, UpdaterConfig.DEFAULT);
    }

    /**
     * Schedules an update check with the supplied configuration.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param slug the Modrinth project slug or ID
     * @param loader the required loader
     * @param gameVersion the required Minecraft version
     * @param config the update configuration
     */
    public static void start(Plugin plugin, File file, String slug, String loader, String gameVersion, UpdaterConfig config) {
        var updater = new ModrinthUpdater(plugin, file, slug, loader, gameVersion, config);
        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getScheduler().runTaskAsynchronously(plugin, updater::run), 1L);
    }

    ModrinthUpdater(Plugin plugin, File file, String slug, String loader, String gameVersion, UpdaterConfig config) {
        super(plugin, file, config, ORIGIN);
        this.slug = Preconditions.checkNotNull(slug);
        this.loader = Preconditions.checkNotNull(loader);
        this.gameVersion = Preconditions.checkNotNull(gameVersion);
    }

    /** {@inheritDoc} */
    @Override
    protected Optional<UpdateInfo> checkUpdate() throws Exception {
        JsonArray loaders = new JsonArray();
        loaders.add(loader);
        JsonArray versions = new JsonArray();
        versions.add(gameVersion);
        URI url = URI.create(ORIGIN + "/v2/project/" + encode(slug) + "/version?loaders=" + encode(loaders.toString()) + "&game_versions=" + encode(versions.toString()));
        var response = fetch(url);
        if (response.statusCode() == 429) {
            // Modrinth's reset header is a number of remaining seconds, not a Unix timestamp.
            String reset = response.headers().firstValue("X-Ratelimit-Reset").map(value -> " X-Ratelimit-Reset: " + value + " seconds.").orElse("");
            String retry = response.headers().firstValue("Retry-After").map(value -> " Retry-After: " + value + ".").orElse("");
            log(Level.WARNING, "Modrinth rate limit reached (HTTP 429).%s%s", reset, retry);
            return Optional.empty();
        }
        if (response.statusCode() == 404) {
            log(Level.WARNING, "Modrinth project or versions not found (HTTP 404).");
            return Optional.empty();
        }
        if (response.statusCode() != 200) throw new IllegalStateException("Modrinth HTTP " + response.statusCode());
        JsonArray results = JsonParser.parseString(response.body()).getAsJsonArray();
        JsonObject latest = null;
        Instant latestDate = null;
        for (var result : results) {
            JsonObject version = result.getAsJsonObject();
            Instant date = Instant.parse(version.get("date_published").getAsString());
            if (latestDate == null || date.isAfter(latestDate)) {
                latest = version;
                latestDate = date;
            }
        }
        if (latest == null) return Optional.empty();
        JsonObject primary = null;
        for (var element : latest.getAsJsonArray("files")) {
            JsonObject file = element.getAsJsonObject();
            if (file.has("primary") && !file.get("primary").isJsonNull() && file.get("primary").getAsBoolean()) {
                if (primary != null) {
                    log(Level.WARNING, "Modrinth candidate has multiple primary files.");
                    return Optional.empty();
                }
                primary = file;
            }
        }
        if (primary == null) {
            log(Level.WARNING, "Modrinth candidate has no primary file.");
            return Optional.empty();
        }
        String name = primary.get("filename").getAsString();
        if (!safeBasename(name)) throw new IllegalStateException("Unsafe Modrinth file name");
        JsonObject hashes = primary.has("hashes") && primary.get("hashes").isJsonObject() ? primary.getAsJsonObject("hashes") : null;
        String checksum = hashes != null && hashes.has("sha512") && !hashes.get("sha512").isJsonNull() ? hashes.get("sha512").getAsString() : null;
        if (!config().checkOnly() && !validChecksum("SHA-512", checksum)) throw new IllegalStateException("Missing or invalid Modrinth SHA-512 checksum");
        if (!config().checkOnly() && !name.toLowerCase(Locale.ROOT).endsWith(".jar")) throw new IllegalStateException("Modrinth primary file is not a JAR");
        return Optional.of(new UpdateInfo(latest.get("version_number").getAsString(), URI.create(primary.get("url").getAsString()), name, checksum, "SHA-512", false));
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
