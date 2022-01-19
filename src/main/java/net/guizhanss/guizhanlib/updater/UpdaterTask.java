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
            plugin.getLogger().log(Level.SEVERE, "无法获取版本格式信息，可能是自动更新模块没有配置正确");
            return;
        }

        plugin.getLogger().log(Level.INFO, format);
    }

    private @Nullable String getVersionFormat() {
        try {
            URL repos = new URL(updater.getRepos());
            JsonObject reposJson = (JsonObject) JsonUtil.parse(fetch(repos));
            if (!reposJson.has(updater.getRepoKey())) {
                return null;
            }

            return reposJson.get(updater.getRepoKey()).toString();
        } catch (MalformedURLException x) {
            plugin.getLogger().log(Level.SEVERE, "构建站URL地址错误，无法获取版本格式信息");
            return null;
        }
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
            plugin.getLogger().log(Level.WARNING, "无法从构建站获取更新信息", e);
            return null;
        }
    }
}
