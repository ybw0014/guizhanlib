package net.guizhanss.guizhanlib.minecraft.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods about Minecraft versions.
 */
@UtilityClass
public class MinecraftVersionUtil {

    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?");

    /**
     * Check if the server's current minecraft version is at least the specified version (inclusive).
     * <p>
     * This method supports both semantic versioning (1.16, 1.18.2, 1.21.11) and
     * future year-based versions (26.1, 26.1.1, 26.2, 26.2.3, 27.1).
     *
     * @param major The major version.
     * @param minor The minor version.
     * @param patch The patch version.
     * @return Whether the server's current version is at least the specified version.
     */
    public static boolean isAtLeast(int major, int minor, int patch) {
        String currentVersion = Bukkit.getServer().getMinecraftVersion();
        Matcher matcher = VERSION_PATTERN.matcher(currentVersion);

        if (!matcher.find()) {
            return false;
        }

        int currentMajor = Integer.parseInt(matcher.group(1));
        int currentMinor = Integer.parseInt(matcher.group(2));
        int currentPatch = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;

        if (currentMajor != major) {
            return currentMajor > major;
        }
        if (currentMinor != minor) {
            return currentMinor > minor;
        }
        return currentPatch >= patch;
    }

    /**
     * Check if the server's current minecraft version is at least the specified version (inclusive).
     *
     * @param major The major version.
     * @param minor The minor version.
     * @return Whether the server's current version is at least the specified version.
     */
    public static boolean isAtLeast(int major, int minor) {
        // compatibility for old 1.x.x versions
        if (major > 1 && major <= 21) {
            return isAtLeast(1, major, minor);
        }
        return isAtLeast(major, minor, 0);
    }

    /**
     * Check if the sever's current minecraft version is at least the specified version (inclusive).
     *
     * @param major The major version.
     * @return Whether the server's current version is at least the specified version.
     * @deprecated Use {@link #isAtLeast(int, int)} or {@link #isAtLeast(int, int, int)} instead.
     */
    @Deprecated(since = "2.5.0", forRemoval = true)
    public static boolean isAtLeast(int major) {
        return isAtLeast(major, 0);
    }

    /**
     * Check if the server's current minecraft version is before the specified version (exclusive).
     *
     * @param major The major version.
     * @param minor The minor version.
     * @param patch The patch version.
     * @return Whether the server's current version is before the specified version.
     */
    public static boolean isBefore(int major, int minor, int patch) {
        return !isAtLeast(major, minor, patch);
    }

    /**
     * Check if the server's current minecraft version is before the specified version (exclusive).
     *
     * @param major The major version.
     * @param minor The minor version.
     * @return Whether the server's current version is before the specified version.
     */
    public static boolean isBefore(int major, int minor) {
        // compatibility for old 1.x.x versions
        if (major > 1 && major <= 21) {
            return isBefore(1, major, minor);
        }
        return isBefore(major, minor, 0);
    }

    /**
     * Check if the sever's current minecraft version is before the specified version (exclusive).
     *
     * @param minor The major version.
     * @return Whether the server's current version is before the specified version.
     * @deprecated Use {@link #isBefore(int, int)} or {@link #isBefore(int, int, int)} instead.
     */
    @Deprecated(since = "2.5.0", forRemoval = true)
    public static boolean isBefore(int minor) {
        return isBefore(minor, 0);
    }
}
