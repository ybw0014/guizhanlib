package net.guizhanss.guizhanlib.updater;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.guizhanss.guizhanlib.utils.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * 自动更新，从构建站(builds.guizhanss.net)获取最新版本
 */
@Getter
public final class GuizhanBuildsUpdater {

    private final Plugin plugin;
    private final File file;
    private final String user;
    private final String repo;
    private final String branch;
    private final boolean checkOnly;
    private String lang;
    private JsonObject locale;

    /**
     * 初始化
     *
     * @param user      用户
     * @param repo      仓库
     * @param branch    分支
     * @param checkOnly 是否仅检查而不下载更新
     */
    @ParametersAreNonnullByDefault
    public GuizhanBuildsUpdater(Plugin plugin, File file, String user, String repo, String branch, boolean checkOnly) {
        this(plugin, file, user, repo, branch, checkOnly, "zh-CN");
    }

    /**
     * 初始化 (包含语言)
     *
     * @param user      用户
     * @param repo      仓库
     * @param branch    分支
     * @param checkOnly 是否仅检查而不下载更新
     * @param lang      设置自动更新模块语言
     */
    @ParametersAreNonnullByDefault
    public GuizhanBuildsUpdater(Plugin plugin, File file, String user, String repo, String branch, boolean checkOnly, String lang) {
        this.plugin = plugin;
        this.file = file;
        this.user = user;
        this.repo = repo;
        this.branch = branch;
        this.checkOnly = checkOnly;
        this.lang = lang;

        prepareUpdateFolder();
        loadLang();
    }

    /**
     * 创建下载目录
     */
    private void prepareUpdateFolder() {
        File dir = new File("plugins/" + Bukkit.getUpdateFolder());

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 开始检查更新
     */
    public void start() {
        scheduleAsyncUpdateTask(new UpdaterTask(this));
    }

    /**
     * 获取构建站所有仓库列表(repos.json)地址
     *
     * @return 仓库列表地址
     */
    @Nonnull
    public String getReposFileURL() {
        return "https://builds.guizhanss.net/repos.json";
    }

    /**
     * 获得仓库列表中查询使用的键名
     *
     * @return 键名
     */
    @Nonnull
    public String getRepoKey() {
        return MessageFormat.format("{0}/{1}:{2}", user, repo, branch);
    }

    /**
     * 获取构建信息(builds.json)地址
     *
     * @param directory 工作目录
     * @return 构建信息地址
     */
    @Nonnull
    public String getVersions(@Nonnull String directory) {
        return MessageFormat.format("https://builds.guizhanss.net/f/{0}/builds.json", directory);
    }

    /**
     * 获取日志组件
     *
     * @return 日志组件
     */
    @Nonnull
    public Logger getLogger() {
        return plugin.getLogger();
    }

    /**
     * 获取构建文件URL
     *
     * @param directory 工作目录
     * @param target    构建文件名
     * @return 构建文件URL
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getTargetUrl(String directory, String target) {
        return MessageFormat.format("https://builds.guizhanss.net/f/{0}/{1}", directory, target);
    }

    /**
     * 运行异步任务
     *
     * @param task 任务
     */
    private void scheduleAsyncUpdateTask(@Nonnull UpdaterTask task) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
    }

    /**
     * 加载更新模块语言文件
     */
    private void loadLang() {
        // 从资源中加载
        InputStream stream = plugin.getResource("updater.json");
        if (stream == null) {
            throw new IllegalStateException("The updater's language file is missing, did you correctly relocated it?");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        JsonObject langRoot = (JsonObject) JsonUtil.parse(reader);

        if (JsonUtil.getFromPath(langRoot, lang) != null) {
            lang = "en";
            langRoot = (JsonObject) JsonUtil.getFromPath(langRoot, lang);
        }

        // 加载
        this.locale = langRoot;
    }

    @Nonnull
    public String getFromLocale(@Nonnull String key, @Nonnull String defaultString) {
        Preconditions.checkNotNull(key, "language key should not be null");
        Preconditions.checkNotNull(defaultString, "default string should not be null");

        try {
            if (this.locale != null) {
                return this.locale.get(key).getAsString();
            }
        } catch (NullPointerException ignored) {
        }
        return defaultString;
    }
}
