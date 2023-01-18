package net.guizhanss.guizhanlib.updater;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.guizhanss.guizhanlib.utils.JsonUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Auto update task.
 *
 * @author ybw0014
 */
class UpdaterTask implements Runnable {

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

        String format = getVersionFormat();
        if (format == null) {
            updater.log(Level.SEVERE, Locales.INVALID_VERSION);
            return;
        }
        if (!checkVersion(format)) {
            updater.log(Level.WARNING, Locales.INVALID_FILE_VERSION);
            return;
        }
        if (hasUpdate()) {
            if (updater.checkOnly()) {
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
            URL repos = new URL(updater.getReposFileURL());
            JsonObject reposJson = (JsonObject) JsonUtil.parse(fetch(repos));

            String key = updater.getRepoKey();
            JsonElement currentRepoInfo = null;
            while (key != null) {
                currentRepoInfo = JsonUtil.getFromPath(reposJson, key);

                if (currentRepoInfo == null) {
                    break;
                }

                if (JsonUtil.getFromPath((JsonObject) currentRepoInfo, "type").getAsString().equals("redirect")) {
                    key = JsonUtil.getFromPath((JsonObject) currentRepoInfo, "options.repo").getAsString();
                } else {
                    key = null;
                }
            }

            if (currentRepoInfo == null) {
                throw new IllegalStateException("Repository information is not found");
            }

            this.repoInfo = (JsonObject) currentRepoInfo;

            // Get working directory
            JsonElement customDir = JsonUtil.getFromPath(this.repoInfo, "options.customDir");
            if (customDir != null) {
                this.workingDirectory = customDir.getAsString();
            } else {
                this.workingDirectory = MessageFormat.format(
                    "{0}/{1}/{2}",
                    updater.getUser(),
                    updater.getRepo(),
                    updater.getBranch()
                );
            }
        } catch (MalformedURLException | IllegalStateException | IllegalArgumentException | NullPointerException ex) {
            updater.log(Level.SEVERE, Locales.CANNOT_FIND_REPO);
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
            return JsonUtil.getFromPath(repoInfo, "options.target.version").getAsString();
        } catch (IllegalStateException | IllegalArgumentException | NullPointerException ex) {
            return null;
        }
    }

    /**
     * Check if version format matches.
     *
     * @param format The version format.
     *
     * @return Whether the version format matches.
     */
    private boolean checkVersion(String format) {
        String regex = format.replace("(", "\\(")
            .replace(")", "\\)")
            .replace("{version}", "\\d{1,6}")
            .replace("{git_commit}", "([a-z0-9]{7})")
            .replace("{year}", "\\d{1,4}")
            .replace("{month}", "\\d{1,2}")
            .replace("{day}", "\\d{1,2}");
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
                updater.log(Level.SEVERE, Locales.CANNOT_FIND_BUILDS);
                return false;
            }

            String pluginName = JsonUtil.getFromPath(this.repoInfo, "options.target.name").getAsString();
            boolean needUpdate = !MessageFormat.format("{0}-{1}.jar", pluginName, updater.getPlugin().getDescription().getVersion()).equals(build.get("target").getAsString());
            if (!needUpdate) {
                updater.log(Level.INFO, Locales.UP_TO_DATE, updater.getPlugin().getName());
                return false;
            }
            updateInfo = build;
            return true;
        } catch (MalformedURLException | IllegalArgumentException | IllegalStateException | NullPointerException ex) {
            updater.log(Level.SEVERE, Locales.CANNOT_FETCH_INFO);
            return false;
        }
    }

    /**
     * Send update notification.
     */
    private void sendUpdateNotification() {
        updater.log(Level.INFO, Locales.NEED_UPDATE, updater.getPlugin().getName());
        updater.log(Level.INFO, Locales.DOWNLOAD_NOTIFICATION, updater.getPlugin().getName());
    }

    /**
     * Download and install the latest version
     */
    private void update() {
        String targetFilename = updateInfo.get("target").getAsString();
        updater.log(Level.INFO, Locales.NEED_UPDATE, updater.getPlugin().getName());
        updater.log(Level.INFO, Locales.DOWNLOADING, updater.getPlugin().getName(), updateInfo.get("id").getAsString());

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
            updater.log(Level.SEVERE, Locales.DOWNLOAD_FAIL, updater.getPlugin().getName());
            return;
        }

        updater.log(Level.INFO, " ");
        updater.log(Level.INFO, Locales.UPDATE_INFO_0);
        updater.log(Level.INFO, Locales.UPDATE_INFO_1, updater.getPlugin().getName(), updateInfo.get("id").getAsString());
        updater.log(Level.INFO, Locales.UPDATE_INFO_2);
        updater.log(Level.INFO, " ");
    }

    /**
     * Fetch information from {@link URL}.
     *
     * @param url The {@link URL} of resource.
     *
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
            updater.log(Level.WARNING, Locales.CANNOT_FETCH_INFO);
            return null;
        }
    }
}
