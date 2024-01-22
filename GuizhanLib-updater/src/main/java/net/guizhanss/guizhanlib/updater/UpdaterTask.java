package net.guizhanss.guizhanlib.updater;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;
import net.guizhanss.guizhanlib.utils.JsonUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Auto update task.
 *
 * @author ybw0014
 */
class UpdaterTask implements Runnable {
    @Getter(AccessLevel.NONE)
    private static boolean debug = false;

    private final AbstractGuizhanBuildsUpdater updater;

    private String workingDirectory;
    private JsonObject repoInfo = null;
    private JsonObject updateInfo = null;

    UpdaterTask(AbstractGuizhanBuildsUpdater updater) {
        this.updater = updater;
    }

    @Override
    public void run() {
        getRepoInfo();

        if (updater.getConfig().checkVersionFormat()) {
            String format = getVersionFormat();
            if (format == null) {
                updater.log(Level.SEVERE, LocaleKey.INVALID_VERSION);
                return;
            }
            if (!checkVersion(format)) {
                updater.log(Level.WARNING, LocaleKey.INVALID_FILE_VERSION);
                return;
            }
        }

        if (hasUpdate()) {
            if (updater.getConfig().checkOnly()) {
                sendUpdateNotification();
            } else {
                update();
            }
        }
    }

    /**
     * Get repository information.
     */
    private void getRepoInfo() {
        try {
            URL reposUrl = new URL(updater.getReposFileURL());
            String repos = fetch(reposUrl);
            if (repos == null) {
                throw new IllegalStateException("Repository list is null");
            }
            JsonObject reposJson = (JsonObject) JsonUtil.parse(repos);

            String key = updater.getRepoKey();
            // direct find
            JsonElement currentRepoInfo = JsonUtil.getFromPath(reposJson, key);

            if (currentRepoInfo == null) {
                // find by alias
                for (Map.Entry<String, JsonElement> repoEntry : reposJson.entrySet()) {
                    JsonObject repoObj = repoEntry.getValue().getAsJsonObject();
                    if (!repoObj.has("alias")) {
                        continue;
                    }
                    for (JsonElement alias : repoObj.get("alias").getAsJsonArray()) {
                        String aliasPrimitive = alias.getAsJsonPrimitive().getAsString();
                        if (aliasPrimitive.equals(key)) {
                            currentRepoInfo = repoObj;
                            key = repoEntry.getKey();
                            break;
                        }
                    }
                    if (currentRepoInfo != null) {
                        break;
                    }
                }
            }

            if (currentRepoInfo == null) {
                throw new IllegalStateException("Repository information is not found");
            }

            repoInfo = (JsonObject) currentRepoInfo;

            // Get working directory
            workingDirectory = key.replace(":", "/");
        } catch (MalformedURLException | IllegalStateException | IllegalArgumentException | NullPointerException ex) {
            updater.log(Level.SEVERE, LocaleKey.CANNOT_FIND_REPO);
            if (debug) {
                updater.log(Level.SEVERE, ex, ex.getMessage());
            }
        }
    }

    /**
     * Get the version format.
     *
     * @return the version format, {@code null} if failed
     */
    @Nullable
    private String getVersionFormat() {
        try {
            return JsonUtil.getFromPath(repoInfo, "buildOptions.version").getAsString();
        } catch (IllegalStateException | IllegalArgumentException | NullPointerException ex) {
            if (debug) {
                updater.log(Level.SEVERE, ex, ex.getMessage());
            }
            return null;
        }
    }

    /**
     * Check if version format matches.
     *
     * @param format The version format.
     * @return Whether the version format matches.
     */
    private boolean checkVersion(String format) {
        String regex = format.replace("(", "\\(")
            .replace(")", "\\)")
            .replace("{version}", "\\d{1,6}")
            .replace("{git_commit}", "([a-z0-9]{7})")
            .replace("{Year}", "\\d{4}")
            .replace("{year}", "\\d{2}")
            .replace("{Month}", "\\d{2}")
            .replace("{month}", "\\d{1,2}")
            .replace("{Date}", "\\d{2}")
            .replace("{date}", "\\d{1,2}");
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(updater.getPlugin().getDescription().getVersion());
        return m.matches();
    }

    /**
     * Check if there is new build.
     *
     * @return Whether new build exists.
     */
    private boolean hasUpdate() {
        try {
            // Retrieve builds info
            URL buildsUrl = new URL(updater.getBuildsInfo(workingDirectory));
            JsonObject buildsJson = (JsonObject) JsonUtil.parse(fetch(buildsUrl));
            JsonArray builds = (JsonArray) JsonUtil.getFromPath(buildsJson, "builds");

            JsonObject build = null;
            for (int i = builds.size() - 1; i >= 0; i--) {
                build = (JsonObject) builds.get(i);
                if (build.get("success").getAsBoolean()) break;
                build = null;
            }
            if (build == null) {
                updater.log(Level.SEVERE, LocaleKey.CANNOT_FIND_BUILDS);
                return false;
            }

            // compare the checksum
            String currentChecksum = updater.getChecksum();
            if (currentChecksum == null) {
                return false;
            }

            String checksum = build.get("sha1").getAsString();
            if (currentChecksum.equals(checksum)) {
                updater.log(Level.INFO, LocaleKey.UP_TO_DATE, updater.getPlugin().getName());
                return false;
            }
            updateInfo = build;
            return true;
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException | IOException ex) {
            updater.log(Level.SEVERE, LocaleKey.CANNOT_FETCH_INFO);
            if (debug) {
                updater.log(Level.SEVERE, ex, ex.getMessage());
            }
            return false;
        }
    }

    /**
     * Send update notification.
     */
    private void sendUpdateNotification() {
        updater.log(Level.INFO, LocaleKey.NEED_UPDATE, updater.getPlugin().getName());
        updater.log(Level.INFO, LocaleKey.DOWNLOAD_NOTIFICATION, updater.getPlugin().getName());
    }

    /**
     * Download and install the latest version
     */
    private void update() {
        String targetFilename = updateInfo.get("target").getAsString();
        updater.log(Level.INFO, LocaleKey.NEED_UPDATE, updater.getPlugin().getName());
        updater.log(Level.INFO, LocaleKey.DOWNLOADING, updater.getPlugin().getName(), updateInfo.get("id").getAsString());

        try {
            BufferedInputStream input = new BufferedInputStream(new URL(updater.getTargetUrl(workingDirectory, targetFilename)).openStream());
            FileOutputStream output = new FileOutputStream(new File("plugins/" + updater.getPlugin().getServer().getUpdateFolder(), updater.getFile().getName()));
            byte[] data = new byte[1024];
            int read;

            while ((read = input.read(data, 0, 1024)) != -1) {
                output.write(data, 0, read);
            }

            input.close();
            output.close();
        } catch (Exception ex) {
            updater.log(Level.SEVERE, LocaleKey.DOWNLOAD_FAIL, updater.getPlugin().getName());
            if (debug) {
                updater.log(Level.SEVERE, ex, ex.getMessage());
            }
            return;
        }

        updater.log(Level.INFO, " ");
        updater.log(Level.INFO, LocaleKey.UPDATE_INFO_0);
        updater.log(Level.INFO, LocaleKey.UPDATE_INFO_1, updater.getPlugin().getName(), updateInfo.get("id").getAsString());
        updater.log(Level.INFO, LocaleKey.UPDATE_INFO_2);
        updater.log(Level.INFO, " ");
    }

    /**
     * Fetch information from {@link URL}.
     *
     * @param url The {@link URL} of resource.
     * @return The content {@link String}.
     */
    @Nullable
    private String fetch(@Nonnull URL url) {
        try {
            StringBuilder content = new StringBuilder();

            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10_000);
            connection.addRequestProperty("User-Agent", "Guizhan Updater");
            connection.setDoOutput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line = reader.readLine();
            while (line != null) {
                content.append(line);
                line = reader.readLine();
            }

            return content.toString();
        } catch (IOException | NullPointerException ex) {
            updater.log(Level.WARNING, LocaleKey.CANNOT_FETCH_INFO);
            if (debug) {
                updater.log(Level.SEVERE, ex, ex.getMessage());
            }
            return null;
        }
    }
}
