package net.guizhanss.guizhanlib.localization;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Localization service.
 * Should be initialized after loading config file and before registering items.
 *
 * Localization service will create a folder "lang" (by default) under plugin's data folder,
 *
 * @author ybw0014
 */
public class Localization {

    private boolean initialized = false;

    private final JavaPlugin plugin;
    private final File langFolder;
    private final List<String> languages = new LinkedList<>();
    private final Map<String, Language> langMap = new LinkedHashMap<>();

    /**
     * Constructor
     *
     * @param plugin The {@link JavaPlugin} instance
     */
    @ParametersAreNonnullByDefault
    public Localization(JavaPlugin plugin) {
        this(plugin, "lang");
    }

    /**
     * Constructor
     *
     * @param plugin The {@link JavaPlugin} instance
     * @param folderName The name of the folder that holds all language files
     */
    @ParametersAreNonnullByDefault
    public Localization(JavaPlugin plugin, String folderName) {
        Validate.notNull(plugin, "The plugin instance should not be null");
        Validate.notNull(folderName, "The folder name should not be null");

        this.plugin = plugin;

        // Check data folder
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        // Check language folder
        langFolder = new File(plugin.getDataFolder(), "/" + folderName);
        if (!langFolder.exists()) {
            langFolder.mkdir();
        }

        this.initialized = true;
    }

    /**
     * Constructor
     *
     * @param plugin The {@link JavaPlugin} instance
     * @param folderName The name of the folder that holds all language files
     * @param langFile The name of language file (without file extension .yml)
     */
    @ParametersAreNonnullByDefault
    public Localization(JavaPlugin plugin, String folderName, String langFile) {
        this(plugin, folderName);
        addLanguage(langFile);
    }

    /**
     * Load a language file to configuration
     *
     * @param langFilename the filename of language file without extension .yml
     *
     * @throws IllegalStateException when language file does not exist
     */
    public void addLanguage(@Nonnull String langFilename) {
        Validate.notNull(langFilename, "The language file name should not be null");

        File langFile = new File(langFolder, langFilename + ".yml");

        languages.add(langFilename);
        langMap.put(langFilename, new Language(langFilename, langFile));
    }

    /**
     * Get localized {@link String} from path
     *
     * @param path the localization path
     *
     * @return Localized {@link String}
     */
    public @Nonnull String getString(@Nonnull String path) {
        String localization = getStringOrNull(path);
        return localization != null ? localization : path;
    }

    /**
     * Get localized {@link String} {@link List} from path
     *
     * @param path the localization path
     *
     * @return Localized {@link String} {@link List}
     */
    public @Nonnull List<String> getStringList(@Nonnull String path) {
        List<String> localization = getStringListOrNull(path);
        return localization != null ? localization : Collections.singletonList(path);
    }

    private @Nullable String getStringOrNull(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null");
        if (!initialized) {
            throw new IllegalStateException("Localization service is not initialized");
        }

        for (String lang : languages) {
            String localization = langMap.get(lang).getFile().getString(path);
            if (localization != null) {
                return localization;
            }
        }
        return null;
    }

    private @Nullable List<String> getStringListOrNull(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null");
        if (!initialized) {
            throw new IllegalStateException("Localization service is not initialized");
        }

        for (String lang : languages) {
            List<String> localization = langMap.get(lang).getFile().getStringList(path);
            if (!localization.isEmpty()) {
                return localization;
            }
        }
        return null;
    }
}
