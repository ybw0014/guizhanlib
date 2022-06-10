package net.guizhanss.guizhanlib.updater;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.guizhanss.guizhanlib.utils.JsonUtil;
import org.bukkit.plugin.Plugin;

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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自动更新任务
 * 由 {@link GuizhanBuildsUpdater} 调用
 *
 * @author ybw0014
 */
public final class UpdaterTask implements Runnable {

    private final GuizhanBuildsUpdater updater;
    private final Plugin plugin;
    private final File file;
    private final Logger logger;

    private String workingDirectory;
    private JsonObject repoInfo = null;
    private JsonObject updateInfo = null;

    UpdaterTask(GuizhanBuildsUpdater updater) {
        this.updater = updater;
        this.plugin = updater.getPlugin();
        this.file = updater.getFile();
        this.logger = updater.getLogger();
    }

    @Override
    public void run() {
        getRepoInfo();

        String format = getVersionFormat();
        if (format == null) {
            logger.log(Level.SEVERE, updater.getFromLocale("invalid_version_format", "Cannot get version format. Maybe the developer did not configure the updater correctly?"));
            return;
        }
        if (!checkVersion(format)) {
            logger.log(Level.WARNING, updater.getFromLocale("invalid_version", "You are using an unofficial version, which is not downloaded from Guizhan Builds. The updater has stopped."));
            return;
        }
        if (checkUpdate()) {
            update();
        }
    }

    /**
     * 获取仓库信息
     */
    private void getRepoInfo() {
        try {
            URL repos = new URL(updater.getReposFileURL());
            JsonObject reposJson = (JsonObject) JsonUtil.parse(fetch(repos));

            String key = updater.getRepoKey();
            JsonElement repoInfo = null;
            while (key != null) {
                repoInfo = JsonUtil.getFromPath(reposJson, key);

                if (repoInfo == null) {
                    break;
                }

                if (JsonUtil.getFromPath((JsonObject) repoInfo, "type").getAsString().equals("redirect")) {
                    key = JsonUtil.getFromPath((JsonObject) repoInfo, "options.repo").getAsString();
                } else {
                    key = null;
                }
            }

            if (repoInfo == null) {
                throw new IllegalArgumentException(updater.getFromLocale("cannot_find_repo", "Cannot find repo in Guizhan Builds."));
            }

            this.repoInfo = (JsonObject) repoInfo;

            // 获取工作目录
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
        } catch (MalformedURLException | IllegalStateException ex) {
            logger.log(Level.SEVERE, updater.getFromLocale("cannot_find_repo", "Cannot find repo in Guizhan Builds."), ex);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从构建站获取版本格式
     *
     * @return 版本格式，null表示获取失败
     */
    private @Nullable
    String getVersionFormat() {
        try {
            return JsonUtil.getFromPath(repoInfo, "options.target.version").getAsString();
        } catch (IllegalStateException ex) {
            return null;
        }
    }

    /**
     * 检查插件版本是否与构建站版本格式一致
     *
     * @param format 从构建站获取的版本格式
     * @return 格式是否一致
     */
    private boolean checkVersion(String format) {
        String regex = format.replace("(", "\\(")
            .replace(")", "\\)")
            .replace("{version}", "\\d{1,6}")
            .replace("{git_commit}", "([a-z0-9]{7})");
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(plugin.getDescription().getVersion());
        return m.matches();
    }

    /**
     * 检查是否有新版本
     *
     * @return 是否有新版本
     */
    private boolean checkUpdate() {
        try {
            // 获取构建信息
            URL buildsUrl = new URL(updater.getVersions(workingDirectory));
            JsonObject buildsJson = (JsonObject) JsonUtil.parse(fetch(buildsUrl));
            JsonArray builds = (JsonArray) JsonUtil.getFromPath(buildsJson, "builds");

            JsonObject build = null;
            for (int i = builds.size() - 1; i >= 0; i--) {
                build = (JsonObject) builds.get(i);
                if (build.get("success").getAsBoolean()) break;
                build = null;
            }
            if (build == null) {
                logger.log(Level.SEVERE, updater.getFromLocale("no_successful_builds", "There is no successful build in Guizhan Builds. How did you do this?"));
                return false;
            }

            String pluginName = JsonUtil.getFromPath(this.repoInfo, "options.target.name").getAsString();
            boolean needUpdate = !MessageFormat.format("{0}-{1}.jar", pluginName, plugin.getDescription().getVersion()).equals(build.get("target").getAsString());
            if (!needUpdate) {
                logger.log(Level.INFO, updater.getFromLocale("latest", "{0} is up-to-date."), plugin.getName());
                return false;
            }
            updateInfo = build;
            return true;
        } catch (MalformedURLException | NullPointerException ex) {
            logger.log(Level.SEVERE, updater.getFromLocale("an_error_has_occurred", "An error has occurred while checking for update"));
            return false;
        }
    }

    /**
     * 下载并安装新版本
     */
    private void update() {
        String targetFilename = updateInfo.get("target").getAsString();
        logger.log(Level.INFO, updater.getFromLocale("need_update", "An error has occurred while checking for update"), plugin.getName());
        logger.log(Level.INFO, () -> MessageFormat.format(updater.getFromLocale("downloading", "Downloading {0} - Build {1}"), plugin.getName(), updateInfo.get("id").getAsString()));

        try {
            BufferedInputStream input = new BufferedInputStream(new URL(updater.getTargetUrl(workingDirectory, targetFilename)).openStream());
            FileOutputStream output = new FileOutputStream(new File("plugins/" + plugin.getServer().getUpdateFolder(), file.getName()));
            byte[] data = new byte[1024];
            int read;

            while ((read = input.read(data, 0, 1024)) != -1) {
                output.write(data, 0, read);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex, () -> MessageFormat.format(updater.getFromLocale("update_fail", "Failed to update {0}"), plugin.getName()));
            return;
        }

        logger.log(Level.INFO, " ");
        logger.log(Level.INFO, updater.getFromLocale("info_auto_update", "============== Auto update =============="));
        logger.log(Level.INFO, () -> MessageFormat.format(updater.getFromLocale("info_auto_update_1", "Downloaded {0} Build {1}"), plugin.getName(), updateInfo.get("id").getAsString()));
        logger.log(Level.INFO, updater.getFromLocale("info_auto_update_2", "Restart server to update"));
        logger.log(Level.INFO, " ");
    }

    /**
     * 从URL获取所有内容
     *
     * @param url 资源所在的 {@link URL}
     * @return 所有内容 {@link String}
     */
    private @Nullable
    String fetch(@Nonnull URL url) {
        try {
            StringBuilder content = new StringBuilder();

            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10_000);
            connection.addRequestProperty("User-Agent", "Guizhan Updater");
            connection.setDoOutput(true);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                while (line != null) {
                    content.append(line);
                    line = reader.readLine();
                }
            }

            return content.toString();
        } catch (IOException | NullPointerException ex) {
            logger.log(Level.WARNING, updater.getFromLocale("cannot_fetch_info", "Cannot fetch info from Guizhan Builds"), ex);
            return null;
        }
    }
}
