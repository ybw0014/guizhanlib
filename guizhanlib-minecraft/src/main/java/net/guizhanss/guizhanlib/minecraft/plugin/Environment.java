package net.guizhanss.guizhanlib.minecraft.plugin;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

public enum Environment {
    LIVE,
    TEST;

    private static final String LEGACY_MOCK_BUKKIT_SERVER = "be.seeseemelk.mockbukkit.ServerMock";
    private static final String MODERN_MOCK_BUKKIT_SERVER = "org.mockbukkit.mockbukkit.ServerMock";

    @Nonnull
    public static Environment detect() {
        return detect(getServerClassName(), Thread.currentThread().getStackTrace());
    }

    public static boolean isTest() {
        return detect() == TEST;
    }

    @Nonnull
    public static Environment detect(@Nullable String serverClassName, StackTraceElement[] stackTrace) {
        if (isMockBukkitServer(serverClassName)) {
            return TEST;
        }

        if (isTestStackTrace(stackTrace)) {
            return TEST;
        }

        return LIVE;
    }

    @Nullable
    private static String getServerClassName() {
        try {
            if (Bukkit.getServer() == null) {
                return null;
            }

            return Bukkit.getServer().getClass().getName();
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private static boolean isMockBukkitServer(@Nullable String serverClassName) {
        return LEGACY_MOCK_BUKKIT_SERVER.equals(serverClassName)
            || MODERN_MOCK_BUKKIT_SERVER.equals(serverClassName);
    }

    private static boolean isTestStackTrace(StackTraceElement[] stackTrace) {
        boolean mockBukkitDetected = false;
        boolean junitDetected = false;

        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName().toLowerCase(Locale.ROOT);
            if (className.contains("mockbukkit")) {
                mockBukkitDetected = true;
            }
            if (className.startsWith("org.junit.") || className.startsWith("junit.")) {
                junitDetected = true;
            }

            if (mockBukkitDetected && junitDetected) {
                return true;
            }
        }

        return false;
    }
}
