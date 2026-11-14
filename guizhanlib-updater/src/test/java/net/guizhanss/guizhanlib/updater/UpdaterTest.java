package net.guizhanss.guizhanlib.updater;

import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;
import io.papermc.paper.plugin.configuration.PluginMeta;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.CountDownLatch;
import java.time.Duration;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class UpdaterTest {
    @TempDir Path dir;
    private static final Logger LOGGER = Logger.getLogger("updater-test");
    private static final String HASH = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad";
    private static final String HASH512 = "ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f";

    @Test
    void versions() {
        assertTrue(VersionComparison.needsUpdate("26.10", "26.3", LOGGER));
        assertFalse(VersionComparison.needsUpdate("1.0", "1.0.0", LOGGER));
        assertFalse(VersionComparison.needsUpdate("01.002", "1.2", LOGGER));
        assertTrue(VersionComparison.needsUpdate("1.0.0-beta.10", "1.0.0-beta.2", LOGGER));
        assertTrue(VersionComparison.needsUpdate("1.0.0", "1.0.0-SNAPSHOT", LOGGER));
        assertFalse(VersionComparison.needsUpdate("1.0.0-SNAPSHOT", "1.0.0-SNAPSHOT", LOGGER));
        assertFalse(VersionComparison.needsUpdate("1.0+one", "1.0+two", LOGGER));
        assertTrue(VersionComparison.needsUpdate("v1.0.0", "2.0.0", LOGGER));
        List<LogRecord> records = new ArrayList<>();
        Handler handler = logHandler(records);
        LOGGER.addHandler(handler);
        try {
            assertTrue(VersionComparison.needsUpdate("v1.0.0", "1.0.0", LOGGER));
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("version comparison fallback")));
        } finally {
            LOGGER.removeHandler(handler);
        }
    }

    @Test
    void checksumsAndBasenames() throws Exception {
        Path file = Files.writeString(dir.resolve("test"), "abc");
        assertTrue(AbstractUpdater.verifyChecksum(file, "SHA-256", HASH));
        assertFalse(AbstractUpdater.verifyChecksum(file, "SHA-256", "0".repeat(64)));
        assertFalse(AbstractUpdater.verifyChecksum(file, "SHA-256", "bad"));
        assertTrue(AbstractUpdater.verifyChecksum(file, "SHA-512", HASH512));
        assertFalse(AbstractUpdater.verifyChecksum(file, "SHA-512", "0".repeat(128)));
        assertTrue(AbstractUpdater.safeBasename("plugin.jar"));
        for (String name : List.of("../plugin.jar", "..", "x\\plugin.jar", "/plugin.jar", "C:plugin.jar")) {
            assertFalse(AbstractUpdater.safeBasename(name));
        }
    }

    @Test
    void primaryMustBeUnique() {
        assertNull(GuizhanResourcesUpdater.primary(JsonParser.parseString("[]").getAsJsonArray()));
        assertNull(GuizhanResourcesUpdater.primary(JsonParser.parseString("[{\"is_primary\":false}]").getAsJsonArray()));
        assertNull(GuizhanResourcesUpdater.primary(JsonParser.parseString("[{\"is_primary\":true},{\"is_primary\":true}]").getAsJsonArray()));
        assertNotNull(GuizhanResourcesUpdater.primary(JsonParser.parseString("[{\"is_primary\":false},{\"is_primary\":true}]").getAsJsonArray()));
    }

    @Test
    void envelopeCodesAndCandidate() throws Exception {
        var updater = new GuizhanResourcesUpdater(plugin(), new File("current.jar"), "test", "release", new UpdaterConfig());
        for (int code : new int[]{5300, 5200, 5000, 11}) {
            updater.setSender(sender(code == 11 ? 429 : 404, "{\"code\":" + code + ",\"message\":\"error\"}"));
            assertTrue(updater.checkUpdate().isEmpty());
        }
        updater.setSender(sender(200, "{\"code\":0,\"data\":{\"id\":\"12345678\",\"version\":\"2.0\",\"files\":[{\"name\":\"plugin.jar\",\"is_primary\":true,\"checksum\":{\"sha256\":\"" + HASH + "\"}}]}}"));
        assertEquals("https://resources.guizhanss.com/api/v1/projects/test/versions/12345678/download", updater.checkUpdate().orElseThrow().downloadUrl().toString());
        updater.setSender(sender(200, "{\"code\":0,\"data\":{\"id\":\"12345678\",\"version\":\"2.0\",\"files\":[{\"name\":\"plugin.jar\",\"is_primary\":true,\"checksum\":{\"sha256\":\"bad\"}}]}}"));
        assertThrows(IllegalStateException.class, updater::checkUpdate);
        updater.setSender(sender(200, "{\"code\":0,\"data\":{\"id\":\"12345678\",\"version\":\"2.0\",\"files\":[{\"name\":\"plugin.jar\",\"is_primary\":true,\"external_url\":\"https://example.test\"}]}}"));
        assertTrue(updater.checkUpdate().orElseThrow().external());
    }

    @Test
    void atomicMoveFailureDoesNotTouchUpdateFolder() throws Exception {
        Path update = Files.createDirectories(dir.resolve("update"));
        Files.writeString(update.resolve("plugin.jar"), "existing");
        var updater = new AbstractUpdater(plugin(), new File("current.jar"), new UpdaterConfig(), URI.create("https://example.test")) {
            @Override protected Optional<UpdateInfo> checkUpdate() { return Optional.empty(); }
            @Override void move(Path source, Path target) throws IOException { throw new IOException("atomic move unsupported"); }
        };
        updater.setSender(sender(200, "abc"));
        assertThrows(IOException.class, () -> updater.download(new UpdateInfo("2", URI.create("https://example.test/file"), "plugin.jar", HASH, "SHA-256", false)));
        assertEquals("existing", Files.readString(update.resolve("plugin.jar")));
        try (var files = Files.list(update)) { assertEquals(1, files.count()); }
        try (var files = Files.list(dir.resolve("data"))) { assertEquals(0, files.count()); }
    }

    @Test
    void downloadFailureMismatchAndSuccess() throws Exception {
        Path update = Files.createDirectories(dir.resolve("update"));
        Files.writeString(update.resolve("plugin.jar"), "existing");
        var updater = updater(new UpdaterConfig());
        var info = new UpdateInfo("2", URI.create("https://example.test/file"), "plugin.jar", HASH512, "SHA-512", false);
        updater.setSender(sender(500, "failure"));
        assertThrows(IOException.class, () -> updater.download(info));
        assertClean(update, "existing");
        updater.setSender(sender(200, "wrong"));
        assertThrows(IOException.class, () -> updater.download(info));
        assertClean(update, "existing");
        updater.setSender(sender(200, "abc"));
        updater.download(info);
        assertClean(update, "abc");
    }

    @Test
    void checkOnlyAndExternalNeverDownload() throws Exception {
        for (boolean checkOnly : List.of(true, false)) {
            var config = new UpdaterConfig().checkOnly(checkOnly);
            var updater = new GuizhanResourcesUpdater(plugin(), new File("current.jar"), "test", "release", config);
            String extra = checkOnly ? "" : ",\"external_url\":\"https://external.test/file\"";
            String body = "{\"code\":0,\"data\":{\"id\":\"12345678\",\"version\":\"2.0\",\"files\":[{\"name\":\"document.txt\",\"is_primary\":true" + extra + "}]}}";
            List<HttpRequest> requests = new ArrayList<>();
            updater.setSender(capturingSender(requests, List.of(200, 200), List.of(Map.of(), Map.of()), body));
            var candidate = updater.checkUpdate().orElseThrow();
            assertNull(candidate.checksum());
            updater.run();
            assertEquals(2, requests.size());
            assertFalse(Files.exists(dir.resolve("update")));
        }
    }

    @Test
    void requestUrlAndUserAgent() throws Exception {
        var config = new UpdaterConfig().userAgent("Test Agent").minecraftVersion("1.21.4").platform("paper");
        var updater = new GuizhanResourcesUpdater(plugin(), new File("current.jar"), "my slug/one", "pre release", config);
        List<HttpRequest> requests = new ArrayList<>();
        updater.setSender(capturingSender(requests, List.of(404), List.of(Map.of()), "{\"code\":5300}"));
        assertTrue(updater.checkUpdate().isEmpty());
        assertEquals(1, requests.size());
        assertEquals("https://resources.guizhanss.com/api/v1/projects/my%20slug%2Fone/versions/latest?channel=pre%20release&minecraft_version=1.21.4&platform=paper", requests.getFirst().uri().toString());
        assertEquals("Test Agent", requests.getFirst().headers().firstValue("User-Agent").orElseThrow());
    }

    @Test
    void authorizationStaysOnApiOrigin() throws Exception {
        var updater = updater(new UpdaterConfig().apiKey("secret"));
        List<HttpRequest> requests = new ArrayList<>();
        updater.setSender(capturingSender(requests, List.of(302, 302, 302, 200), List.of(
            Map.of("Location", List.of("/same")), Map.of("Location", List.of("https://third.test/file")),
            Map.of("Location", List.of("https://example.test/return")), Map.of()), "ok"));
        updater.fetch(URI.create("https://example.test/start"));
        assertEquals(4, requests.size());
        assertEquals("Bearer secret", requests.get(0).headers().firstValue("Authorization").orElseThrow());
        assertEquals("Bearer secret", requests.get(1).headers().firstValue("Authorization").orElseThrow());
        assertTrue(requests.get(2).headers().firstValue("Authorization").isEmpty());
        assertTrue(requests.get(3).headers().firstValue("Authorization").isEmpty());
        requests.clear();
        updater.setSender(capturingSender(requests, List.of(200), List.of(Map.of()), "abc"));
        updater.download(new UpdateInfo("2", URI.create("https://third.test/file"), "plugin.jar", HASH, "SHA-256", false));
        assertEquals(1, requests.size());
        assertTrue(requests.getFirst().headers().firstValue("Authorization").isEmpty());
    }

    @Test
    void stalledResponseBodiesAreAborted() throws Exception {
        var server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        CountDownLatch release = new CountDownLatch(1);
        server.createContext("/stall", exchange -> {
            exchange.sendResponseHeaders(200, 100);
            exchange.getResponseBody().write('x');
            exchange.getResponseBody().flush();
            try { release.await(); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
            exchange.close();
        });
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool(r -> { var t = new Thread(r); t.setDaemon(true); return t; }));
        server.start();
        try {
            URI uri = URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/stall");
            var updater = updater(new UpdaterConfig());
            updater.setTimeouts(Duration.ofSeconds(1), Duration.ofSeconds(1));
            Path update = Files.createDirectories(dir.resolve("update"));
            Files.writeString(update.resolve("plugin.jar"), "existing");
            assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertThrows(IOException.class, () -> updater.fetch(uri)));
            assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertThrows(IOException.class, () -> updater.download(new UpdateInfo("2", uri, "plugin.jar", HASH, "SHA-256", false))));
            assertClean(update, "existing");
        } finally {
            release.countDown();
            server.stop(0);
        }
    }

    @Test
    void modrinthSelectsLatestAndDownloadsWithSha512WithoutCdnToken() throws Exception {
        var updater = new ModrinthUpdater(plugin(), new File("current.jar"), "some slug", "paper", "1.21.4", new UpdaterConfig().apiKey("secret"));
        String old = "{\"version_number\":\"1.5\",\"date_published\":\"2024-01-01T00:00:00Z\",\"files\":[]}";
        String latest = "{\"version_number\":\"2.0\",\"date_published\":\"2025-01-01T00:00:00Z\",\"files\":[{\"primary\":true,\"filename\":\"plugin.jar\",\"url\":\"https://cdn.modrinth.com/plugin.jar\",\"hashes\":{\"sha512\":\"" + HASH512 + "\"}}]}";
        List<HttpRequest> requests = new ArrayList<>();
        updater.setSender(capturingSender(requests, List.of(200, 200), List.of(Map.of(), Map.of()), List.of("[" + old + "," + latest + "]", "abc")));
        updater.run();
        assertEquals("abc", Files.readString(dir.resolve("update/plugin.jar")));
        assertEquals(2, requests.size());
        assertEquals("https://api.modrinth.com/v2/project/some%20slug/version?loaders=%5B%22paper%22%5D&game_versions=%5B%221.21.4%22%5D", requests.getFirst().uri().toString());
        assertEquals("Test/1.0 (GuizhanLib)", requests.getFirst().headers().firstValue("User-Agent").orElseThrow());
        assertEquals("Bearer secret", requests.getFirst().headers().firstValue("Authorization").orElseThrow());
        assertTrue(requests.get(1).headers().firstValue("Authorization").isEmpty());
    }

    @Test
    void modrinthRequiresUniquePrimaryAndValidChecksum() throws Exception {
        var updater = new ModrinthUpdater(plugin(), new File("current.jar"), "slug", "paper", "1.21", new UpdaterConfig());
        updater.setSender(sender(200, "[]"));
        assertTrue(updater.checkUpdate().isEmpty());
        String prefix = "[{\"version_number\":\"2.0\",\"date_published\":\"2025-01-01T00:00:00Z\",\"files\":";
        updater.setSender(sender(200, prefix + "[]}]"));
        assertTrue(updater.checkUpdate().isEmpty());
        updater.setSender(sender(200, prefix + "[{\"primary\":true},{\"primary\":true}]}]"));
        assertTrue(updater.checkUpdate().isEmpty());
        updater.setSender(sender(200, prefix + "[{\"primary\":true,\"filename\":\"plugin.jar\",\"url\":\"https://cdn.test/plugin.jar\"}]}]"));
        assertThrows(IllegalStateException.class, updater::checkUpdate);
        var checkOnly = new ModrinthUpdater(plugin(), new File("current.jar"), "slug", "paper", "1.21", new UpdaterConfig().checkOnly(true));
        checkOnly.setSender(sender(200, prefix + "[{\"primary\":true,\"filename\":\"plugin.jar\",\"url\":\"https://cdn.test/plugin.jar\"}]}]"));
        assertNull(checkOnly.checkUpdate().orElseThrow().checksum());
    }

    @Test
    void hangarTwoStepAndSha256Download() throws Exception {
        var updater = new HangarUpdater(plugin(), new File("current.jar"), "author", "slug", "Release", "PAPER", new UpdaterConfig());
        String list = "{\"result\":[{\"name\":\"2.0+build\"}]}";
        String details = "{\"downloads\":{\"PAPER\":{\"downloadUrl\":\"https://cdn.test/other\",\"fileInfo\":{\"name\":\"plugin.jar\",\"sha256Hash\":\"" + HASH + "\"}}}}";
        List<HttpRequest> requests = new ArrayList<>();
        updater.setSender(capturingSender(requests, List.of(200, 200, 200), List.of(Map.of(), Map.of(), Map.of()), List.of(list, details, "abc")));
        updater.run();
        assertEquals(3, requests.size());
        assertEquals("https://hangar.papermc.io/api/v1/projects/author/slug/versions?limit=1&platform=PAPER&channel=Release", requests.get(0).uri().toString());
        assertEquals("https://hangar.papermc.io/api/v1/projects/author/slug/versions/2.0%2Bbuild", requests.get(1).uri().toString());
        assertEquals("Test/1.0 (GuizhanLib)", requests.get(0).headers().firstValue("User-Agent").orElseThrow());
        assertEquals("Test/1.0 (GuizhanLib)", requests.get(1).headers().firstValue("User-Agent").orElseThrow());
        assertEquals("abc", Files.readString(dir.resolve("update/plugin.jar")));
    }

    @Test
    void hangarMissingPlatformAndExternalNotify() throws Exception {
        var updater = new HangarUpdater(plugin(), new File("current.jar"), "author", "slug", "Release", "PAPER", new UpdaterConfig());
        String list = "{\"result\":[{\"name\":\"2.0\"}]}";
        List<HttpRequest> requests = new ArrayList<>();
        updater.setSender(capturingSender(requests, List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, "{\"downloads\":{}}")));
        assertTrue(updater.checkUpdate().isEmpty());
        requests.clear();
        updater.setSender(capturingSender(requests, List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, "{\"downloads\":{\"PAPER\":{\"externalUrl\":\"https://example.test/release\",\"fileInfo\":null}}}")));
        updater.run();
        assertEquals(2, requests.size());
        assertFalse(Files.exists(dir.resolve("update")));
    }

    @Test
    void checkOnlyEmitsUpdateNotification() {
        var updater = new ModrinthUpdater(plugin(), new File("current.jar"), "slug", "paper", "1.21", new UpdaterConfig().checkOnly(true));
        List<HttpRequest> requests = new ArrayList<>();
        updater.setSender(capturingSender(requests, List.of(200), List.of(Map.of()), "[{\"version_number\":\"2.0\",\"date_published\":\"2025-01-01T00:00:00Z\",\"files\":[{\"primary\":true,\"filename\":\"plugin.jar\",\"url\":\"https://cdn.test/plugin.jar\"}]}]"));
        List<LogRecord> records = new ArrayList<>();
        Handler handler = new Handler() {
            public void publish(LogRecord record) { records.add(record); }
            public void flush() {}
            public void close() {}
        };
        LOGGER.addHandler(handler);
        try {
            updater.run();
            assertTrue(records.stream().anyMatch(record -> record.getLevel() == Level.INFO && record.getMessage().equals("Test has an update: 2.0")));
            assertTrue(records.stream().anyMatch(record -> record.getLevel() == Level.INFO && record.getMessage().equals("Download the update manually: https://cdn.test/plugin.jar")));
            assertEquals(1, requests.size());
        } finally {
            LOGGER.removeHandler(handler);
        }
    }

    @Test
    void notFoundIsWarnedOnceOnBothPlatforms() throws Exception {
        List<LogRecord> records = new ArrayList<>();
        Handler handler = logHandler(records);
        LOGGER.addHandler(handler);
        try {
            var modrinth = new ModrinthUpdater(plugin(), new File("current.jar"), "slug", "paper", "1.21", new UpdaterConfig());
            List<HttpRequest> requests = new ArrayList<>();
            modrinth.setSender(capturingSender(requests, List.of(404), List.of(Map.of()), ""));
            assertTrue(modrinth.checkUpdate().isEmpty());
            assertEquals(1, requests.size());
            assertEquals(1, records.stream().filter(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("404")).count());

            var hangar = new HangarUpdater(plugin(), new File("current.jar"), "author", "slug", "Release", "PAPER", new UpdaterConfig());
            requests.clear(); records.clear();
            hangar.setSender(capturingSender(requests, List.of(404), List.of(Map.of()), ""));
            assertTrue(hangar.checkUpdate().isEmpty());
            assertEquals(1, requests.size());
            assertEquals(1, records.stream().filter(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("404")).count());

            requests.clear(); records.clear();
            hangar.setSender(capturingSender(requests, List.of(200, 404), List.of(Map.of(), Map.of()), List.of("{\"result\":[{\"name\":\"2.0\"}]}", "")));
            assertTrue(hangar.checkUpdate().isEmpty());
            assertEquals(2, requests.size());
            assertEquals(1, records.stream().filter(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("404")).count());
        } finally {
            LOGGER.removeHandler(handler);
        }
    }

    @Test
    void rateLimitHintsAreLoggedWithoutRetries() throws Exception {
        List<LogRecord> records = new ArrayList<>();
        Handler handler = logHandler(records);
        LOGGER.addHandler(handler);
        try {
            var modrinth = new ModrinthUpdater(plugin(), new File("current.jar"), "slug", "paper", "1.21", new UpdaterConfig());
            List<HttpRequest> requests = new ArrayList<>();
            modrinth.setSender(capturingSender(requests, List.of(429), List.of(Map.of("X-Ratelimit-Reset", List.of("42"))), ""));
            assertTrue(modrinth.checkUpdate().isEmpty());
            assertEquals(1, requests.size());
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("X-Ratelimit-Reset: 42 seconds")));
            records.clear(); requests.clear();
            var hangar = new HangarUpdater(plugin(), new File("current.jar"), "author", "slug", "Release", "PAPER", new UpdaterConfig());
            hangar.setSender(capturingSender(requests, List.of(429), List.of(Map.of("Retry-After", List.of("17"), "X-Ratelimit-Reset", List.of("99"))), ""));
            assertTrue(hangar.checkUpdate().isEmpty());
            assertEquals(1, requests.size());
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("Retry-After: 17") && r.getMessage().contains("X-Ratelimit-Reset: 99")));
            records.clear(); requests.clear();
            hangar.setSender(capturingSender(requests, List.of(200, 429), List.of(Map.of(), Map.of("Retry-After", List.of("18"))), List.of("{\"result\":[{\"name\":\"2.0\"}]}", "")));
            assertTrue(hangar.checkUpdate().isEmpty());
            assertEquals(2, requests.size());
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("Retry-After: 18")));
        } finally {
            LOGGER.removeHandler(handler);
        }
    }

    @Test
    void hangarChecksumAndFilenameRules() throws Exception {
        var updater = new HangarUpdater(plugin(), new File("current.jar"), "author", "slug", "Release", "PAPER", new UpdaterConfig());
        String list = "{\"result\":[{\"name\":\"2.0\"}]}";
        String prefix = "{\"downloads\":{\"PAPER\":{\"downloadUrl\":\"https://cdn.test/tail.jar\",\"fileInfo\":";
        for (String info : List.of("{\"name\":\"plugin.jar\"}", "{\"name\":\"plugin.jar\",\"sha256Hash\":\"bad\"}")) {
            updater.setSender(capturingSender(new ArrayList<>(), List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, prefix + info + "}}}")));
            assertThrows(IllegalStateException.class, updater::checkUpdate);
        }
        var checkOnly = new HangarUpdater(plugin(), new File("current.jar"), "author", "slug", "Release", "PAPER", new UpdaterConfig().checkOnly(true));
        checkOnly.setSender(capturingSender(new ArrayList<>(), List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, prefix + "{\"name\":\"plugin.jar\"}}}}")));
        assertNull(checkOnly.checkUpdate().orElseThrow().checksum());
        checkOnly.setSender(capturingSender(new ArrayList<>(), List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, prefix + "{\"name\":\"plugin.jar\",\"sha256Hash\":\"bad\"}}}}")));
        assertEquals("bad", checkOnly.checkUpdate().orElseThrow().checksum());
        updater.setSender(capturingSender(new ArrayList<>(), List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, prefix + "{\"name\":\"chosen.jar\",\"sha256Hash\":\"" + HASH + "\"}}}}")));
        assertEquals("chosen.jar", updater.checkUpdate().orElseThrow().fileName());
        updater.setSender(capturingSender(new ArrayList<>(), List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, prefix + "{\"sha256Hash\":\"" + HASH + "\"}}}}")));
        assertEquals("tail.jar", updater.checkUpdate().orElseThrow().fileName());
        updater.setSender(capturingSender(new ArrayList<>(), List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, prefix + "{\"name\":\"../evil.jar\",\"sha256Hash\":\"" + HASH + "\"}}}}")));
        assertThrows(IllegalStateException.class, updater::checkUpdate);
        String traversal = "{\"downloads\":{\"PAPER\":{\"downloadUrl\":\"https://cdn.test/..%2Fevil.jar\",\"fileInfo\":{\"sha256Hash\":\"" + HASH + "\"}}}}";
        updater.setSender(capturingSender(new ArrayList<>(), List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, traversal)));
        assertThrows(IllegalStateException.class, updater::checkUpdate);
        for (var entry : Map.of("plus+name.jar", "plus+name.jar", "plus%2Bname.jar", "plus+name.jar", "space%20name.jar", "space name.jar").entrySet()) {
            String details = "{\"downloads\":{\"PAPER\":{\"downloadUrl\":\"https://cdn.test/" + entry.getKey() + "\",\"fileInfo\":{\"sha256Hash\":\"" + HASH + "\"}}}}";
            updater.setSender(capturingSender(new ArrayList<>(), List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, details)));
            assertEquals(entry.getValue(), updater.checkUpdate().orElseThrow().fileName());
        }
        for (String unsafe : List.of("bad%5Cname.jar", "bad%00name.jar")) {
            String details = "{\"downloads\":{\"PAPER\":{\"downloadUrl\":\"https://cdn.test/" + unsafe + "\",\"fileInfo\":{\"sha256Hash\":\"" + HASH + "\"}}}}";
            updater.setSender(capturingSender(new ArrayList<>(), List.of(200, 200), List.of(Map.of(), Map.of()), List.of(list, details)));
            assertThrows(IllegalStateException.class, updater::checkUpdate);
        }
    }

    @Test
    void githubAssetSelectionAndDigests() throws Exception {
        var updater = new GitHubReleaseUpdater(plugin(), new File("current.jar"), "owner", "repo", "plugin.jar", new UpdaterConfig());
        String asset = githubAsset("plugin.jar", "https://cdn.test/plugin.jar", "sha256:" + HASH);
        String other = githubAsset("other.zip", "https://cdn.test/other.zip", null);
        updater.setSender(sender(200, githubRelease(asset + "," + other)));
        var info = updater.checkUpdate().orElseThrow();
        assertEquals("plugin.jar", info.fileName());
        assertEquals(HASH, info.checksum());
        assertEquals("SHA-256", info.checksumAlgorithm());
        updater.setSender(sender(200, githubRelease(asset).replace("\"tag_name\":\"2.0\"", "\"tag_name\":\"v2.0\"")));
        assertEquals("v2.0", updater.checkUpdate().orElseThrow().version());
        updater.setSender(sender(200, githubRelease(other)));
        assertTrue(updater.checkUpdate().isEmpty());
        updater.setSender(sender(200, githubRelease(asset + "," + githubAsset("another-plugin.jar", "https://cdn.test/another.jar", "sha256:" + HASH))));
        assertTrue(updater.checkUpdate().isEmpty());
        var unfiltered = new GitHubReleaseUpdater(plugin(), new File("current.jar"), "owner", "repo", null, new UpdaterConfig());
        unfiltered.setSender(sender(200, githubRelease(asset)));
        assertEquals("plugin.jar", unfiltered.checkUpdate().orElseThrow().fileName());
        unfiltered.setSender(sender(200, githubRelease(asset + "," + other)));
        assertTrue(unfiltered.checkUpdate().isEmpty());
        for (String invalid : List.of("", "bad", "sha512:" + HASH, "sha256:bad")) {
            String response = githubRelease(githubAsset("plugin.jar", "https://cdn.test/plugin.jar", invalid.isEmpty() ? null : invalid));
            updater.setSender(sender(200, response));
            assertThrows(IllegalStateException.class, updater::checkUpdate);
            var checkOnly = new GitHubReleaseUpdater(plugin(), new File("current.jar"), "owner", "repo", null, new UpdaterConfig().checkOnly(true));
            List<HttpRequest> requests = new ArrayList<>();
            checkOnly.setSender(capturingSender(requests, List.of(200), List.of(Map.of()), response));
            List<LogRecord> records = new ArrayList<>();
            Handler handler = logHandler(records);
            LOGGER.addHandler(handler);
            try {
                checkOnly.run();
                assertEquals(1, requests.size());
                assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.INFO && r.getMessage().equals("Download the update manually: https://cdn.test/plugin.jar")));
            } finally {
                LOGGER.removeHandler(handler);
            }
        }
    }

    @Test
    void githubInstallsVerifiedAssetAndDoesNotForwardToken() throws Exception {
        var updater = new GitHubReleaseUpdater(plugin(), new File("current.jar"), "my owner", "my repo", null, new UpdaterConfig().apiKey("secret"));
        String asset = githubAsset("plugin.jar", "https://api.github.com/assets/1", "sha256:" + HASH);
        List<HttpRequest> requests = new ArrayList<>();
        updater.setSender(capturingSender(requests, List.of(200, 302, 200), List.of(Map.of(), Map.of("Location", List.of("https://objects.githubusercontent.com/plugin.jar")), Map.of()), List.of(githubRelease(asset), "", "abc")));
        updater.run();
        assertEquals("abc", Files.readString(dir.resolve("update/plugin.jar")));
        assertEquals(3, requests.size());
        assertEquals("https://api.github.com/repos/my%20owner/my%20repo/releases/latest", requests.get(0).uri().toString());
        assertEquals("Test/1.0 (GuizhanLib)", requests.get(0).headers().firstValue("User-Agent").orElseThrow());
        assertEquals("Bearer secret", requests.get(0).headers().firstValue("Authorization").orElseThrow());
        assertEquals("Bearer secret", requests.get(1).headers().firstValue("Authorization").orElseThrow());
        assertTrue(requests.get(2).headers().firstValue("Authorization").isEmpty());
        requests.clear();
        updater.setSender(capturingSender(requests, List.of(200, 200), List.of(Map.of(), Map.of()), List.of(githubRelease(githubAsset("plugin.jar", "https://third.test/plugin.jar", "sha256:" + HASH)), "abc")));
        updater.run();
        assertEquals(2, requests.size());
        assertTrue(requests.get(1).headers().firstValue("Authorization").isEmpty());
    }

    @Test
    void githubNotFoundAndRateLimitWarnings() throws Exception {
        var updater = new GitHubReleaseUpdater(plugin(), new File("current.jar"), "owner", "repo", null, new UpdaterConfig());
        List<HttpRequest> requests = new ArrayList<>();
        List<LogRecord> records = new ArrayList<>();
        Handler handler = logHandler(records);
        LOGGER.addHandler(handler);
        try {
            updater.setSender(capturingSender(requests, List.of(404), List.of(Map.of()), ""));
            assertTrue(updater.checkUpdate().isEmpty());
            assertEquals(1, requests.size());
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("404")));
            requests.clear(); records.clear();
            updater.setSender(capturingSender(requests, List.of(403), List.of(Map.of("X-RateLimit-Remaining", List.of("0"), "X-RateLimit-Reset", List.of("1700000000"))), ""));
            assertTrue(updater.checkUpdate().isEmpty());
            assertEquals(1, requests.size());
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("2023-11-14T22:13:20Z")));
            requests.clear(); records.clear();
            updater.setSender(capturingSender(requests, List.of(429), List.of(Map.of("Retry-After", List.of("30"))), ""));
            assertTrue(updater.checkUpdate().isEmpty());
            assertEquals(1, requests.size());
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("Retry-After: 30")));
        } finally {
            LOGGER.removeHandler(handler);
        }
    }

    @Test
    void githubSecondaryRateLimitsAndPlainForbidden() throws Exception {
        var updater = new GitHubReleaseUpdater(plugin(), new File("current.jar"), "owner", "repo", null, new UpdaterConfig());
        List<HttpRequest> requests = new ArrayList<>();
        List<LogRecord> records = new ArrayList<>();
        Handler handler = logHandler(records);
        LOGGER.addHandler(handler);
        try {
            updater.setSender(capturingSender(requests, List.of(403), List.of(Map.of("X-RateLimit-Remaining", List.of("12"), "Retry-After", List.of("45"))), ""));
            assertTrue(updater.checkUpdate().isEmpty());
            assertEquals(1, requests.size());
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("Retry-After: 45")));
            for (String message : List.of("You have exceeded a secondary rate limit", "Abuse detection mechanism")) {
                requests.clear(); records.clear();
                updater.setSender(capturingSender(requests, List.of(403), List.of(Map.of("X-RateLimit-Remaining", List.of("12"))), "{\"message\":\"" + message + "\"}"));
                assertTrue(updater.checkUpdate().isEmpty());
                assertEquals(1, requests.size());
                assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("rate limit")));
            }
            requests.clear(); records.clear();
            updater.setSender(capturingSender(requests, List.of(429), List.of(Map.of("X-RateLimit-Reset", List.of("invalid-epoch"))), ""));
            assertTrue(updater.checkUpdate().isEmpty());
            assertEquals(1, requests.size());
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.WARNING && r.getMessage().contains("X-RateLimit-Reset: invalid-epoch")));
            for (String body : List.of("not JSON", "{\"message\":null}", "{\"message\":\"Forbidden\"}")) {
                requests.clear(); records.clear();
                updater.setSender(capturingSender(requests, List.of(403), List.of(Map.of()), body));
                assertThrows(IllegalStateException.class, updater::checkUpdate);
                assertEquals(1, requests.size());
                requests.clear();
                updater.run();
                assertEquals(1, requests.size());
                assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.SEVERE && r.getMessage().contains("GitHub HTTP 403")));
            }
        } finally {
            LOGGER.removeHandler(handler);
        }
    }

    private static String githubAsset(String name, String url, String digest) {
        return "{\"name\":\"" + name + "\",\"browser_download_url\":\"" + url + "\",\"digest\":" + (digest == null ? "null" : "\"" + digest + "\"") + "}";
    }

    private static String githubRelease(String assets) {
        return "{\"tag_name\":\"2.0\",\"assets\":[" + assets + "]}";
    }

    @Test
    void modrinthDateTieKeepsFirstAndInvalidDateLogsSevere() throws Exception {
        var updater = new ModrinthUpdater(plugin(), new File("current.jar"), "slug", "paper", "1.21", new UpdaterConfig().checkOnly(true));
        String first = "{\"version_number\":\"2.0\",\"date_published\":\"2025-01-01T00:00:00Z\",\"files\":[{\"primary\":true,\"filename\":\"plugin.jar\",\"url\":\"https://cdn.test/plugin.jar\"}]}";
        String second = first.replace("2.0", "3.0");
        updater.setSender(sender(200, "[" + first + "," + second + "]"));
        assertEquals("2.0", updater.checkUpdate().orElseThrow().version());
        updater.setSender(sender(200, "[" + first.replace("2025-01-01T00:00:00Z", "invalid") + "]"));
        List<LogRecord> records = new ArrayList<>();
        Handler handler = logHandler(records);
        LOGGER.addHandler(handler);
        try {
            updater.run();
            assertTrue(records.stream().anyMatch(r -> r.getLevel() == Level.SEVERE && r.getMessage().contains("Update check or installation failed")));
        } finally {
            LOGGER.removeHandler(handler);
        }
    }

    private static Handler logHandler(List<LogRecord> records) {
        return new Handler() {
            public void publish(LogRecord record) { records.add(record); }
            public void flush() {}
            public void close() {}
        };
    }

    private AbstractUpdater updater(UpdaterConfig config) {
        return new AbstractUpdater(plugin(), new File("current.jar"), config, URI.create("https://example.test")) {
            @Override protected Optional<UpdateInfo> checkUpdate() { return Optional.empty(); }
        };
    }

    private void assertClean(Path update, String expected) throws IOException {
        assertEquals(expected, Files.readString(update.resolve("plugin.jar")));
        try (var files = Files.list(update)) { assertEquals(1, files.count()); }
        try (var files = Files.list(dir.resolve("data"))) { assertEquals(0, files.count()); }
    }

    private Plugin plugin() {
        PluginMeta meta = proxy(PluginMeta.class, Map.of("getVersion", "1.0"));
        Server server = proxy(Server.class, Map.of("getUpdateFolderFile", dir.resolve("update").toFile()));
        return proxy(Plugin.class, Map.of("getName", "Test", "getPluginMeta", meta, "getServer", server, "getDataFolder", dir.resolve("data").toFile(), "getLogger", LOGGER));
    }

    private static <T> T proxy(Class<T> type, Map<String, Object> values) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (p, method, args) -> values.get(method.getName())));
    }

    private static AbstractUpdater.Sender sender(int status, String body) {
        return capturingSender(new ArrayList<>(), List.of(status), List.of(Map.of()), body);
    }

    private static AbstractUpdater.Sender capturingSender(List<HttpRequest> requests, List<Integer> statuses, List<Map<String, List<String>>> responseHeaders, String body) {
        return capturingSender(requests, statuses, responseHeaders, java.util.Collections.nCopies(statuses.size(), body));
    }

    private static AbstractUpdater.Sender capturingSender(List<HttpRequest> requests, List<Integer> statuses, List<Map<String, List<String>>> responseHeaders, List<String> bodies) {
        return new AbstractUpdater.Sender() {
            @Override public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> handler) throws IOException {
                int index = requests.size();
                requests.add(request);
                int status = statuses.get(index);
                HttpHeaders headers = HttpHeaders.of(responseHeaders.get(index), (a, b) -> true);
                HttpResponse.ResponseInfo info = new HttpResponse.ResponseInfo() {
                    public int statusCode() { return status; }
                    public HttpHeaders headers() { return headers; }
                    public HttpClient.Version version() { return HttpClient.Version.HTTP_1_1; }
                };
                var subscriber = handler.apply(info);
                subscriber.onSubscribe(new Flow.Subscription() { public void request(long n) {} public void cancel() {} });
                subscriber.onNext(List.of(ByteBuffer.wrap(bodies.get(index).getBytes(java.nio.charset.StandardCharsets.UTF_8))));
                subscriber.onComplete();
                T result = subscriber.getBody().toCompletableFuture().join();
                return new HttpResponse<>() {
                    public int statusCode() { return status; }
                    public HttpRequest request() { return request; }
                    public Optional<HttpResponse<T>> previousResponse() { return Optional.empty(); }
                    public HttpHeaders headers() { return headers; }
                    public T body() { return result; }
                    public Optional<javax.net.ssl.SSLSession> sslSession() { return Optional.empty(); }
                    public URI uri() { return request.uri(); }
                    public HttpClient.Version version() { return HttpClient.Version.HTTP_1_1; }
                };
            }
        };
    }
}
