package net.guizhanss.guizhanlib.slimefun.addon;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import net.guizhanss.guizhanlib.updater.GuizhanBuildsUpdater;
import net.guizhanss.guizhanlib.utils.ChatUtil;
import org.apache.commons.lang.Validate;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Level;

/**
 * An abstract {@link SlimefunAddon} class that contains
 * the updater and some utilities.
 *
 * Extend this as your main class to use them.
 *
 * This is modified from InfinityLib
 *
 * @author Mooy1
 * @author ybw0014
 */
@ParametersAreNonnullByDefault
public abstract class AbstractAddon extends JavaPlugin implements SlimefunAddon {

    private static AbstractAddon instance;

    private final Environment environment;
    private final String githubUser;
    private final String githubRepo;
    private final String githubBranch;
    private final String autoUpdateKey;
    private final String updaterLangKey;
    private final String bugTrackerURL;

    private AddonConfig config;
    private int metricsId;
    private Metrics metrics;
    private boolean loading;
    private boolean enabling;
    private boolean disabling;
    private boolean autoUpdateEnabled;

    /**
     * Live addon constructor (old)
     *
     * @param githubUser GitHub username of this project
     * @param githubRepo GitHub repository of this project
     * @param githubBranch GitHub branch of this project
     * @param autoUpdateKey Auto update key in the config
     */
    @Deprecated
    public AbstractAddon(String githubUser, String githubRepo, String githubBranch, String autoUpdateKey) {
        this(githubUser, githubRepo, githubBranch, autoUpdateKey, "");
    }

    /**
     * Live addon constructor
     *
     * @param githubUser GitHub username of this project
     * @param githubRepo GitHub repository of this project
     * @param githubBranch GitHub branch of this project
     * @param autoUpdateKey Auto update key in the config
     * @param updaterLangKey Updater language key in the config. Leave this empty if you want updater be in English.
     */
    public AbstractAddon(String githubUser, String githubRepo, String githubBranch, String autoUpdateKey, String updaterLangKey) {
        this.environment = Environment.LIVE;
        this.githubUser = githubUser;
        this.githubRepo = githubRepo;
        this.githubBranch = githubBranch;
        this.autoUpdateKey = autoUpdateKey;
        this.updaterLangKey = updaterLangKey;
        this.bugTrackerURL = MessageFormat.format("https://github.com/{0}/{1}/issues", githubUser, githubRepo);
        validate();
    }

    /**
     * Testing addon constructor
     *
     * @param loader the {@link JavaPluginLoader}
     * @param description the {@link PluginDescriptionFile} of plugin
     * @param dataFolder the {@link File} of plugin's data folder
     * @param file the {@link File} of plugin
     * @param githubUser GitHub username of this project
     * @param githubRepo GitHub repository of this project
     * @param githubBranch GitHub branch of this project
     * @param autoUpdateKey Auto update key in the config
     * @param updaterLangKey Updater language key in the config
     */
    public AbstractAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file,
                         String githubUser, String githubRepo, String githubBranch, String autoUpdateKey, String updaterLangKey) {
        this(loader, description, dataFolder, file, githubUser, githubRepo, githubBranch, autoUpdateKey, updaterLangKey, Environment.TESTING);
    }

    /**
     * Testing library constructor
     *
     * @param loader the {@link JavaPluginLoader}
     * @param description the {@link PluginDescriptionFile} of plugin
     * @param dataFolder the {@link File} of plugin's data folder
     * @param file the {@link File} of plugin
     * @param githubUser GitHub username of this project
     * @param githubRepo GitHub repository of this project
     * @param githubBranch GitHub branch of this project
     * @param autoUpdateKey Auto update key in the config
     * @param updaterLangKey Updater language key in the config
     * @param environment the {@link Environment} of file
     */
    AbstractAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file,
                  String githubUser, String githubRepo, String githubBranch, String autoUpdateKey, String updaterLangKey,
                  Environment environment) {
        super(loader, description, dataFolder, file);
        this.environment = environment;
        this.githubUser = githubUser;
        this.githubBranch = githubBranch;
        this.githubRepo = githubRepo;
        this.autoUpdateKey = autoUpdateKey;
        this.updaterLangKey = updaterLangKey;
        this.bugTrackerURL = MessageFormat.format("https://github.com/{0}/{1}/issues", githubUser, githubRepo);
        validate();
    }

    /**
     * Validate the information given by constructor
     */
    private void validate() {
        if (instance != null) {
            throw new IllegalStateException("Addon " + instance.getName() + " is already using this GuizhanLib, Shade an relocate your own!");
        }
        if (!githubUser.matches("[\\w-]+")) {
            throw new IllegalArgumentException("Invalid githubUser");
        }
        if (!githubRepo.matches("[\\w-]+")) {
            throw new IllegalArgumentException("Invalid githubRepo");
        }
        if (!githubBranch.matches("[\\w-]+")) {
            throw new IllegalArgumentException("Invalid githubBranch");
        }
    }

    /**
     * Use load() instead
     */
    @Override
    public final void onLoad() {
        if (loading) {
            throw new IllegalStateException(getName() + " is already loading! Do not call super.onLoad()!");
        }

        loading = true;

        // Load
        try {
            load();
        }
        catch (RuntimeException e) {
            handleException(e);
        }
        finally {
            loading = false;
        }
    }

    /**
     * Use enable() instead
     */
    @Override
    public final void onEnable() {
        if (enabling) {
            throw new IllegalStateException(getName() + " is already enabling! Do not call super.onEnable()!");
        }

        enabling = true;

        // Set instance
        instance = this;

        // This is used to check if the auto update config is broken
        boolean brokenConfig = false;

        // Check config.yml
        try {
            config = new AddonConfig("config.yml");
        } catch (RuntimeException e) {
            brokenConfig = true;
            e.printStackTrace();
        }

        // Validate autoUpdateKey
        if (autoUpdateKey == null || autoUpdateKey.isEmpty()) {
            brokenConfig = true;
            handleException(new IllegalStateException("Invalid autoUpdateKey"));
        }

        if (!brokenConfig && !config.getDefaults().contains(autoUpdateKey, true)) {
            brokenConfig = true;
            handleException(new IllegalStateException("Auto update key missing from the default config!"));
        }

        String updaterLang = "en";
        // Validate updaterLangKey
        if (updaterLangKey != null && !updaterLangKey.isEmpty()) {
            updaterLang = config.getString(updaterLangKey, "en");
        }

        // Check updater
        if (environment == Environment.LIVE) {
            GuizhanBuildsUpdater updater = new GuizhanBuildsUpdater(this, getFile(), githubUser, githubRepo, githubBranch, false, updaterLang);
            // Set up warning
            if (brokenConfig) {
                handleException(new IllegalArgumentException("Auto update is not configured correctly"));
            } else if (config.getBoolean(autoUpdateKey)) {
                autoUpdateEnabled = true;
                updater.start();
            }
        }

        // Setup metrics
        if (metricsId != 0) {
            metrics = new Metrics(this, metricsId);
            setupMetrics(metrics);
        }

        // Call enable()
        try {
            enable();
        } catch (RuntimeException ex) {
            handleException(ex);
        } finally {
            enabling = false;
        }
    }

    /**
     * Use disable() instead.
     */
    @Override
    public final void onDisable() {
        if (disabling) {
            throw new IllegalStateException(getName() + " is already disabling! Do not call super.onDisable()!");
        }

        disabling = true;

        try {
            disable();
        } catch (RuntimeException e) {
            handleException(e);
        } finally {
            disabling = false;
            instance = null;
            config = null;
        }
    }

    /**
     * Called when loading
     */
    protected void load() {

    }

    /**
     * Called when enabling
     */
    protected abstract void enable();

    /**
     * Called when disabling
     */
    protected abstract void disable();

    /**
     * Handle the {@link RuntimeException} in different environments.
     * Print the exception if in live environment, throw the exception if in testing environment
     *
     * @param ex the {@link RuntimeException}
     */
    private void handleException(RuntimeException ex) {
        switch (environment) {
            case LIVE:
                ex.printStackTrace();
                break;
            case TESTING:
                throw ex;
        }
    }

    /**
     * Enable metrics module.
     * Should be called in constructor.
     *
     * @param pluginId the plugin id in bStats
     */
    public void enableMetrics(int pluginId) {
        metricsId = pluginId;
    }

    /**
     * Set up metrics module. If you need this, override it
     * e.g. Custom charts, etc...
     *
     * @param metrics The {@link Metrics} instance.
     */
    public void setupMetrics(@Nonnull Metrics metrics) {
    }

    /**
     * Get an instance of extended class of {@link AbstractAddon}
     *
     * @param <T> The class that extends {@link AbstractAddon}, which is the real addon main class
     *
     * @return The instance of extended class of {@link AbstractAddon}
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T extends AbstractAddon> T getInstance() {
        return (T) Objects.requireNonNull(instance, "Addon is not enabled!");
    }

    /**
     * Get the {@link Metrics} module
     *
     * @return the {@link Metrics} module
     */
    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Used for Slimefun to get instance of this {@link JavaPlugin}
     *
     * @return the instance of this {@link JavaPlugin}
     */
    @Nonnull
    @Override
    public final JavaPlugin getJavaPlugin() {
        return this;
    }

    /**
     * This returns the default bug tracker URL by
     * the given GitHub username and repository in constructor.
     *
     * Override it if you don't use GitHub issues as bug tracker
     *
     * @return the default bug tracker url
     */
    @Nonnull
    @Override
    public String getBugTrackerURL() {
        return bugTrackerURL;
    }

    /**
     * Get the current {@link Environment}
     *
     * @return the current {@link Environment}
     */
    @Nonnull
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * If the auto update is enabled
     *
     * @return if the auto update is enabled
     */
    public final boolean isAutoUpdateEnabled() {
        return autoUpdateEnabled;
    }

    /**
     * Get the {@link AddonConfig}
     *
     * @return the {@link AddonConfig}
     */
    @Override
    @Nonnull
    public final FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get the {@link AddonConfig}
     *
     * @return the {@link AddonConfig}
     */
    @Nonnull
    public static AddonConfig getAddonConfig() {
        return getInstance().config;
    }

    /**
     * Save default config.
     * Overridden and does nothing since it is handled in #onEnable()
     */
    @Override
    public final void saveDefaultConfig() {
    }

    /**
     * Get the {@link PluginCommand} of {@link AbstractAddon}.
     *
     * @return the {@link PluginCommand} of {@link AbstractAddon}.
     */
    public static @Nonnull PluginCommand getPluginCommand(@Nonnull String command) {
        Validate.notNull(command, "command should not be null");
        return Objects.requireNonNull(getInstance().getCommand(command));
    }

    /**
     * Creates a {@link NamespacedKey} from the given string
     *
     * @param key the {@link String} representation of the key
     *
     * @return the {@link NamespacedKey} created from given string
     */
    @Nonnull
    public static NamespacedKey createKey(String key) {
        return new NamespacedKey(getInstance(), key);
    }

    /**
     * Call the logger to log a message with arguments.
     * ChatColor code will be translated automatically,
     * and message is dealt with MessageFormat#format().
     *
     * @param level the log {@link Level}
     * @param message the message
     * @param args the arguments with in
     *
     * @see MessageFormat
     */
    public static void log(@Nonnull Level level, @Nonnull String message, @Nullable Object... args) {
        Validate.notNull(level, "log level should not be null");
        Validate.notNull(message, "log message should not be null");

        getInstance().getLogger().log(level, ChatUtil.color(MessageFormat.format(message, args)));
    }

    /**
     * Call the {@link org.bukkit.command.ConsoleCommandSender} to send a message with arguments.
     * ChatColor code will be translated automatically,
     * and message is dealt with MessageFormat#format().
     *
     * @param message the message
     * @param args the arguments with in
     *
     * @see MessageFormat
     */
    public static void sendConsole(@Nonnull String message, @Nullable Object... args) {
        Validate.notNull(message, "log message should not be null");

        Bukkit.getConsoleSender().sendMessage("[" + getInstance().getName() + "] " + ChatUtil.color(MessageFormat.format(message, args)));
    }
}
