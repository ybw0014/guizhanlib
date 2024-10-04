package net.guizhanss.guizhanlib.updater;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.logging.Level;

/**
 * The Guizhan Builds Updater is responsible to auto-update the plugin from
 * Guizhan Builds.
 *
 * @author ybw0014
 */
@SuppressWarnings("ConstantConditions")
@Getter(AccessLevel.PACKAGE)
public final class GuizhanBuildsUpdater {

    private final Plugin plugin;
    private final File file;
    private final String owner;
    private final String repository;
    private final String branch;
    private final UpdaterConfig updaterConfig;

    /**
     * This is the helper method to start the updater.
     *
     * @param plugin     The plugin instance.
     * @param file       The plugin file.
     * @param owner      The project owner.
     * @param repository The project repository.
     * @param branch     The project branch.
     */
    @ParametersAreNonnullByDefault
    public static void start(Plugin plugin, File file, String owner, String repository, String branch) {
        start(plugin, file, owner, repository, branch, UpdaterConfig.DEFAULT);
    }

    /**
     * This is the helper method to start the updater.
     *
     * @param plugin        The plugin instance.
     * @param file          The plugin file.
     * @param owner         The project owner.
     * @param repository    The project repository.
     * @param branch        The project branch.
     * @param updaterConfig The updater config.
     */
    @ParametersAreNonnullByDefault
    public static void start(Plugin plugin, File file, String owner, String repository, String branch, UpdaterConfig updaterConfig) {
        Preconditions.checkNotNull(plugin);
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(owner);
        Preconditions.checkNotNull(repository);
        Preconditions.checkNotNull(branch);
        Preconditions.checkNotNull(updaterConfig);

        var task = new GuizhanBuildsUpdaterTask(new GuizhanBuildsUpdater(plugin, file, owner, repository, branch, updaterConfig));
        task.runTaskLater(plugin, 1);
    }

    private GuizhanBuildsUpdater(Plugin plugin, File file, String owner, String repository, String branch, UpdaterConfig updaterConfig) {
        this.plugin = plugin;
        this.file = file;
        this.owner = owner;
        this.repository = repository;
        this.branch = branch;
        this.updaterConfig = updaterConfig;
    }

    void log(Level level, String message, Object... args) {
        plugin.getLogger().log(level, String.format(message, args));
    }

    void log(Level level, Throwable throwable, String message, Object... args) {
        plugin.getLogger().log(level, throwable, () -> String.format(message, args));
    }
}
