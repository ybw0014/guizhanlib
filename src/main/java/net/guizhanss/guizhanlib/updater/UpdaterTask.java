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

public class UpdaterTask implements Runnable {

    private final GuizhanBuildsUpdater updater;
    private final Plugin plugin;
    private final File file;
    private final Logger logger;

    private JsonObject updateInfo = null;

    UpdaterTask(GuizhanBuildsUpdater updater) {
        this.updater = updater;
        this.plugin = updater.getPlugin();
        this.file = updater.getFile();
        this.logger = updater.getLogger();
    }

    @Override
    public void run() {
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
     * 从构建站获取版本格式
     * @return 版本格式，null表示获取失败
     */
    private @Nullable String getVersionFormat() {
        try {
            URL repos = new URL(updater.getRepos());
            JsonObject reposJson = (JsonObject) JsonUtil.parse(fetch(repos));
            JsonElement repo = JsonUtil.getFromPath(reposJson, updater.getRepoKey());

            if (repo == null) {
                return null;
            }

            return JsonUtil.getFromPath((JsonObject) repo, "options.target.version").getAsString();
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, "构建站URL地址错误，无法获取版本格式信息");
            return null;
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
            URL buildsUrl = new URL(updater.getVersions());
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

            boolean needUpdate = !updater.getTargetFilename().equals(build.get("target").getAsString());
            if (!needUpdate) {
                logger.log(Level.INFO, "{0} 已是最新版本，无需更新", plugin.getName());
                return false;
            }
            updateInfo = build;
            return true;
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, "构建站URL地址错误，无法获取所有构建信息");
            return false;
        }
    }

    /**
     * 下载并安装新版本
     */
    private void update() {
        String targetFilename = updateInfo.get("target").getAsString();
        logger.log(Level.INFO, "{0} 需要进行更新", plugin.getName());
        logger.log(Level.INFO, "正在下载 {0}", targetFilename);

        try {
            BufferedInputStream input = new BufferedInputStream(new URL(updater.getTargetUrl(targetFilename)).openStream());
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
        plugin.getLogger().log(Level.INFO, "已更新 {0}", plugin.getName());
        plugin.getLogger().log(Level.INFO, "重启服务器以安装新版本");
        plugin.getLogger().log(Level.INFO, " ");
    }

    /**
     * 从URL获取所有内容
     * @param url 资源所在的 {@link URL}
     * @return 所有内容 {@link String}
     */
    private @Nullable String fetch(@Nonnull URL url) {
        StringBuilder content = null;
        try {
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10_000);
            connection.addRequestProperty("User-Agent", "Guizhan Updater");
            connection.setDoOutput(true);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                while (line != null) {
                    if (content == null) {
                        content = new StringBuilder(line);
                    } else {
                        content.append(line);
                    }
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
