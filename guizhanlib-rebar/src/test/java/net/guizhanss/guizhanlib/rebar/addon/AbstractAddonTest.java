package net.guizhanss.guizhanlib.rebar.addon;

import net.guizhanss.guizhanlib.minecraft.plugin.Environment;
import net.guizhanss.guizhanlib.minecraft.plugin.AbstractJavaPlugin;
import net.guizhanss.guizhanlib.minecraft.plugin.Logger;
import net.guizhanss.guizhanlib.minecraft.plugin.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.plugin.PluginManagerMock;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AbstractAddonTest {

    private static final String CONFIG_YAML = "auto-update: true\n";

    private ServerMock server;

    @BeforeEach
    void setUp() {
        LifecycleAddon.prepare(FailureStage.NONE);
        clearAddonInstance();
        if (Bukkit.getServer() != null) {
            MockBukkit.unmock();
        }
        server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        if (Bukkit.getServer() != null) {
            MockBukkit.unmock();
        }
        LifecycleAddon.prepare(FailureStage.NONE);
        clearAddonInstance();
    }

    @Test
    void inheritsSharedLifecycleContractThroughRebarWrappers() throws ReflectiveOperationException {
        LifecycleAddon plugin = loadOnly("RebarLifecyclePlugin", FailureStage.NONE);

        Assertions.assertEquals(List.of("load"), plugin.getLifecycleEvents());
        Assertions.assertEquals(new RuntimeSnapshot(false, false, false, false), plugin.getLoadSnapshot());
        assertUnavailable(AbstractAddon::getInstance);
        assertUnavailable(AbstractAddon::getAddonConfig);
        assertUnavailable(AbstractAddon::getAddonLogger);
        assertUnavailable(AbstractAddon::getAddonScheduler);
        Assertions.assertTrue(Bukkit.getScheduler().getPendingTasks().isEmpty());

        setEnvironment(plugin, Environment.LIVE);
        server.getPluginManager().enablePlugin(plugin);

        Assertions.assertEquals(List.of("load", "auto-update", "enable"), plugin.getLifecycleEvents());
        Assertions.assertEquals(new RuntimeSnapshot(true, true, false, true), plugin.getAutoUpdateSnapshot());
        Assertions.assertEquals(new RuntimeSnapshot(true, true, true, true), plugin.getEnableSnapshot());
        Assertions.assertSame(plugin, AbstractAddon.getInstance());
        Assertions.assertSame(plugin.getSharedConfigForTest(), AbstractAddon.getAddonConfig());
        Assertions.assertSame(plugin.getSharedLoggerForTest(), AbstractAddon.getAddonLogger());
        Assertions.assertSame(plugin.getSharedSchedulerForTest(), AbstractAddon.getAddonScheduler());
        Assertions.assertEquals(0, AbstractAddon.getSlimefunTickCount());
        Assertions.assertTrue(Bukkit.getScheduler().getPendingTasks().isEmpty());

        disable(plugin);

        Assertions.assertEquals(List.of("load", "auto-update", "enable", "disable"), plugin.getLifecycleEvents());
        Assertions.assertEquals(new RuntimeSnapshot(true, true, true, true), plugin.getDisableSnapshot());
        Assertions.assertEquals(0, getLegacyCounter(plugin));
        assertUnavailable(AbstractAddon::getInstance);
        assertUnavailable(AbstractAddon::getAddonConfig);
        assertUnavailable(AbstractAddon::getAddonLogger);
        assertUnavailable(AbstractAddon::getAddonScheduler);
        Assertions.assertTrue(Bukkit.getScheduler().getPendingTasks().isEmpty());
    }

    @Test
    void testEnvironmentRethrowsLifecycleExceptions() {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> loadOnly("RebarLoadFailurePlugin", FailureStage.LOAD)
        );

        LifecycleAddon enableFailurePlugin = loadOnly("RebarEnableFailurePlugin", FailureStage.ENABLE);
        Assertions.assertThrows(IllegalStateException.class, () -> server.getPluginManager().enablePlugin(enableFailurePlugin));
        clearAddonInstance();

        LifecycleAddon disableFailurePlugin = loadOnly("RebarDisableFailurePlugin", FailureStage.DISABLE);
        server.getPluginManager().enablePlugin(disableFailurePlugin);

        Assertions.assertThrows(IllegalStateException.class, () -> disable(disableFailurePlugin));
        assertUnavailable(AbstractAddon::getInstance);
        Assertions.assertEquals(0, getLegacyCounter(disableFailurePlugin));
    }

    @Test
    void legacyCounterRemainsNonTickingAndResetsOnDisable() throws ReflectiveOperationException {
        LifecycleAddon plugin = loadOnly("RebarLegacyCounterPlugin", FailureStage.NONE);
        setEnvironment(plugin, Environment.LIVE);
        server.getPluginManager().enablePlugin(plugin);

        setLegacyCounter(plugin, 7);

        Assertions.assertEquals(7, AbstractAddon.getSlimefunTickCount());
        Assertions.assertTrue(Bukkit.getScheduler().getPendingTasks().isEmpty());
        Assertions.assertEquals(7, AbstractAddon.getSlimefunTickCount());

        disable(plugin);

        Assertions.assertEquals(0, getLegacyCounter(plugin));
        assertUnavailable(AbstractAddon::getInstance);
        Assertions.assertTrue(Bukkit.getScheduler().getPendingTasks().isEmpty());
    }

    private LifecycleAddon loadOnly(String pluginName, FailureStage failureStage) {
        LifecycleAddon.prepare(failureStage);

        PluginDescriptionFile description = new PluginDescriptionFile(
            pluginName,
            "1.0.0",
            LifecycleAddon.class.getName()
        );
        PluginManagerMock pluginManager = server.getPluginManager();

        return (LifecycleAddon) pluginManager.loadPlugin(LifecycleAddon.class, description, new Object[0]);
    }

    private void disable(LifecycleAddon plugin) {
        server.getPluginManager().disablePlugin(plugin);
    }

    private static void setEnvironment(LifecycleAddon plugin, Environment environment) throws ReflectiveOperationException {
        Field field = AbstractJavaPlugin.class.getDeclaredField("environment");
        field.setAccessible(true);
        field.set(plugin, environment);
    }

    private static void clearAddonInstance() {
        try {
            Field field = AbstractAddon.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        } catch (ReflectiveOperationException ex) {
            throw new AssertionError(ex);
        }
    }

    private static void setLegacyCounter(LifecycleAddon plugin, int value) throws ReflectiveOperationException {
        Field field = AbstractAddon.class.getDeclaredField("slimefunTickCount");
        field.setAccessible(true);
        field.setInt(plugin, value);
    }

    private static int getLegacyCounter(LifecycleAddon plugin) {
        try {
            Field field = AbstractAddon.class.getDeclaredField("slimefunTickCount");
            field.setAccessible(true);
            return field.getInt(plugin);
        } catch (ReflectiveOperationException ex) {
            throw new AssertionError(ex);
        }
    }

    private static void assertUnavailable(ThrowingSupplier<?> supplier) {
        Assertions.assertThrows(RuntimeException.class, supplier::get);
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T get();
    }

    private enum FailureStage {
        NONE,
        LOAD,
        ENABLE,
        DISABLE
    }

    private record RuntimeSnapshot(boolean configAvailable, boolean loggerAvailable, boolean schedulerAvailable,
                                   boolean instanceAssigned) {
    }

    /**
     * MockBukkit fixture for inherited Rebar lifecycle regression coverage.
     */
    public static class LifecycleAddon extends AbstractAddon {

        private static FailureStage nextFailureStage = FailureStage.NONE;

        private final FailureStage failureStage;
        private final List<String> lifecycleEvents = new ArrayList<>();

        @Nullable
        private RuntimeSnapshot loadSnapshot;
        @Nullable
        private RuntimeSnapshot autoUpdateSnapshot;
        @Nullable
        private RuntimeSnapshot enableSnapshot;
        @Nullable
        private RuntimeSnapshot disableSnapshot;

        /**
         * Create the Rebar lifecycle test addon.
         */
        public LifecycleAddon() {
            super("GuizhanSs", "GuizhanLib", "master", "auto-update");
            this.failureStage = nextFailureStage;
        }

        private static void prepare(FailureStage failureStage) {
            nextFailureStage = failureStage;
        }

        /**
         * Record inherited onLoad timing through Rebar wrappers.
         */
        @Override
        protected void load() {
            lifecycleEvents.add("load");
            loadSnapshot = snapshot();
            maybeFail(FailureStage.LOAD);
        }

        /**
         * Record inherited enable timing through Rebar wrappers.
         */
        @Override
        protected void enable() {
            lifecycleEvents.add("enable");
            enableSnapshot = snapshot();
            maybeFail(FailureStage.ENABLE);
        }

        /**
         * Record inherited disable timing through Rebar wrappers.
         */
        @Override
        protected void disable() {
            lifecycleEvents.add("disable");
            disableSnapshot = snapshot();
            maybeFail(FailureStage.DISABLE);
        }

        /**
         * Record inherited auto-update timing through Rebar wrappers.
         */
        @Override
        protected void autoUpdate() {
            lifecycleEvents.add("auto-update");
            autoUpdateSnapshot = snapshot();
        }

        /**
         * Provide a minimal config resource for inherited config bootstrap.
         *
         * @param filename resource path requested by the plugin
         * @return config stream for {@code config.yml}, otherwise the superclass resource
         */
        @Override
        public InputStream getResource(String filename) {
            if ("config.yml".equals(filename)) {
                return new ByteArrayInputStream(CONFIG_YAML.getBytes(StandardCharsets.UTF_8));
            }

            return super.getResource(filename);
        }

        /**
         * Expose the minimal translation locale set required by RebarAddon.
         *
         * @return supported locales for this test addon
         */
        @Override
        public Set<Locale> getLanguages() {
            return Set.of(Locale.ENGLISH);
        }

        /**
         * Expose the minimal menu material required by RebarAddon.
         *
         * @return menu material for this test addon
         */
        @Override
        public Material getMaterial() {
            return Material.STONE;
        }

        private void maybeFail(FailureStage stage) {
            if (failureStage == stage) {
                throw new IllegalStateException(stage.name().toLowerCase() + " failed");
            }
        }

        private RuntimeSnapshot snapshot() {
            return new RuntimeSnapshot(
                isAvailable(AbstractAddon::getAddonConfig),
                isAvailable(AbstractAddon::getAddonLogger),
                isAvailable(AbstractAddon::getAddonScheduler),
                isInstanceAssigned()
            );
        }

        private static boolean isAvailable(ThrowingSupplier<?> supplier) {
            try {
                supplier.get();
                return true;
            } catch (RuntimeException ex) {
                return false;
            }
        }

        private boolean isInstanceAssigned() {
            try {
                return AbstractAddon.getInstance() == this;
            } catch (RuntimeException ex) {
                return false;
            }
        }

        private RuntimeSnapshot getLoadSnapshot() {
            return requireSnapshot(loadSnapshot, "load");
        }

        private RuntimeSnapshot getAutoUpdateSnapshot() {
            return requireSnapshot(autoUpdateSnapshot, "auto-update");
        }

        private RuntimeSnapshot getEnableSnapshot() {
            return requireSnapshot(enableSnapshot, "enable");
        }

        private RuntimeSnapshot getDisableSnapshot() {
            return requireSnapshot(disableSnapshot, "disable");
        }

        private List<String> getLifecycleEvents() {
            return List.copyOf(lifecycleEvents);
        }

        private net.guizhanss.guizhanlib.minecraft.config.YamlConfig getSharedConfigForTest() {
            return getSharedConfig();
        }

        private Logger getSharedLoggerForTest() {
            return getSharedLogger();
        }

        private Scheduler getSharedSchedulerForTest() {
            return getSharedScheduler();
        }

        private RuntimeSnapshot requireSnapshot(@Nullable RuntimeSnapshot snapshot, String phase) {
            if (snapshot == null) {
                throw new IllegalStateException("Missing snapshot for " + phase);
            }

            return snapshot;
        }
    }
}
