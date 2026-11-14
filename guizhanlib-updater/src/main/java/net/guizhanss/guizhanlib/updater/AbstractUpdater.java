package net.guizhanss.guizhanlib.updater;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.HexFormat;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import javax.net.ssl.SSLSession;

/**
 * Provides the shared update flow for platform-specific updaters.
 * Subclasses fetch a remote candidate, which this class compares with the plugin version before
 * either notifying the user or downloading and verifying the update for atomic installation in
 * the server's update folder. Callers must schedule {@link #run()} asynchronously, off the main
 * server thread. Downloads use a temporary file outside the update scan root and are moved only
 * after checksum verification; if an atomic move fails, no fallback copy is attempted.
 */
public abstract class AbstractUpdater {
    private static final java.util.concurrent.ScheduledExecutorService WATCHDOG = Executors.newScheduledThreadPool(2, task -> {
        Thread thread = new Thread(task, "guizhan-updater-watchdog");
        thread.setDaemon(true);
        return thread;
    });
    private final Plugin plugin;
    private final File file;
    private final UpdaterConfig config;
    private final URI apiOrigin;
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10))
        .followRedirects(HttpClient.Redirect.NEVER).build();
    private final boolean debug = Boolean.getBoolean("guizhanlib.updater.debug");
    private Sender sender = client::send;
    // Metadata should be quick; downloads get longer but must not occupy an async worker indefinitely.
    private Duration metadataTimeout = Duration.ofSeconds(15);
    private Duration downloadTimeout = Duration.ofSeconds(60);

    interface Sender {
        <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> handler) throws IOException, InterruptedException;
    }

    void setSender(Sender sender) { this.sender = sender; }
    void setTimeouts(Duration metadata, Duration download) { metadataTimeout = metadata; downloadTimeout = download; }

    /**
     * Creates an updater for a plugin and its currently installed file.
     *
     * @param plugin the plugin being updated
     * @param file the currently installed plugin file
     * @param config the update configuration
     * @param apiOrigin the platform API origin to which an optional token may be sent
     */
    protected AbstractUpdater(Plugin plugin, File file, UpdaterConfig config, URI apiOrigin) {
        this.plugin = Preconditions.checkNotNull(plugin);
        this.file = Preconditions.checkNotNull(file);
        this.config = Preconditions.checkNotNull(config);
        this.apiOrigin = Preconditions.checkNotNull(apiOrigin);
    }

    /**
     * Fetches the latest candidate from the update provider without installing it.
     *
     * @return the candidate, or an empty optional if none is available
     * @throws Exception if the provider request or response cannot be processed
     */
    protected abstract Optional<UpdateInfo> checkUpdate() throws Exception;

    /** Executes the update flow; callers must run this asynchronously, off the server thread. */
    protected final void run() {
        try {
            var candidate = checkUpdate();
            if (candidate.isEmpty()) return;
            var info = candidate.get();
            if (!VersionComparison.needsUpdate(info.version(), plugin.getPluginMeta().getVersion(), plugin.getLogger())) return;
            log(Level.INFO, "%s has an update: %s", plugin.getName(), info.version());
            if (config.checkOnly() || info.external()) {
                log(Level.INFO, "Download the update manually: %s", info.downloadUrl());
            } else {
                download(info);
            }
        } catch (Exception ex) {
            if (ex instanceof InterruptedException) Thread.currentThread().interrupt();
            log(Level.SEVERE, "Update check or installation failed: %s", ex.getMessage());
            if (debug) plugin.getLogger().log(Level.SEVERE, "Updater failure", ex);
        }
    }

    /**
     * Logs a formatted updater message using the plugin's logger.
     *
     * @param level the log level
     * @param message the format string
     * @param args the format arguments
     */
    protected final void log(Level level, String message, Object... args) {
        plugin.getLogger().log(level, String.format(message, args));
    }

    /**
     * Returns the updater configuration.
     *
     * @return the configuration
     */
    protected final UpdaterConfig config() { return config; }

    /**
     * Returns the plugin being updated.
     *
     * @return the plugin
     */
    protected final Plugin plugin() { return plugin; }

    /**
     * Returns the currently installed plugin file.
     *
     * @return the plugin file
     */
    protected final File file() { return file; }

    <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> handler) throws IOException, InterruptedException {
        return sender.send(request, handler);
    }

    HttpResponse<String> fetch(URI uri) throws IOException, InterruptedException {
        return fetch(uri, null, metadataTimeout);
    }

    private HttpResponse<String> fetch(URI uri, Path destination, Duration timeout) throws IOException, InterruptedException {
        boolean sendToken = sameOrigin(apiOrigin, uri);
        for (int redirects = 0; redirects <= 5; redirects++) {
            if (uri.getHost() == null || !("https".equalsIgnoreCase(uri.getScheme()) || "http".equalsIgnoreCase(uri.getScheme()))) {
                throw new IOException("Invalid HTTP URL");
            }
            var builder = HttpRequest.newBuilder(uri).timeout(timeout)
                .header("User-Agent", config.userAgent() == null ? plugin.getName() + "/" + plugin.getPluginMeta().getVersion() + " (GuizhanLib)" : config.userAgent())
                .GET();
            if (sendToken && sameOrigin(apiOrigin, uri) && config.apiKey() != null && !config.apiKey().isBlank()) builder.header("Authorization", "Bearer " + config.apiKey());
            HttpResponse<InputStream> response = send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
            int status = response.statusCode();
            boolean redirect = status == 301 || status == 302 || status == 303 || status == 307 || status == 308;
            String body = null;
            if (redirect || (destination != null && status != 200)) {
                response.body().close();
            } else {
                AtomicBoolean timedOut = new AtomicBoolean();
                try (InputStream input = response.body()) {
                    var deadline = WATCHDOG.schedule(() -> {
                        timedOut.set(true);
                        try { input.close(); } catch (IOException ignored) { }
                    }, timeout.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
                    try {
                        if (destination == null) body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                        else Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
                        if (timedOut.get()) throw new IOException("Response body timed out");
                    } catch (IOException ex) {
                        if (timedOut.get()) throw new IOException("Response body timed out", ex);
                        throw ex;
                    } finally {
                        deadline.cancel(false);
                    }
                }
            }
            if (!redirect) return new TextResponse(response, body);
            URI next = uri.resolve(response.headers().firstValue("Location").orElseThrow(() -> new IOException("Redirect without location")));
            if ("https".equalsIgnoreCase(uri.getScheme()) && !"https".equalsIgnoreCase(next.getScheme())) throw new IOException("Insecure redirect");
            if (!sameOrigin(apiOrigin, next)) sendToken = false;
            uri = next;
        }
        throw new IOException("Too many redirects");
    }

    private record TextResponse(HttpResponse<?> source, String body) implements HttpResponse<String> {
        public int statusCode() { return source.statusCode(); }
        public HttpRequest request() { return source.request(); }
        public Optional<HttpResponse<String>> previousResponse() { return Optional.empty(); }
        public HttpHeaders headers() { return source.headers(); }
        public Optional<SSLSession> sslSession() { return source.sslSession(); }
        public URI uri() { return source.uri(); }
        public HttpClient.Version version() { return source.version(); }
    }

    private static boolean sameOrigin(URI a, URI b) {
        return a.getHost() != null && b.getHost() != null && a.getScheme().equalsIgnoreCase(b.getScheme())
            && a.getHost().equalsIgnoreCase(b.getHost()) && port(a) == port(b);
    }

    private static int port(URI uri) {
        return uri.getPort() >= 0 ? uri.getPort() : "https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
    }

    /** Downloads and verifies a candidate before atomically staging it for installation. */
    void download(UpdateInfo info) throws Exception {
        if (!safeBasename(info.fileName()) || !info.fileName().toLowerCase(java.util.Locale.ROOT).endsWith(".jar")) throw new IOException("Unsafe or non-JAR file name");
        if (!validChecksum(info.checksumAlgorithm(), info.checksum())) throw new IOException("Invalid checksum or algorithm");
        Path data = plugin.getDataFolder().toPath();
        Path update = plugin.getServer().getUpdateFolderFile().toPath();
        Files.createDirectories(data);
        Files.createDirectories(update);
        if (data.toRealPath().startsWith(update.toRealPath())) throw new IOException("Plugin data folder is inside the update folder");
        Path temp = Files.createTempFile(data, "guizhan-update-", ".tmp");
        try {
            HttpResponse<String> response = fetch(info.downloadUrl(), temp, downloadTimeout);
            if (response.statusCode() != 200) throw new IOException("Download HTTP status " + response.statusCode());
            if (!verifyChecksum(temp, info.checksumAlgorithm(), info.checksum())) throw new IOException(info.checksumAlgorithm() + " checksum mismatch");
            move(temp, update.resolve(info.fileName()));
            log(Level.INFO, "Downloaded %s; restart the server to install it.", info.fileName());
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    void move(Path source, Path target) throws IOException {
        Files.move(source, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    static boolean safeBasename(String name) {
        return name != null && !name.isBlank() && !name.equals(".") && !name.equals("..")
            && !name.contains("/") && !name.contains("\\") && !name.contains(":") && name.indexOf('\0') < 0;
    }

    static boolean validSha256(String checksum) {
        return validChecksum("SHA-256", checksum);
    }

    static boolean validChecksum(String algorithm, String checksum) {
        int length = "SHA-256".equals(algorithm) ? 64 : "SHA-512".equals(algorithm) ? 128 : 0;
        return checksum != null && length != 0 && checksum.matches("[0-9a-fA-F]{" + length + "}");
    }

    static boolean verifyChecksum(Path file, String algorithm, String checksum) throws Exception {
        if (!validChecksum(algorithm, checksum)) return false;
        var digest = MessageDigest.getInstance(algorithm);
        try (var input = Files.newInputStream(file)) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = input.read(buffer)) != -1) digest.update(buffer, 0, count);
        }
        return MessageDigest.isEqual(digest.digest(), HexFormat.of().parseHex(checksum));
    }
}
