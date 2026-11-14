package net.guizhanss.guizhanlib.updater;

import com.google.common.base.Preconditions;
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
 * Updates a plugin using GitHub's {@code /releases/latest} endpoint, which excludes drafts and
 * prereleases. An optional case-sensitive asset-name suffix must match exactly one asset, and
 * installation requires its {@code sha256:...} digest. Anonymous requests are limited to 60 per
 * hour; an API token supplied through {@link UpdaterConfig} raises that limit. Tags are compared
 * as-is: a {@code v} prefix that does not match the plugin version triggers the version comparison
 * fallback to string inequality, which may repeatedly report an update or allow a downgrade.
 */
public final class GitHubReleaseUpdater extends AbstractUpdater {
    private static final URI ORIGIN = URI.create("https://api.github.com");
    private final String owner;
    private final String repo;
    private final String assetName;

    /**
     * Schedules an update check requiring exactly one release asset.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param owner the repository owner
     * @param repo the repository name
     */
    public static void start(Plugin plugin, File file, String owner, String repo) {
        start(plugin, file, owner, repo, null, UpdaterConfig.DEFAULT);
    }

    /**
     * Schedules an update check requiring exactly one release asset with a configuration.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param owner the repository owner
     * @param repo the repository name
     * @param config the update configuration
     */
    public static void start(Plugin plugin, File file, String owner, String repo, UpdaterConfig config) {
        start(plugin, file, owner, repo, null, config);
    }

    /**
     * Schedules an update check using an asset name suffix.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param owner the repository owner
     * @param repo the repository name
     * @param assetName the case-sensitive asset name suffix, or {@code null} to match all assets
     */
    public static void start(Plugin plugin, File file, String owner, String repo, String assetName) {
        start(plugin, file, owner, repo, assetName, UpdaterConfig.DEFAULT);
    }

    /**
     * Schedules an update check using an optional asset name suffix and configuration.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param owner the repository owner
     * @param repo the repository name
     * @param assetName the case-sensitive asset name suffix, or {@code null} to match all assets
     * @param config the update configuration
     */
    public static void start(Plugin plugin, File file, String owner, String repo, String assetName, UpdaterConfig config) {
        var updater = new GitHubReleaseUpdater(plugin, file, owner, repo, assetName, config);
        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getScheduler().runTaskAsynchronously(plugin, updater::run), 1L);
    }

    GitHubReleaseUpdater(Plugin plugin, File file, String owner, String repo, String assetName, UpdaterConfig config) {
        super(plugin, file, config, ORIGIN);
        this.owner = Preconditions.checkNotNull(owner);
        this.repo = Preconditions.checkNotNull(repo);
        this.assetName = assetName;
    }

    /** {@inheritDoc} */
    @Override
    protected Optional<UpdateInfo> checkUpdate() throws Exception {
        var response = fetch(URI.create(ORIGIN + "/repos/" + encode(owner) + "/" + encode(repo) + "/releases/latest"));
        int status = response.statusCode();
        if (status == 429 || (status == 403 && (response.headers().firstValue("X-RateLimit-Remaining").orElse("").equals("0")
            || response.headers().firstValue("Retry-After").isPresent() || secondaryLimit(response.body())))) {
            // GitHub's reset header is Unix epoch seconds, unlike Modrinth's remaining seconds.
            String reset = response.headers().firstValue("X-RateLimit-Reset").map(GitHubReleaseUpdater::resetHint).orElse("");
            String retry = response.headers().firstValue("Retry-After").map(value -> " Retry-After: " + value + ".").orElse("");
            log(Level.WARNING, "GitHub rate limit reached (HTTP %d).%s%s", status, reset, retry);
            return Optional.empty();
        }
        if (status == 404) {
            log(Level.WARNING, "GitHub repository or release not found (HTTP 404).");
            return Optional.empty();
        }
        if (status != 200) throw new IllegalStateException("GitHub HTTP " + status);
        JsonObject release = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject selected = null;
        for (var element : release.getAsJsonArray("assets")) {
            JsonObject asset = element.getAsJsonObject();
            String name = asset.get("name").getAsString();
            if (assetName == null || name.endsWith(assetName)) {
                if (selected != null) {
                    log(Level.WARNING, "GitHub release has multiple matching assets.");
                    return Optional.empty();
                }
                selected = asset;
            }
        }
        if (selected == null) {
            log(Level.WARNING, "GitHub release has no matching asset.");
            return Optional.empty();
        }
        String name = selected.get("name").getAsString();
        if (!safeBasename(name)) throw new IllegalStateException("Unsafe GitHub asset name");
        String digest = selected.has("digest") && !selected.get("digest").isJsonNull() ? selected.get("digest").getAsString() : null;
        String checksum = digest != null && digest.startsWith("sha256:") ? digest.substring("sha256:".length()) : null;
        if (!validSha256(checksum)) checksum = null;
        if (!config().checkOnly() && checksum == null) throw new IllegalStateException("Missing or invalid GitHub SHA-256 digest");
        if (!config().checkOnly() && !name.toLowerCase(Locale.ROOT).endsWith(".jar")) throw new IllegalStateException("GitHub asset is not a JAR");
        return Optional.of(new UpdateInfo(release.get("tag_name").getAsString(), URI.create(selected.get("browser_download_url").getAsString()), name, checksum, "SHA-256", false));
    }

    private static boolean secondaryLimit(String body) {
        try {
            var json = JsonParser.parseString(body);
            if (!json.isJsonObject()) return false;
            var message = json.getAsJsonObject().get("message");
            if (message == null || !message.isJsonPrimitive() || !message.getAsJsonPrimitive().isString()) return false;
            String text = message.getAsString().toLowerCase(Locale.ROOT);
            return text.contains("secondary rate limit") || text.contains("abuse");
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private static String resetHint(String value) {
        try {
            return " X-RateLimit-Reset: " + Instant.ofEpochSecond(Long.parseLong(value)) + ".";
        } catch (RuntimeException ex) {
            return " X-RateLimit-Reset: " + value + ".";
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
