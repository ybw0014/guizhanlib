package net.guizhanss.guizhanlib.localization;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;

/**
 * This class represents a {@link Language}, which holds the localization {@link FileConfiguration}.
 *
 * @author ybw0014
 * @see Localization
 */
@SuppressWarnings("ConstantConditions")
public final class Language {

    private final String lang;
    private final File currentFile;
    private final FileConfiguration currentConfig;

    /**
     * Constructor
     *
     * @param lang          the key of language
     * @param currentFile   the current language {@link File}
     * @param defaultConfig the {@link FileConfiguration} of default from resource
     */
    @ParametersAreNonnullByDefault
    public Language(String lang, File currentFile, FileConfiguration defaultConfig) {
        Preconditions.checkArgument(lang != null, "Language key cannot be null");
        Preconditions.checkArgument(currentFile != null, "Current file cannot be null");
        Preconditions.checkArgument(defaultConfig != null, "default config cannot be null");

        this.lang = lang;
        this.currentFile = currentFile;
        this.currentConfig = YamlConfiguration.loadConfiguration(currentFile);
        this.currentConfig.setDefaults(defaultConfig);
        save();
    }

    /**
     * Get language name
     *
     * @return The language name
     */
    @Nonnull
    public String getName() {
        return lang;
    }

    /**
     * Get current language {@link FileConfiguration}
     *
     * @return the language {@link FileConfiguration}
     */
    @Nonnull
    public FileConfiguration getLang() {
        return currentConfig;
    }

    /**
     * Save current lang file
     */
    public void save() {
        try {
            currentConfig.save(currentFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
