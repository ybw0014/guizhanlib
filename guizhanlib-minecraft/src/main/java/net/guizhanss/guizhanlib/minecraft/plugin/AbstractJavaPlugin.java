package net.guizhanss.guizhanlib.minecraft.plugin;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.guizhanss.guizhanlib.minecraft.config.YamlConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.MessageFormat;
import java.util.regex.Pattern;

/**
 * Shared lifecycle template for GuizhanLib Java plugins.
 *
 * @author ybw0014
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"ConstantConditions", "unused"})
public abstract class AbstractJavaPlugin extends JavaPlugin {

    private static final Pattern GITHUB_PATTERN = Pattern.compile("[\\w-]+");

    @Getter
    @Accessors(makeFinal = true)
    private final Environment environment;
    @Getter
    @Accessors(makeFinal = true)
    private final String githubUser;
    @Getter
    @Accessors(makeFinal = true)
    private final String githubRepo;
    @Getter
    @Accessors(makeFinal = true)
    private final String githubBranch;
    @Getter
    private final String bugTrackerURL;

    @Nullable
    private YamlConfig config;
    @Nullable
    private Logger logger;
    @Nullable
    private Scheduler scheduler;
    private boolean loading;
    private boolean enabling;
    private boolean disabling;

    /**
     * Create a new abstract Java plugin.
     *
     * @param githubUser   GitHub username of this project
     * @param githubRepo   GitHub repository of this project
     * @param githubBranch GitHub branch of this project
     */
    protected AbstractJavaPlugin(String githubUser, String githubRepo, String githubBranch) {
        this(Environment.detect(), githubUser, githubRepo, githubBranch);
    }

    /**
     * Create a new abstract Java plugin with explicit environment.
     *
     * @param environment  runtime environment
     * @param githubUser   GitHub username of this project
     * @param githubRepo   GitHub repository of this project
     * @param githubBranch GitHub branch of this project
     */
    protected AbstractJavaPlugin(
        Environment environment,
        String githubUser,
        String githubRepo,
        String githubBranch
    ) {
        this.environment = environment;
        this.githubUser = githubUser;
        this.githubRepo = githubRepo;
        this.githubBranch = githubBranch;
        this.bugTrackerURL = MessageFormat.format("https://github.com/{0}/{1}/issues", githubUser, githubRepo);
        validate();
    }

    /**
     * Validate shared metadata provided by constructor.
     */
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
        scheduler = new Scheduler(this);
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
            scheduler = null;
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
     * Assign subclass-owned singleton/state references.
     */
    protected abstract void assignSubclassInstance();

    /**
     * Clear subclass-owned singleton/state references.
     */
    protected abstract void clearSubclassInstance();

    /**
     * Start platform-specific tasks after scheduler is ready.
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
     * Get the shared scheduler wrapper.
     *
     * @return the shared scheduler wrapper
     */
    @Nonnull
    public final Scheduler getPluginScheduler() {
        if (scheduler == null) {
            throw new IllegalStateException("Scheduler is not available");
        }

        return scheduler;
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
