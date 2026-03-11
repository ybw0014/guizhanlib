package net.guizhanss.guizhanlib.minecraft.plugin;

import net.guizhanss.guizhanlib.minecraft.config.YamlConfig;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AbstractJavaPluginTestPlugin extends JavaPlugin {

    private static final String AUTO_UPDATE_KEY = "auto-update";

    @Nullable
    private static AbstractJavaPluginTestPlugin instance;

    private final Environment environment;
    private final FailureStage failureStage;
    private final List<String> lifecycleEvents = new ArrayList<>();

    @Nullable
    private YamlConfig config;
    @Nullable
    private Logger logger;
    @Nullable
    private Scheduler scheduler;
    @Nullable
    private RuntimeSnapshot loadSnapshot;
    @Nullable
    private RuntimeSnapshot autoUpdateSnapshot;
    @Nullable
    private RuntimeSnapshot enableSnapshot;
    @Nullable
    private RuntimeSnapshot disableSnapshot;
    private boolean loading;
    private boolean enabling;
    private boolean disabling;
    private boolean autoUpdateEnabled;

    public AbstractJavaPluginTestPlugin(String environmentName, String failureStageName) {
        this.environment = Environment.valueOf(environmentName);
        this.failureStage = FailureStage.valueOf(failureStageName);
    }

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

    @Override
    public final void onEnable() {
        if (enabling) {
            throw new IllegalStateException(getName() + " is already enabling! Do not call super.onEnable()!");
        }

        enabling = true;
        instance = this;

        boolean brokenConfig = false;

        try {
            config = new YamlConfig(this, "config.yml");
        } catch (RuntimeException ex) {
            brokenConfig = true;
            handleException(ex);
        }

        logger = new Logger(this);

        if (environment != Environment.TEST) {
            if (brokenConfig) {
                handleException(new IllegalArgumentException("Auto update is not configured correctly"));
            } else if (config != null && config.getBoolean(AUTO_UPDATE_KEY)) {
                autoUpdateEnabled = true;
                autoUpdate();
            }
        }

        scheduler = new Scheduler(this);

        try {
            enable();
        } catch (RuntimeException ex) {
            handleException(ex);
        } finally {
            enabling = false;
        }
    }

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
            autoUpdateEnabled = false;
            instance = null;
            config = null;
            logger = null;
            scheduler = null;
        }
    }

    protected void load() {
        lifecycleEvents.add("load");
        loadSnapshot = snapshot();
        maybeFail(FailureStage.LOAD);
    }

    protected void enable() {
        lifecycleEvents.add("enable");
        enableSnapshot = snapshot();
        maybeFail(FailureStage.ENABLE);
    }

    protected void disable() {
        lifecycleEvents.add("disable");
        disableSnapshot = snapshot();
        maybeFail(FailureStage.DISABLE);
    }

    protected void autoUpdate() {
        lifecycleEvents.add("auto-update");
        autoUpdateSnapshot = snapshot();
    }

    private void maybeFail(FailureStage stage) {
        if (failureStage == stage) {
            throw new IllegalStateException(stage.name().toLowerCase() + " failed");
        }
    }

    private void handleException(RuntimeException ex) {
        if (environment == Environment.TEST) {
            throw ex;
        }

        ex.printStackTrace();
    }

    RuntimeSnapshot getLoadSnapshot() {
        return requireSnapshot(loadSnapshot, "load");
    }

    @Nullable
    RuntimeSnapshot getAutoUpdateSnapshot() {
        return autoUpdateSnapshot;
    }

    RuntimeSnapshot getEnableSnapshot() {
        return requireSnapshot(enableSnapshot, "enable");
    }

    RuntimeSnapshot getDisableSnapshot() {
        return requireSnapshot(disableSnapshot, "disable");
    }

    List<String> getLifecycleEvents() {
        return List.copyOf(lifecycleEvents);
    }

    boolean isRuntimeStateCreated() {
        return config != null || logger != null || scheduler != null;
    }

    boolean isAutoUpdateEnabledForTest() {
        return autoUpdateEnabled;
    }

    boolean isInstanceAssignedForTest() {
        return instance == this;
    }

    private RuntimeSnapshot requireSnapshot(@Nullable RuntimeSnapshot snapshot, String phase) {
        if (snapshot == null) {
            throw new IllegalStateException("Missing snapshot for " + phase);
        }

        return snapshot;
    }

    private RuntimeSnapshot snapshot() {
        return new RuntimeSnapshot(config != null, logger != null, scheduler != null, instance == this);
    }

    public record RuntimeSnapshot(boolean configAvailable, boolean loggerAvailable, boolean schedulerAvailable,
                                  boolean instanceAssigned) {
    }

    public enum FailureStage {
        NONE,
        LOAD,
        ENABLE,
        DISABLE
    }
}
