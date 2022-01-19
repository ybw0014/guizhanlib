package net.guizhanss.guizhanlib.updater;

import com.google.gson.JsonObject;
import net.guizhanss.guizhanlib.utils.JsonUtil;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
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

public class UpdaterTask implements Runnable {

    private final GuizhanBuildsUpdater updater;
    private final Plugin plugin;
    private final File file;
    private final Logger logger;

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

        checkVersion(format);
    }

    private @Nullable String getVersionFormat() {
        try {
            URL repos = new URL(updater.getRepos());
            JsonObject reposJson = JsonUtil.parse(fetch(repos));
            if (!reposJson.has(updater.getRepoKey())) {
                return null;
            }

            JsonObject repo = (JsonObject) reposJson.get(updater.getRepoKey());
            return ((JsonObject) repo.get("options.target.version")).getAsString();
        } catch (MalformedURLException x) {
            logger.log(Level.SEVERE, "构建站URL地址错误，无法获取版本格式信息");
            return null;
        }
    }

    private void checkVersion(String format) {
        String regex = format.replace("{version}", "\\d{1,6}")
            .replace("{git_commit}", "\\s+([a-z0-9]{7})");
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(plugin.getDescription().getVersion());
        logger.log(Level.INFO, m.matches() ? "版本匹配" : "版本不匹配");

    }

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
        } catch (IOException | NullPointerException e) {
            logger.log(Level.WARNING, "无法从构建站获取更新信息", e);
            return null;
        }
    }
}
