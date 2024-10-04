package net.guizhanss.guizhanlib.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;

/**
 * Auto update task.
 *
 * @author ybw0014
 */
class GuizhanBuildsUpdaterTask implements Runnable {

    private final GuizhanBuildsUpdater updater;
    private final HttpClient httpClient;
    private final boolean isDebug;

    private JsonObject projectInfo = null;
    private JsonObject buildInfo = null;

    GuizhanBuildsUpdaterTask(@Nonnull GuizhanBuildsUpdater updater) {
        this.updater = updater;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.isDebug = Boolean.parseBoolean(System.getProperty("guizhanlib.updater.debug"));
    }

    @Override
    public void run() {
        getProjectInfo();

        if (hasUpdate()) {
            if (updater.getUpdaterConfig().checkOnly()) {
                sendUpdateNotification();
            } else {
                update();
            }
        }
    }

    /**
     * Get project information.
     */
    private void getProjectInfo() {
        try {
            URI baseUrl = new URI(updater.getUpdaterConfig().baseUrl());
            if (!isValidHost(baseUrl)) {
                throw new IllegalArgumentException("Invalid base URL provided. Only provide the host.");
            }
            URI projectUrl = baseUrl.resolve("/api/project/" + updater.getOwner() + "/" + updater.getRepository() + "/" + updater.getBranch());
            JsonObject projectsResp = fetch(projectUrl);
            if (projectsResp == null) {
                throw new IllegalStateException("Cannot get project information");
            }

            projectInfo = projectsResp.get("data").getAsJsonObject();
        } catch (Exception ex) {
            updater.log(Level.SEVERE, "An error has occurred while fetching project info.");
            if (isDebug) {
                updater.log(Level.SEVERE, ex, ex.getMessage());
            }
        }
    }

    private boolean isValidHost(@Nonnull URI uri) {
        if (uri.getScheme() == null || (!uri.getScheme().equals("http") && !uri.getScheme().equals("https"))) {
            return false;
        }
        return uri.getPath().isEmpty() || uri.getPath().equals("/");
    }

    /**
     * Check if there is new build.
     *
     * @return Whether new build exists.
     */
    private boolean hasUpdate() {
        try {
            // Retrieve latest successful build
            URI baseUrl = new URI(updater.getUpdaterConfig().baseUrl());
            if (!isValidHost(baseUrl)) {
                throw new IllegalArgumentException("Invalid base URL provided. Only provide the host.");
            }
            URI buildUrl = baseUrl.resolve("/api/build/" + updater.getOwner() + "/" + updater.getRepository() + "/" + updater.getBranch() + "/latest?status=success");
            JsonObject buildResp = fetch(buildUrl);
            if (buildResp == null) {
                throw new IllegalStateException("Cannot get latest successful build");
            }

            buildInfo = buildResp.get("data").getAsJsonObject();

            // check if there is update
            var pluginVersion = updater.getPlugin().getDescription().getVersion();
            var target = updater.getPlugin().getName() + "-" + pluginVersion + ".jar";
            return !target.equals(buildInfo.get("target").getAsString());
        } catch (Exception ex) {
            updater.log(Level.SEVERE, "An error has occurred while fetching the latest build.");
            if (isDebug) {
                updater.log(Level.SEVERE, ex, ex.getMessage());
            }
            return false;
        }
    }

    /**
     * Send update notification.
     */
    private void sendUpdateNotification() {
        updater.log(Level.INFO, "%s needs to be updated!", updater.getPlugin().getName());
        updater.log(Level.INFO, "Downloading is disabled, you have to download the new version manually!");
    }

    /**
     * Download and install the latest version
     */
    private void update() {
        updater.log(Level.INFO, "%s needs to be updated!", updater.getPlugin().getName());
        updater.log(Level.INFO, "Downloading %s - Build #%d", updater.getPlugin().getName(), buildInfo.get("id").getAsString());

        try {
            URI baseUrl = new URI(updater.getUpdaterConfig().baseUrl());
            if (!isValidHost(baseUrl)) {
                throw new IllegalArgumentException("Invalid base URL provided. Only provide the host.");
            }
            URI downloadUrl = baseUrl.resolve("/api/download/" + updater.getOwner() + "/" + updater.getRepository() + "/" + updater.getBranch() + "/" + buildInfo.get("id").getAsInt());

            HttpRequest request = buildRequest(downloadUrl);
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                throw new IllegalArgumentException("Cannot download build artifact.");
            }

            InputStream inputStream = response.body();
            FileOutputStream outputStream = new FileOutputStream(new File("plugins/" + updater.getPlugin().getServer().getUpdateFolder(), updater.getFile().getName()));

            byte[] buffer = new byte[4096]; // Buffer for data
            int bytesRead;

            // Read and write in chunks
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception ex) {
            updater.log(Level.SEVERE, "An error has occurred while downloading %s.", updater.getPlugin().getName());
            if (isDebug) {
                updater.log(Level.SEVERE, ex, ex.getMessage());
            }
            return;
        }

        updater.log(Level.INFO, " ");
        updater.log(Level.INFO, "========== Guizhan Auto Update ==========");
        updater.log(Level.INFO, "Successfully downloaded %s - Build #%d", updater.getPlugin().getName(), buildInfo.get("id").getAsString());
        updater.log(Level.INFO, "Restart the server to install the update");
        updater.log(Level.INFO, " ");
    }

    private HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder()
            .uri(uri)
            .header("Accept", "application/json")
            .header("User-Agent", "Guizhan Updater")
            .GET()
            .build();
    }

    /**
     * Fetch information from {@link URI}.
     *
     * @param uri The {@link URI} of resource.
     * @return The response {@link JsonObject}.
     */
    @Nullable
    private JsonObject fetch(@Nonnull URI uri) {
        HttpRequest request = buildRequest(uri);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return null;
            }

            return JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (Exception ex) {
            updater.log(Level.SEVERE, "An error has occurred while fetching.");
            if (isDebug) {
                updater.log(Level.SEVERE, ex, ex.getMessage());
            }
            return null;
        }
    }
}
