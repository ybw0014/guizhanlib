package net.guizhanss.guizhanlib.updater;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Updates a plugin from Hangar for a specific platform. Resolution first requests a filtered
 * version list and then retrieves details for its first entry; the API defaults to
 * {@code createdAt:desc} but does not guarantee ordering. The candidate must provide a download
 * for the requested platform; hosted installations require SHA-256, and {@code externalUrl}
 * candidates are notification-only. The {@code {author}/{slug}} API path remains functional but
 * is deprecated in the official OpenAPI specification.
 */
public final class HangarUpdater extends AbstractUpdater {
    private static final URI ORIGIN = URI.create("https://hangar.papermc.io");
    private final String author;
    private final String slug;
    private final String channel;
    private final String platform;

    /**
     * Schedules an update check on the Release channel.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param author the project author
     * @param slug the Hangar project slug or ID
     * @param platform the required Hangar platform, such as {@code PAPER}
     */
    public static void start(Plugin plugin, File file, String author, String slug, String platform) {
        start(plugin, file, author, slug, "Release", platform, UpdaterConfig.DEFAULT);
    }

    /**
     * Schedules an update check on the Release channel with a configuration.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param author the project author
     * @param slug the Hangar project slug or ID
     * @param platform the required Hangar platform, such as {@code PAPER}
     * @param config the update configuration
     */
    public static void start(Plugin plugin, File file, String author, String slug, String platform, UpdaterConfig config) {
        start(plugin, file, author, slug, "Release", platform, config);
    }

    /**
     * Schedules an update check on a specified channel.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param author the project author
     * @param slug the Hangar project slug or ID
     * @param channel the version channel
     * @param platform the required Hangar platform, such as {@code PAPER}
     */
    public static void start(Plugin plugin, File file, String author, String slug, String channel, String platform) {
        start(plugin, file, author, slug, channel, platform, UpdaterConfig.DEFAULT);
    }

    /**
     * Schedules an update check on a specified channel with a configuration.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param author the project author
     * @param slug the Hangar project slug or ID
     * @param channel the version channel
     * @param platform the required Hangar platform, such as {@code PAPER}
     * @param config the update configuration
     */
    public static void start(Plugin plugin, File file, String author, String slug, String channel, String platform, UpdaterConfig config) {
        var updater = new HangarUpdater(plugin, file, author, slug, channel, platform, config);
        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getScheduler().runTaskAsynchronously(plugin, updater::run), 1L);
    }

    HangarUpdater(Plugin plugin, File file, String author, String slug, String channel, String platform, UpdaterConfig config) {
        super(plugin, file, config, ORIGIN);
        this.author = Preconditions.checkNotNull(author);
        this.slug = Preconditions.checkNotNull(slug);
        this.channel = Preconditions.checkNotNull(channel);
        this.platform = Preconditions.checkNotNull(platform);
    }

    /** {@inheritDoc} */
    @Override
    protected Optional<UpdateInfo> checkUpdate() throws Exception {
        String project = ORIGIN + "/api/v1/projects/" + encode(author) + "/" + encode(slug) + "/versions";
        var list = fetch(URI.create(project + "?limit=1&platform=" + encode(platform) + "&channel=" + encode(channel)));
        if (!successful(list)) return Optional.empty();
        var versions = JsonParser.parseString(list.body()).getAsJsonObject().getAsJsonArray("result");
        if (versions.isEmpty()) return Optional.empty();
        String name = versions.get(0).getAsJsonObject().get("name").getAsString();
        var details = fetch(URI.create(project + "/" + encode(name)));
        if (!successful(details)) return Optional.empty();
        JsonObject version = JsonParser.parseString(details.body()).getAsJsonObject();
        JsonObject downloads = version.getAsJsonObject("downloads");
        if (downloads == null || !downloads.has(platform) || !downloads.get(platform).isJsonObject()) {
            log(Level.WARNING, "Hangar candidate has no download for platform %s.", platform);
            return Optional.empty();
        }
        JsonObject download = downloads.getAsJsonObject(platform);
        String externalUrl = string(download, "externalUrl");
        boolean external = externalUrl != null && !externalUrl.isBlank();
        String url = external ? externalUrl : string(download, "downloadUrl");
        if (url == null || url.isBlank()) throw new IllegalStateException("Missing Hangar download URL");
        URI uri = URI.create(url);
        JsonObject fileInfo = download.has("fileInfo") && download.get("fileInfo").isJsonObject() ? download.getAsJsonObject("fileInfo") : null;
        String checksum = fileInfo == null ? null : string(fileInfo, "sha256Hash");
        String filename = fileInfo == null ? null : string(fileInfo, "name");
        if (filename == null || filename.isBlank()) {
            String path = uri.getRawPath();
            filename = path == null || path.isEmpty() || path.endsWith("/") ? file().getName()
                : URLDecoder.decode(path.substring(path.lastIndexOf('/') + 1).replace("+", "%2B"), StandardCharsets.UTF_8);
        }
        if (!safeBasename(filename)) throw new IllegalStateException("Unsafe Hangar file name");
        if (!external && !config().checkOnly() && !validSha256(checksum)) throw new IllegalStateException("Missing or invalid Hangar SHA-256 checksum");
        if (!external && !config().checkOnly() && !filename.toLowerCase(Locale.ROOT).endsWith(".jar")) throw new IllegalStateException("Hangar file is not a JAR");
        return Optional.of(new UpdateInfo(name, uri, filename, checksum, "SHA-256", external));
    }

    private boolean successful(HttpResponse<?> response) {
        int status = response.statusCode();
        if (status == 429) {
            String retry = response.headers().firstValue("Retry-After").map(value -> " Retry-After: " + value + ".").orElse("");
            String reset = response.headers().firstValue("X-Ratelimit-Reset").map(value -> " X-Ratelimit-Reset: " + value + ".").orElse("");
            log(Level.WARNING, "Hangar rate limit reached (HTTP 429).%s%s", retry, reset);
            return false;
        }
        if (status == 404) {
            log(Level.WARNING, "Hangar project or version not found (HTTP 404).");
            return false;
        }
        if (status != 200) throw new IllegalStateException("Hangar HTTP " + status);
        return true;
    }

    private static String string(JsonObject object, String key) {
        return object.has(key) && !object.get(key).isJsonNull() ? object.get(key).getAsString() : null;
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
