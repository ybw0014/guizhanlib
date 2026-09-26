package net.guizhanss.guizhanlib.slimefun.addon;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.guizhanss.guizhanlib.minecraft.plugin.AbstractJavaPlugin;
import net.guizhanss.guizhanlib.minecraft.plugin.Environment;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.MessageFormat;
import java.util.regex.Pattern;
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

    private static final Pattern GITHUB_PATTERN = Pattern.compile("[\\w-]+");

    @Getter
    @Accessors(makeFinal = true)
    private final String githubUser;
    @Getter
    @Accessors(makeFinal = true)
    private final String githubRepo;
    @Getter
    @Accessors(makeFinal = true)
    private final String githubBranch;
    private final String autoUpdateKey;

    /**
     * Addon constructor.
     *
     * @param githubUser    GitHub username of this project
     * @param githubRepo    GitHub repository of this project
     * @param githubBranch  GitHub branch of this project
     * @param autoUpdateKey Auto update key in the config
     */
    protected AbstractAddon(String githubUser, String githubRepo, String githubBranch, String autoUpdateKey) {
        super();
        this.githubUser = githubUser;
        this.githubRepo = githubRepo;
        this.githubBranch = githubBranch;
        this.autoUpdateKey = autoUpdateKey;
        validate();
    }

    private void validate() {
        if (!GITHUB_PATTERN.matcher(githubUser).matches()) {
            throw new IllegalArgumentException("Invalid githubUser");
        }
        if (!GITHUB_PATTERN.matcher(githubRepo).matches()) {
            throw new IllegalArgumentException("Invalid githubRepo");
        }
        if (!GITHUB_PATTERN.matcher(githubBranch).matches()) {
            throw new IllegalArgumentException("Invalid githubBranch");
        }
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
        return (T) getPlatformInstance();
    }

    /**
     * Start the addon auto update task.
     */
    @Override
    protected final void startPlatformTasks() {
        setupAutoUpdate();
    }

    /**
     * Configure auto update after the scheduler is ready.
     */
    protected final void setupAutoUpdate() {
        if (getEnvironment() != Environment.TEST) {
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

    /**
     * Override this for auto update logic.
     */
    protected void autoUpdate() {
    }

    /**
     * Get the default GitHub issues URL for this addon.
     *
     * @return the default bug tracker URL
     */
    @Override
    @Nonnull
    public String getBugTrackerURL() {
        return MessageFormat.format("https://github.com/{0}/{1}/issues", githubUser, githubRepo);
    }

    /**
     * Get this addon as a Java plugin.
     *
     * @return this plugin
     */
    @Override
    @Nonnull
    public JavaPlugin getJavaPlugin() {
        return this;
    }
}
