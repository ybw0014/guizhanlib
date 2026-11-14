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
import java.util.Optional;
import java.util.logging.Level;

/**
 * Updates a plugin using the Guizhan Resources API. A single request to
 * {@code GET {base}/projects/{slug}/versions/latest?channel=...} returns the version and files;
 * the default base is {@code https://resources.guizhanss.com/api/v1} and the default channel is
 * {@code release}. Station-hosted installations require a SHA-256 checksum, while
 * {@code external_url} files are notification-only. Envelope code 5300 means no candidate,
 * whereas codes 5200 and 5000 indicate configuration errors.
 */
public final class GuizhanResourcesUpdater extends AbstractUpdater {
    private static final String DEFAULT_BASE = "https://resources.guizhanss.com/api/v1";
    private final String slug;
    private final String channel;
    private final String base;

    /**
     * Schedules an update check on the release channel with the default configuration.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param slug the Resources project slug or ID
     */
    public static void start(Plugin plugin, File file, String slug) {
        start(plugin, file, slug, "release", UpdaterConfig.DEFAULT);
    }

    /**
     * Schedules an update check on the specified channel.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param slug the Resources project slug or ID
     * @param channel the version channel
     */
    public static void start(Plugin plugin, File file, String slug, String channel) {
        start(plugin, file, slug, channel, UpdaterConfig.DEFAULT);
    }

    /**
     * Schedules an update check on the release channel with a configuration.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param slug the Resources project slug or ID
     * @param config the update configuration
     */
    public static void start(Plugin plugin, File file, String slug, UpdaterConfig config) {
        start(plugin, file, slug, "release", config);
    }

    /**
     * Schedules an update check on the specified channel with a configuration.
     * @param plugin the plugin to update
     * @param file the installed plugin file
     * @param slug the Resources project slug or ID
     * @param channel the version channel
     * @param config the update configuration
     */
    public static void start(Plugin plugin, File file, String slug, String channel, UpdaterConfig config) {
        Preconditions.checkNotNull(plugin);
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(slug);
        Preconditions.checkNotNull(channel);
        Preconditions.checkNotNull(config);
        var updater = new GuizhanResourcesUpdater(plugin, file, slug, channel, config);
        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getScheduler().runTaskAsynchronously(plugin, updater::run), 1L);
    }

    GuizhanResourcesUpdater(Plugin plugin, File file, String slug, String channel, UpdaterConfig config) {
        super(plugin, file, config, URI.create(baseUrl(config)));
        this.slug = Preconditions.checkNotNull(slug);
        this.channel = Preconditions.checkNotNull(channel);
        this.base = baseUrl(config);
    }

    private static String baseUrl(UpdaterConfig config) {
        String configured = Preconditions.checkNotNull(config).baseUrl();
        return configured == null || configured.equals("https://builds.guizhanss.com/") ? DEFAULT_BASE : configured.replaceAll("/+$", "");
    }

    /** {@inheritDoc} */
    @Override
    protected Optional<UpdateInfo> checkUpdate() throws Exception {
        String project = base + "/projects/" + encode(slug);
        String query = "?channel=" + encode(channel);
        if (config().minecraftVersion() != null) query += "&minecraft_version=" + encode(config().minecraftVersion());
        if (config().platform() != null) query += "&platform=" + encode(config().platform());
        var response = fetch(URI.create(project + "/versions/latest" + query));
        if (response.statusCode() == 429) {
            log(Level.WARNING, "Guizhan Resources rate limit reached (HTTP 429).");
            return Optional.empty();
        }
        JsonObject envelope = JsonParser.parseString(response.body()).getAsJsonObject();
        int code = envelope.get("code").getAsInt();
        if (code == 11) {
            log(Level.WARNING, "Guizhan Resources rate limit reached (HTTP %d, code %d).", response.statusCode(), code);
            return Optional.empty();
        }
        if (code == 5300) return Optional.empty();
        if (code == 5200 || code == 5000) {
            log(Level.WARNING, "Guizhan Resources configuration error (code %d).", code);
            return Optional.empty();
        }
        if (response.statusCode() != 200 || code != 0) throw new IllegalStateException("Guizhan Resources HTTP " + response.statusCode() + ", code " + code);
        JsonObject data = envelope.getAsJsonObject("data");
        if (!compatible(data, "minecraft_versions", config().minecraftVersion()) || !compatible(data, "platforms", config().platform())) {
            log(Level.WARNING, "Guizhan Resources candidate is incompatible with the configured filters.");
            return Optional.empty();
        }
        JsonObject primary = primary(data.getAsJsonArray("files"));
        if (primary == null) {
            log(Level.WARNING, "Guizhan Resources candidate does not have exactly one primary file.");
            return Optional.empty();
        }
        String name = primary.get("name").getAsString();
        if (!safeBasename(name)) throw new IllegalStateException("Unsafe resource file name");
        boolean external = primary.has("external_url") && !primary.get("external_url").isJsonNull()
            && !primary.get("external_url").getAsString().isBlank();
        String checksum = primary.has("checksum") && primary.get("checksum").isJsonObject()
            && primary.getAsJsonObject("checksum").has("sha256") && !primary.getAsJsonObject("checksum").get("sha256").isJsonNull()
            ? primary.getAsJsonObject("checksum").get("sha256").getAsString() : null;
        if (!external && !config().checkOnly() && !validSha256(checksum)) throw new IllegalStateException("Missing or invalid resource SHA-256 checksum");
        if (!external && !config().checkOnly() && !name.toLowerCase(java.util.Locale.ROOT).endsWith(".jar")) throw new IllegalStateException("Resource primary file is not a JAR");
        URI download = URI.create(project + "/versions/" + encode(data.get("id").getAsString()) + "/download");
        return Optional.of(new UpdateInfo(data.get("version").getAsString(), download, name, checksum, "SHA-256", external));
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    static JsonObject primary(JsonArray files) {
        if (files == null) return null;
        JsonObject result = null;
        for (var file : files) {
            JsonObject item = file.getAsJsonObject();
            if (item.has("is_primary") && !item.get("is_primary").isJsonNull() && item.get("is_primary").getAsBoolean()) {
                if (result != null) return null;
                result = item;
            }
        }
        return result;
    }

    private static boolean compatible(JsonObject data, String key, String filter) {
        if (filter == null || !data.has(key) || !data.get(key).isJsonArray()) return true;
        JsonArray values = data.getAsJsonArray(key);
        if (values.isEmpty()) return true;
        for (var value : values) if (filter.equalsIgnoreCase(value.getAsString())) return true;
        return false;
    }
}
