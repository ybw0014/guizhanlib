package net.guizhanss.guizhanlib.minecraft.plugin;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.guizhanss.guizhanlib.minecraft.config.YamlConfig;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Shared lifecycle template for GuizhanLib Java plugins.
 *
 * @author ybw0014
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"ConstantConditions", "unused"})
public abstract class AbstractJavaPlugin extends JavaPlugin {

    @Nullable
    private static AbstractJavaPlugin instance;

    @Getter
    @Accessors(makeFinal = true)
    private final Environment environment;

    @Nullable
    private YamlConfig config;
    @Nullable
    private Logger logger;
    private boolean loading;
    private boolean enabling;
    private boolean disabling;

    /**
     * Create a new abstract Java plugin.
     */
    protected AbstractJavaPlugin() {
        this(Environment.detect());
    }

    /**
     * Create a new abstract Java plugin with explicit environment.
     *
     * @param environment runtime environment
     */
    protected AbstractJavaPlugin(Environment environment) {
        this.environment = environment;
    }

    /**
     * Get the enabled addon.
     *
     * @return the enabled addon
     */
    protected static AbstractJavaPlugin getPlatformInstance() {
        return Objects.requireNonNull(instance, "Addon is not enabled!");
    }

    /**
     * Get the addon's {@link YamlConfig} for the default {@code config.yml}.
     *
     * @return the {@link YamlConfig}
     */
    @Nonnull
    public static YamlConfig config() {
        return getPlatformInstance().getPluginConfig();
    }

    /**
     * Get the addon's wrapping {@link Logger}.
     *
     * @return the {@link Logger}
     */
    @Nonnull
    public static Logger logger() {
        return getPlatformInstance().getPluginLogger();
    }

    /**
     * Get the {@link PluginCommand} of {@link AbstractJavaPlugin}.
     *
     * @param command the command name
     * @return the {@link PluginCommand} of {@link AbstractJavaPlugin}
     */
    @Nonnull
    public static PluginCommand getPluginCommand(String command) {
        Preconditions.checkArgument(command != null, "command should not be null");
        return Objects.requireNonNull(getPlatformInstance().getCommand(command));
    }

    /**
     * Creates a {@link NamespacedKey} from the given string.
     *
     * @param key the {@link String} representation of the key
     * @return the {@link NamespacedKey} created from given string
     */
    @Nonnull
    public static NamespacedKey createKey(String key) {
        return new NamespacedKey(getPlatformInstance(), key);
    }

    /**
     * Use {@link #load()} instead.
     */
    @Override
    public final void onLoad() {
        if (loading) {
            throw new IllegalStateException(getName() + " is already loading! Do not call super.onLoad()!");
        }

        loading = true;

        try {
            load();
        } catch (RuntimeException ex) {
            handleException(ex);
        } finally {
            loading = false;
        }
    }

    /**
     * Use {@link #enable()} instead.
     */
    @Override
    public final void onEnable() {
        if (enabling) {
            throw new IllegalStateException(getName() + " is already enabling! Do not call super.onEnable()!");
        }

        enabling = true;
        assignSubclassInstance();

        try {
            config = new YamlConfig(this, "config.yml");
        } catch (RuntimeException ex) {
            handleException(ex);
        }

        logger = new Logger(this);
        startPlatformTasks();

        try {
            enable();
        } catch (RuntimeException ex) {
            handleException(ex);
        } finally {
            enabling = false;
        }
    }

    /**
     * Use {@link #disable()} instead.
     */
    @Override
    public final void onDisable() {
        if (disabling) {
            throw new IllegalStateException(getName() + " is already disabling! Do not call super.onDisable()!");
        }

        disabling = true;

        try {
            disable();
        } catch (RuntimeException ex) {
            handleException(ex);
        } finally {
            disabling = false;
            clearSubclassInstance();
            config = null;
            logger = null;
            resetPlatformState();
        }
    }

    /**
     * Called when loading.
     */
    protected void load() {
    }

    /**
     * Called when enabling.
     */
    protected abstract void enable();

    /**
     * Called when disabling.
     */
    protected abstract void disable();

    /**
     * Assign the shared addon instance when enabling.
     */
    protected final void assignSubclassInstance() {
        if (instance != null) {
            throw new IllegalStateException(
                "Addon " + instance.getName() + " is already using this GuizhanLib, Shade an relocate your own!"
            );
        }

        instance = this;
    }

    /**
     * Clear the shared addon instance when disabling.
     */
    protected final void clearSubclassInstance() {
        instance = null;
    }

    /**
     * Start platform-specific tasks after runtime state is ready.
     */
    protected void startPlatformTasks() {
    }

    /**
     * Reset platform-specific state after runtime cleanup.
     */
    protected void resetPlatformState() {
    }

    /**
     * Get the {@link YamlConfig} for {@code config.yml}.
     *
     * @return the {@link YamlConfig} instance
     */
    @Nonnull
    public final YamlConfig getPluginConfig() {
        if (config == null) {
            throw new IllegalStateException("Config is not available");
        }

        return config;
    }

    /**
     * Get the shared logger wrapper.
     *
     * @return the shared logger wrapper
     */
    @Nonnull
    public final Logger getPluginLogger() {
        if (logger == null) {
            throw new IllegalStateException("Logger is not available");
        }

        return logger;
    }

    /**
     * Handle lifecycle exception based on environment.
     *
     * @param ex the lifecycle exception
     */
    protected final void handleException(RuntimeException ex) {
        if (environment == Environment.TEST) {
            throw ex;
        }

        ex.printStackTrace();
    }

    /**
     * Get the default config.
     *
     * @return the default config
     */
    @Override
    @Nonnull
    public final FileConfiguration getConfig() {
        return getPluginConfig();
    }

    /**
     * Save default config.
     * Overridden and does nothing since it is handled in {@link #onEnable()}.
     */
    @Override
    public final void saveDefaultConfig() {
    }
}
