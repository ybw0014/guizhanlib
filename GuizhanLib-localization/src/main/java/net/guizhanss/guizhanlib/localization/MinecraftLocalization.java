package net.guizhanss.guizhanlib.localization;

import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * This is an extended {@link Localization} class that overrides
 * some methods to be helpful in minecraft plugin development.
 *
 * @author ybw0014
 */
public class MinecraftLocalization extends Localization {
    /**
     * Constructor
     *
     * @param plugin The {@link JavaPlugin} instance
     */
    @ParametersAreNonnullByDefault
    public MinecraftLocalization(JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Constructor
     *
     * @param plugin     The {@link JavaPlugin} instance
     * @param folderName The name of the folder that holds all language files
     */
    @ParametersAreNonnullByDefault
    public MinecraftLocalization(JavaPlugin plugin, String folderName) {
        super(plugin, folderName);
    }

    /**
     * Constructor
     *
     * @param plugin     The {@link JavaPlugin} instance
     * @param folderName The name of the folder that holds all language files
     * @param langFile   The name of language file (without file extension .yml)
     */
    @ParametersAreNonnullByDefault
    public MinecraftLocalization(JavaPlugin plugin, String folderName, String langFile) {
        super(plugin, folderName, langFile);
    }

    /**
     * Get colored and localized {@link String} from path
     *
     * @param path the localization path
     * @return Colored and localized {@link String}, empty if string is not found
     */
    @Nonnull
    @Override
    public String getString(@Nonnull String path) {
        return ChatUtil.color(super.getString(path));
    }

    /**
     * Get colored and localized {@link String} {@link List} from path
     *
     * @param path the localization path
     * @return Colored and localized {@link String} {@link List}
     */
    @Nonnull
    @Override
    public List<String> getStringList(@Nonnull String path) {
        return ChatUtil.color(super.getStringList(path));
    }

    /**
     * Get colored and localized {@link String} array from path
     *
     * @param path the localization path
     * @return Colored and localized {@link String} array
     */
    @Nonnull
    @Override
    public String[] getStringArray(@Nonnull String path) {
        return getStringList(path).toArray(new String[0]);
    }
}
