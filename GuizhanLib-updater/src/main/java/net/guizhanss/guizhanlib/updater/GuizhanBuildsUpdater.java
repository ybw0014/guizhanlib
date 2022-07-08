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
 * The Guizhan Builds Updater is responsible to auto-update the plugin from
 * Guizhan Builds (builds.guizhanss.net).
 *
 * @author ybw0014
 */
public class GuizhanBuildsUpdater extends AbstractGuizhanBuildsUpdater {

    /**
     * This constructor sets up the updater.
     *
     * @param plugin    The {@link Plugin} instance
     * @param file      The {@link File} of plugin
     * @param user      GitHub user
     * @param repo      GitHub repository
     * @param branch    GitHub branch
     * @param checkOnly Whether to check the version only, without downloading
     */
    @ParametersAreNonnullByDefault
    public GuizhanBuildsUpdater(Plugin plugin, File file, String user, String repo, String branch, boolean checkOnly) {
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
    public GuizhanBuildsUpdater(Plugin plugin, File file, String user, String repo, String branch, boolean checkOnly, String lang) {
        super(plugin, file, user, repo, branch, checkOnly);
    }

    @Nonnull
    @Override
    public String getBuildsURL() {
        return "https://builds.guizhanss.net";
    }
}
