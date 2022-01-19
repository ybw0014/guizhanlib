package net.guizhanss.guizhanlib.updater;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * 自动更新，从构建站(builds.guizhanss.net)获取最新版本
 */
public class GuizhanBuildsUpdater {

    @Getter
    private final Plugin plugin;
    @Getter
    private final File file;
    private final String user;
    private final String repo;
    private final String branch;
    private final boolean checkOnly;

    /**
     * 初始化
     * @param user 用户
     * @param repo 仓库
     * @param branch 分支
     * @param checkOnly 是否仅检查而不下载更新
     */
    @ParametersAreNonnullByDefault
    public GuizhanBuildsUpdater(Plugin plugin, File file, String user, String repo, String branch, boolean checkOnly) {
        this.plugin = plugin;
        this.file = file;
        this.user = user;
        this.repo = repo;
        this.branch = branch;
        this.checkOnly = checkOnly;

        prepareUpdateFolder();
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
     * @return 仓库列表地址
     */
    public @Nonnull String getRepos() {
        return "https://builds.guizhanss.net/repos.json";
    }

    /**
     * 获得仓库列表中查询使用的键名
     * @return 键名
     */
    public @Nonnull String getRepoKey() {
        return MessageFormat.format("{0}/{1}:{2}", user, repo, branch);
    }

    /**
     * 获取构建信息(builds.json)地址
     * @return 构建信息地址
     */
    public @Nonnull String getVersions() {
        return MessageFormat.format("https://builds.guizhanss.net/f/{0}/{1}/{2}/builds.json", user, repo, branch);
    }

    /**
     * 获取日志组件
     * @return 日志组件
     */
    public @Nonnull Logger getLogger() {
        return plugin.getLogger();
    }

    /**
     * 运行异步任务
     * @param task 任务
     */
    private void scheduleAsyncUpdateTask(@Nonnull UpdaterTask task) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
    }
}
