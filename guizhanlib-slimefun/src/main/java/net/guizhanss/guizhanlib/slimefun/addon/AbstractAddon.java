package net.guizhanss.guizhanlib.slimefun.addon;

import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.guizhanss.guizhanlib.minecraft.config.YamlConfig;
import net.guizhanss.guizhanlib.minecraft.plugin.AbstractJavaPlugin;
import net.guizhanss.guizhanlib.minecraft.plugin.Environment;
import net.guizhanss.guizhanlib.minecraft.plugin.Logger;
import net.guizhanss.guizhanlib.minecraft.plugin.Scheduler;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.logging.Level;

/**
 * An abstract {@link SlimefunAddon} class that contains some utilities.
 * <p>
 * Extend this as your main class to use them.
 * <p>
 * Modified from InfinityLib
 *
 * @author Mooy1
 * @author ybw0014
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"ConstantConditions", "unused"})
public abstract class AbstractAddon extends AbstractJavaPlugin implements SlimefunAddon {

    private static final int MOD = 1000000007;

    @Nullable
    private static AbstractAddon instance;

    private final String autoUpdateKey;

    private int slimefunTickCount;

    /**
     * Addon constructor.
     *
     * @param githubUser    GitHub username of this project
     * @param githubRepo    GitHub repository of this project
     * @param githubBranch  GitHub branch of this project
     * @param autoUpdateKey Auto update key in the config
     */
    protected AbstractAddon(String githubUser, String githubRepo, String githubBranch, String autoUpdateKey) {
        super(githubUser, githubRepo, githubBranch);
        this.autoUpdateKey = autoUpdateKey;
    }

    /**
     * Get an instance of extended class of {@link AbstractAddon}.
     *
     * @param <T> The class that extends {@link AbstractAddon}, which is the real addon main class
     * @return The instance of extended class of {@link AbstractAddon}
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T extends AbstractAddon> T getInstance() {
        return (T) Objects.requireNonNull(instance, "Addon is not enabled!");
    }

    private static void setInstance(@Nullable AbstractAddon inst) {
        instance = inst;
    }

    /**
     * Get the {@link YamlConfig} for the default {@code config.yml}.
     *
     * @return the {@link YamlConfig}
     */
    @Nonnull
    public static YamlConfig getAddonConfig() {
        return getInstance().getPluginConfig();
    }

    /**
     * Get the wrapping {@link Logger}.
     *
     * @return the {@link Logger}
     */
    @Nonnull
    public static Logger getAddonLogger() {
        return getInstance().getPluginLogger();
    }

    /**
     * Get the {@link Scheduler}.
     *
     * @return the {@link Scheduler}
     */
    @Nonnull
    public static Scheduler getAddonScheduler() {
        return getInstance().getPluginScheduler();
    }

    /**
     * Returns the total number of Slimefun ticks that have occurred.
     *
     * @return total number of Slimefun ticks
     */
    public static int getSlimefunTickCount() {
        return getInstance().slimefunTickCount;
    }

    /**
     * Get the {@link PluginCommand} of {@link AbstractAddon}.
     *
     * @param command the command name
     * @return the {@link PluginCommand} of {@link AbstractAddon}
     */
    @Nonnull
    public static PluginCommand getPluginCommand(String command) {
        Preconditions.checkArgument(command != null, "command should not be null");
        return Objects.requireNonNull(getInstance().getCommand(command));
    }

    /**
     * Creates a {@link NamespacedKey} from the given string.
     *
     * @param key the {@link String} representation of the key
     * @return the {@link NamespacedKey} created from given string
     */
    @Nonnull
    public static NamespacedKey createKey(String key) {
        return new NamespacedKey(getInstance(), key);
    }

    @Override
    protected final void assignSubclassInstance() {
        if (instance != null) {
            throw new IllegalStateException(
                "Addon " + instance.getName() + " is already using this GuizhanLib, Shade an relocate your own!"
            );
        }

        setInstance(this);
    }

    @Override
    protected final void clearSubclassInstance() {
        setInstance(null);
    }

    @Override
    protected final void startPlatformTasks() {
        if (getEnvironment() != Environment.TEST) {
            // global slimefun tick count
            // AVOID using this, machines should have their own instance tick count
            getPluginScheduler().repeat(
                Slimefun.getTickerTask().getTickRate(),
                () -> slimefunTickCount = (slimefunTickCount + 1) % MOD
            );

            // auto update
            if (!getPluginConfig().contains(autoUpdateKey)) {
                getPluginLogger().log(Level.WARNING, () -> "Auto update is not properly configured, default to enabled");
                getPluginConfig().set(autoUpdateKey, true);
                getPluginConfig().save();
            }
            if (getPluginConfig().getBoolean(autoUpdateKey, true)) {
                autoUpdate();
            }
        }
    }

    @Override
    protected final void resetPlatformState() {
        slimefunTickCount = 0;
    }

    /**
     * Override this for auto update logic.
     */
    protected void autoUpdate() {
    }

    @Override
    @Nonnull
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    /**
     * This returns the default bug tracker URL by the given GitHub username and repository in constructor.
     * <p>
     * Override it if you don't use GitHub issues as bug tracker.
     *
     * @return the default bug tracker url
     */
    @Nonnull
    @Override
    public String getBugTrackerURL() {
        return super.getBugTrackerURL();
    }
}
