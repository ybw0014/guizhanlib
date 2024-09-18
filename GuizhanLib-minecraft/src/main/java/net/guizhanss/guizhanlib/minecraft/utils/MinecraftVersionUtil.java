package net.guizhanss.guizhanlib.minecraft.utils;

import io.papermc.lib.PaperLib;
import lombok.experimental.UtilityClass;

/**
 * Utility methods about Minecraft versions.
 */
@UtilityClass
public class MinecraftVersionUtil {

    /**
     * Check if the sever's current minecraft version is at least the specified version.
     *
     * @param minor The minor version. (actually major)
     * @param patch The patch version.
     * @return Whether the server's current version is at least the specified version.
     */
    public static boolean isAtLeast(int minor, int patch) {
        int currentMinor = PaperLib.getMinecraftVersion();
        int currentPatch = PaperLib.getMinecraftPatchVersion();
        return currentMinor > minor || (currentMinor == minor && currentPatch >= patch);
    }

    /**
     * Check if the sever's current minecraft version is at least the specified version.
     *
     * @param minor The minor version. (actually major)
     * @return Whether the server's current version is at least the specified version.
     */
    public static boolean isAtLeast(int minor) {
        return isAtLeast(minor, 0);
    }

    /**
     * Check if the sever's current minecraft version is before the specified version (exclusive).
     *
     * @param minor The minor version. (actually major)
     * @param patch The patch version.
     * @return Whether the server's current version is before the specified version.
     */
    public static boolean isBefore(int minor, int patch) {
        int currentMinor = PaperLib.getMinecraftVersion();
        int currentPatch = PaperLib.getMinecraftPatchVersion();
        return currentMinor < minor || (currentMinor == minor && currentPatch < patch);
    }

    /**
     * Check if the sever's current minecraft version is before the specified version (exclusive).
     *
     * @param minor The minor version. (actually major)
     * @return Whether the server's current version is before the specified version.
     */
    public static boolean isBefore(int minor) {
        return isBefore(minor, 0);
    }
}
