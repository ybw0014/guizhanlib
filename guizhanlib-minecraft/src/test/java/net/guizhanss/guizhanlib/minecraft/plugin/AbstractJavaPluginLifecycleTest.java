package net.guizhanss.guizhanlib.minecraft.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.plugin.PluginManagerMock;

import java.util.List;

class AbstractJavaPluginLifecycleTest {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void onLoadDoesNotCreateRuntimeState() {
        AbstractJavaPluginTestPlugin plugin = loadOnly("OnLoadOnlyPlugin", Environment.TEST, AbstractJavaPluginTestPlugin.FailureStage.NONE);

        Assertions.assertEquals(List.of("load"), plugin.getLifecycleEvents());
        Assertions.assertEquals(
            new AbstractJavaPluginTestPlugin.RuntimeSnapshot(false, false, false, false),
            plugin.getLoadSnapshot()
        );
        Assertions.assertFalse(plugin.isRuntimeStateCreated());
        Assertions.assertFalse(plugin.isAutoUpdateEnabledForTest());
        Assertions.assertFalse(plugin.isInstanceAssignedForTest());
        Assertions.assertFalse(plugin.isEnabled());
        Assertions.assertTrue(Bukkit.getScheduler().getPendingTasks().isEmpty());
    }

    @Test
    void preservesLifecycleOrderAndTimingInLiveMode() {
        AbstractJavaPluginTestPlugin plugin = loadAndEnable("LifecycleLivePlugin", Environment.LIVE, AbstractJavaPluginTestPlugin.FailureStage.NONE);

        Assertions.assertEquals(List.of("load", "auto-update", "enable"), plugin.getLifecycleEvents());
        Assertions.assertEquals(
            new AbstractJavaPluginTestPlugin.RuntimeSnapshot(false, false, false, false),
            plugin.getLoadSnapshot()
        );
        Assertions.assertEquals(
            new AbstractJavaPluginTestPlugin.RuntimeSnapshot(true, true, false, true),
            plugin.getAutoUpdateSnapshot()
        );
        Assertions.assertEquals(
            new AbstractJavaPluginTestPlugin.RuntimeSnapshot(true, true, true, true),
            plugin.getEnableSnapshot()
        );
        Assertions.assertTrue(plugin.isRuntimeStateCreated());
        Assertions.assertTrue(plugin.isAutoUpdateEnabledForTest());
        Assertions.assertTrue(plugin.isInstanceAssignedForTest());

        disable(plugin);

        Assertions.assertEquals(List.of("load", "auto-update", "enable", "disable"), plugin.getLifecycleEvents());
        Assertions.assertEquals(
            new AbstractJavaPluginTestPlugin.RuntimeSnapshot(true, true, true, true),
            plugin.getDisableSnapshot()
        );
        Assertions.assertFalse(plugin.isRuntimeStateCreated());
        Assertions.assertFalse(plugin.isAutoUpdateEnabledForTest());
        Assertions.assertFalse(plugin.isInstanceAssignedForTest());
        Assertions.assertTrue(Bukkit.getScheduler().getPendingTasks().isEmpty());
    }

    @Test
    void testingEnvironmentRethrowsLifecycleExceptions() {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> loadOnly("LoadFailurePlugin", Environment.TEST, AbstractJavaPluginTestPlugin.FailureStage.LOAD)
        );

        Assertions.assertThrows(
            IllegalStateException.class,
            () -> loadAndEnable("EnableFailurePlugin", Environment.TEST, AbstractJavaPluginTestPlugin.FailureStage.ENABLE)
        );

        AbstractJavaPluginTestPlugin plugin = loadAndEnable(
            "DisableFailurePlugin",
            Environment.TEST,
            AbstractJavaPluginTestPlugin.FailureStage.DISABLE
        );

        Assertions.assertThrows(IllegalStateException.class, () -> disable(plugin));
    }

    @Test
    void liveEnvironmentSwallowsLifecycleExceptionsAndResetsOnDisable() {
        Assertions.assertDoesNotThrow(
            () -> loadOnly("LiveLoadFailurePlugin", Environment.LIVE, AbstractJavaPluginTestPlugin.FailureStage.LOAD)
        );

        AbstractJavaPluginTestPlugin enableFailurePlugin = Assertions.assertDoesNotThrow(
            () -> loadAndEnable("LiveEnableFailurePlugin", Environment.LIVE, AbstractJavaPluginTestPlugin.FailureStage.ENABLE)
        );
        Assertions.assertTrue(enableFailurePlugin.isEnabled());
        Assertions.assertTrue(enableFailurePlugin.isRuntimeStateCreated());

        AbstractJavaPluginTestPlugin disableFailurePlugin = loadAndEnable(
            "LiveDisableFailurePlugin",
            Environment.LIVE,
            AbstractJavaPluginTestPlugin.FailureStage.DISABLE
        );

        Assertions.assertDoesNotThrow(() -> disable(disableFailurePlugin));
        Assertions.assertFalse(disableFailurePlugin.isRuntimeStateCreated());
        Assertions.assertFalse(disableFailurePlugin.isInstanceAssignedForTest());
        Assertions.assertTrue(Bukkit.getScheduler().getPendingTasks().isEmpty());
    }

    private AbstractJavaPluginTestPlugin loadOnly(
        String pluginName,
        Environment environment,
        AbstractJavaPluginTestPlugin.FailureStage failureStage
    ) {
        PluginDescriptionFile description = new PluginDescriptionFile(
            pluginName,
            "1.0.0",
            AbstractJavaPluginTestPlugin.class.getName()
        );
        PluginManagerMock pluginManager = server.getPluginManager();

        return (AbstractJavaPluginTestPlugin) pluginManager.loadPlugin(
            AbstractJavaPluginTestPlugin.class,
            description,
            new Object[]{environment.name(), failureStage.name()}
        );
    }

    private AbstractJavaPluginTestPlugin loadAndEnable(
        String pluginName,
        Environment environment,
        AbstractJavaPluginTestPlugin.FailureStage failureStage
    ) {
        AbstractJavaPluginTestPlugin plugin = loadOnly(pluginName, environment, failureStage);
        server.getPluginManager().enablePlugin(plugin);
        return plugin;
    }

    private void disable(AbstractJavaPluginTestPlugin plugin) {
        server.getPluginManager().disablePlugin(plugin);
    }
}
