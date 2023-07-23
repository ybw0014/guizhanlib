package net.guizhanss.guizhanlib.updater;

import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;

/**
 * The Guizhan Builds Updater is responsible to auto-update the plugin from
 * Mainland China mirror of Guizhan Builds (builds.guizhanss.cn).
 *
 * @author ybw0014
 */
public final class GuizhanBuildsCNUpdater extends AbstractGuizhanBuildsUpdater {

    /**
     * This constructor sets up the updater.
     *
     * @param plugin    The {@link Plugin} instance
     * @param file      The {@link File} of plugin
     * @param user      GitHub user
     * @param repo      GitHub repository
     * @param branch    GitHub branch
     */
    @ParametersAreNonnullByDefault
    public GuizhanBuildsCNUpdater(Plugin plugin, File file, String user, String repo, String branch) {
        super(plugin, file, user, repo, branch, UpdaterConfig.DEFAULT);
    }

    /**
     * This constructor sets up the updater.
     *
     * @param plugin    The {@link Plugin} instance
     * @param file      The {@link File} of plugin
     * @param user      GitHub user
     * @param repo      GitHub repository
     * @param branch    GitHub branch
     * @param updaterConfig The {@link UpdaterConfig}.
     */
    @ParametersAreNonnullByDefault
    public GuizhanBuildsCNUpdater(Plugin plugin, File file, String user, String repo, String branch,
                                UpdaterConfig updaterConfig) {
        super(plugin, file, user, repo, branch, updaterConfig);
    }

    /**
     * This constructor sets up the updater.
     *
     * @param plugin    The {@link Plugin} instance
     * @param file      The {@link File} of plugin
     * @param user      GitHub user
     * @param repo      GitHub repository
     * @param branch    GitHub branch
     * @param checkOnly Whether to check the version only, without downloading
     *
     * @deprecated in favor of {@link UpdaterConfig}.
     */
    @Deprecated
    @ParametersAreNonnullByDefault
    public GuizhanBuildsCNUpdater(Plugin plugin, File file, String user, String repo, String branch, boolean checkOnly) {
        super(plugin, file, user, repo, branch, checkOnly);
    }

    /**
     * This constructor sets up the updater.
     *
     * @param plugin    The {@link Plugin} instance
     * @param file      The {@link File} of plugin
     * @param user      GitHub user
     * @param repo      GitHub repository
     * @param branch    GitHub branch
     * @param checkOnly Whether to check the version only, without downloading
     * @param lang      The language of updater
     *
     * @deprecated The language option is no longer used, since it is now
     * configured under global config file (/plugins/GuizhanBuildsUpdater/config.yml).
     */
    @Deprecated
    @ParametersAreNonnullByDefault
    public GuizhanBuildsCNUpdater(Plugin plugin, File file, String user, String repo, String branch, boolean checkOnly, String lang) {
        super(plugin, file, user, repo, branch, checkOnly);
    }

    @Nonnull
    @Override
    public String getBuildsURL() {
        return "https://builds.guizhanss.cn";
    }

    @Nonnull
    @Override
    public String getR2URL() {
        return "https://builds-r2.gzassets.cn";
    }

    /**
     * Set the language of updater.
     *
     * @return the language of updater
     */
    @Nonnull
    @Override
    public String getLanguage() {
        return "zh-CN";
    }
}
