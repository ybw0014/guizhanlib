package net.guizhanss.guizhanlib.updater;

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.guizhanss.guizhanlib.utils.JsonUtil;

/**
 * 自动更新任务
 * 由 {@link GuizhanBuildsUpdater} 调用
 *
 * @author ybw0014
 */
public class UpdaterTask implements Runnable {

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
            logger.log(Level.SEVERE, "无法获取版本格式信息，可能是自动更新模块没有配置正确");
            return;
        }
        if (!checkVersion(format)) {
            logger.log(Level.WARNING, "你使用的不是从构建站下载的版本，自动更新已禁用");
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
                throw new IllegalArgumentException("无法找到仓库信息");
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
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, "构建站URL地址错误，无法获取仓库配置信息");
        } catch (IllegalStateException ex) {
            logger.log(Level.SEVERE, "构建站配置错误，无法获取仓库配置信息", ex);
        } catch (IllegalArgumentException ex) {
            logger.log(Level.SEVERE, "无法获取仓库配置信息", ex);
        }
    }

    /**
     * 从构建站获取版本格式
     * @return 版本格式，null表示获取失败
     */
    private @Nullable String getVersionFormat() {
        try {
            return JsonUtil.getFromPath(repoInfo, "options.target.version").getAsString();
        } catch (IllegalStateException ex) {
            logger.log(Level.SEVERE, "构建站配置错误，无法获取版本格式信息", ex);
            return null;
        }
    }

    /**
     * 检查插件版本是否与构建站版本格式一致
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
     * @return 是否有新版本
     */
    private boolean checkUpdate() {
        try {
            // 获取构建信息
            URL buildsUrl = new URL(updater.getVersions(workingDirectory));
            JsonObject buildsJson = (JsonObject) JsonUtil.parse(fetch(buildsUrl));
            JsonArray builds = (JsonArray) JsonUtil.getFromPath(buildsJson, "builds");

            JsonObject build = null;
            for (int i = builds.size() - 1; i >= 0 ; i --) {
                build = (JsonObject) builds.get(i);
                if (build.get("success").getAsBoolean()) break;
                build = null;
            }
            if (build == null) {
                logger.log(Level.SEVERE, "构建站没有成功的构建，已禁用自动更新");
                return false;
            }

            String pluginName = JsonUtil.getFromPath(this.repoInfo, "options.target.name").getAsString();
            boolean needUpdate = !MessageFormat.format("{0}-{1}.jar", pluginName, plugin.getDescription().getVersion()).equals(build.get("target").getAsString());
            if (!needUpdate) {
                logger.log(Level.INFO, "{0} 已是最新版本，无需更新", plugin.getName());
                return false;
            }
            updateInfo = build;
            return true;
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, "构建站URL地址错误，无法获取所有构建信息");
            return false;
        } catch (NullPointerException ex) {
            logger.log(Level.SEVERE, "检查更新时出现错误");
            return false;
        }
    }

    /**
     * 下载并安装新版本
     */
    private void update() {
        String targetFilename = updateInfo.get("target").getAsString();
        logger.log(Level.INFO, "{0} 需要进行更新", plugin.getName());
        logger.log(Level.INFO, () -> MessageFormat.format("正在下载 {0} #{1}", plugin.getName(), updateInfo.get("id").getAsString()));

        try {
            BufferedInputStream input = new BufferedInputStream(new URL(updater.getTargetUrl(workingDirectory, targetFilename)).openStream());
            FileOutputStream output = new FileOutputStream(new File("plugins/" + plugin.getServer().getUpdateFolder(), file.getName()));
            byte[] data = new byte[1024];
            int read;

            while ((read = input.read(data, 0, 1024)) != -1) {
                output.write(data, 0, read);
            }
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, ex, () -> "更新" + plugin.getName() + "失败");
            return;
        }

        plugin.getLogger().log(Level.INFO, " ");
        plugin.getLogger().log(Level.INFO, "============== 自动更新 ==============");
        plugin.getLogger().log(Level.INFO, () -> MessageFormat.format("已更新 {0} 至 #{1}", plugin.getName(), updateInfo.get("id").getAsString()));
        plugin.getLogger().log(Level.INFO, "重启服务器以安装新版本");
        plugin.getLogger().log(Level.INFO, " ");
    }

    /**
     * 从URL获取所有内容
     * @param url 资源所在的 {@link URL}
     * @return 所有内容 {@link String}
     */
    private @Nullable String fetch(@Nonnull URL url) {
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
            logger.log(Level.WARNING, "无法从构建站获取更新信息", ex);
            return null;
        }
    }
}
