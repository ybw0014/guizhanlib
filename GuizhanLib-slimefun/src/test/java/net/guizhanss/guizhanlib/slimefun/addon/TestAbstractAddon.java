package net.guizhanss.guizhanlib.slimefun.addon;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test for {@link AbstractAddon}, modified from InfinityLib.
 *
 * @author Mooy1
 * @author ybw0014
 */
public class TestAbstractAddon {

    private static ServerMock server;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
        addon = MockBukkit.load(MockAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testNotNullEnvironment() {
        assertNotNull(addon.getEnvironment());
    }

    @Test
    void testNotNullConfig() {
        assertNotNull(addon.getConfig());
    }

    @Test
    void testCreateKey() {
        assertNotNull(MockAddon.createKey("test"));
    }

    @Test
    void testAutoUpdatedDisabled() {
        assertFalse(addon.isAutoUpdateEnabled());
    }

    @Test
    void testBugTrackerURL() {
        assertEquals("https://github.com/ybw0014/GuizhanLib/issues", addon.getBugTrackerURL());
    }

    @Test
    void testInstance() {
        assertDoesNotThrow((ThrowingSupplier<Object>) MockAddon::getInstance);
    }

    @Test
    void testDuplicateInstance() {
        assertThrows(RuntimeException.class, () -> MockBukkit.load(MockAddon.class));
    }

    void testSlimefunTickCount() {
        server.getScheduler().performOneTick();
        assertEquals(1, MockAddon.getSlimefunTickCount());
    }

    @Test
    void testNotRelocatedLive() {
        assertThrows(RuntimeException.class, () -> MockBukkit.load(MockAddon.class, Environment.LIVE, null));
    }
}
