package net.guizhanss.guizhanlib.minecraft.config;

import net.guizhanss.guizhanlib.common.utils.StringUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * An extended {@link YamlConfiguration} that preserves comments and provides more features.
 *
 * @author ybw0014
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"ConstantConditions", "unused"})
public final class YamlConfig extends YamlConfiguration {

    private final YamlConfiguration defaults = new YamlConfiguration();
    private final Map<String, String> comments = new HashMap<>();
    private final Plugin plugin;
    private final File file;

    /**
     * Initialize a new {@link YamlConfig} instance.
     *
     * @param plugin the plugin instance
     * @param path   the path to the config file, relative to the plugin's data folder
     */
    @ParametersAreNonnullByDefault
    public YamlConfig(Plugin plugin, String path) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), path);
        super.defaults = defaults;
        loadDefaults(path);
    }

    /**
     * Get an integer field within a specific range, with a default value of 0.
     * <p>
     * The value will be corrected to the nearest boundary if it is out of range, but not saved.
     *
     * @param path The field path
     * @param min  The minimum value
     * @param max  The maximum value
     * @return The value of the field
     */
    public int getInt(String path, int min, int max) {
        return getInt(path, min, 0, max);
    }

    /**
     * Get an integer field within a specific range, with a default value.
     * <p>
     * The value will be corrected to the nearest boundary if it is out of range, but not saved.
     *
     * @param path       The field path
     * @param min        The minimum value
     * @param defaultVal The default value
     * @param max        The maximum value
     * @return The value of the field
     */
    public int getInt(String path, int min, int defaultVal, int max) {
        int val = getInt(path, defaultVal);
        if (val < min) {
            val = min;
            set(path, val);
        } else if (val > max) {
            val = max;
            set(path, val);
        }
        return val;
    }

    /**
     * Get a double field within a specific range, with a default value of 0.
     * <p>
     * The value will be corrected to the nearest boundary if it is out of range, but not saved.
     *
     * @param path The field path
     * @param min  The minimum value
     * @param max  The maximum value
     * @return The value of the field
     */
    public double getDouble(String path, double min, double max) {
        return getDouble(path, min, 0d, max);
    }

    /**
     * Get a double field within a specific range, with a default value.
     * <p>
     * The value will be corrected to the nearest boundary if it is out of range, but not saved.
     *
     * @param path       The field path
     * @param min        The minimum value
     * @param defaultVal The default value
     * @param max        The maximum value
     * @return The value of the field
     */
    public double getDouble(String path, double min, double defaultVal, double max) {
        double val = getDouble(path, defaultVal);
        if (val < min) {
            val = min;
            set(path, val);
        } else if (val > max) {
            val = max;
            set(path, val);
        }
        return val;
    }

    /**
     * Removes unused/old keys from the users config.
     */
    public void removeUnusedKeys() {
        for (String key : getKeys(true)) {
            if (!defaults.contains(key)) {
                set(key, null);
            }
        }
    }

    /**
     * Adds the missing keys from the default config to the users config.
     */
    public void addMissingKeys() {
        for (String key : defaults.getKeys(true)) {
            if (!contains(key)) {
                set(key, defaults.get(key));
            }
        }
    }

    /**
     * Set a default value if the field does not exist in config.
     *
     * @param path The field path
     * @param obj  The default value
     */
    public void setDefault(String path, @Nullable Object obj) {
        if (!contains(path)) {
            set(path, obj);
        }
    }

    /**
     * Save the config to file.
     */
    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, e, () -> "An error occurred while saving config file: " + file.getName());
        }
    }

    public void reload() {
        if (file.exists()) {
            try {
                load(file);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, e, () -> "An error occurred while loading config file: " + file.getName());
            }
        }
        save();
    }

    @Nonnull
    @Override
    public YamlConfiguration getDefaults() {
        return defaults;
    }

    @Nullable
    String getComment(String key) {
        return comments.get(key);
    }

    @Nonnull
    @Override
    protected String buildHeader() {
        return "";
    }

    @Nonnull
    @Override
    public String saveToString() {
        options().copyDefaults(true).copyHeader(false).indent(2);
        String defaultSave = super.saveToString();

        try {
            String[] lines = defaultSave.split("\n");
            StringBuilder save = new StringBuilder();
            PathBuilder pathBuilder = new PathBuilder();

            for (String line : lines) {
                if (line.contains(":")) {
                    String comment = getComment(pathBuilder.append(line).build());
                    if (comment != null) {
                        save.append(comment);
                    }
                }
                save.append(line).append('\n');
            }
            return save.toString();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, e, () -> "An error occurred while parsing config for saving: " + file.getName() + ". Using default save instead.");
            return defaultSave;
        }
    }

    @ParametersAreNonnullByDefault
    private void loadDefaults(String name) {
        InputStream stream = plugin.getResource(name);

        if (stream == null) {
            throw new IllegalStateException("No default config found for " + name + "!");
        } else {
            try {
                String def = readDefaults(stream);
                defaults.loadFromString(def);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, e, () -> "An error occurred while loading default config for: " + name);
            }
        }

        reload();
    }

    @Nonnull
    private String readDefaults(@Nonnull InputStream inputStream) throws IOException {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder yamlBuilder = new StringBuilder();
            StringBuilder commentBuilder = new StringBuilder("\n");
            PathBuilder pathBuilder = new PathBuilder();
            String line;

            while ((line = input.readLine()) != null) {
                yamlBuilder.append(line).append('\n');

                if (StringUtil.isBlank(line)) {
                    // Skip
                    continue;
                }

                if (line.contains("#")) {
                    // Add to comment of next path
                    commentBuilder.append(line).append('\n');
                    continue;
                }

                if (line.contains(":")) {
                    // Its part of a path
                    pathBuilder.append(line);
                } else {
                    continue;
                }

                if (commentBuilder.length() != 1) {
                    // Add the comment to the path and clear
                    comments.put(pathBuilder.build(), commentBuilder.toString());
                    commentBuilder = new StringBuilder("\n");
                } else if (pathBuilder.inMainSection()) {
                    // The main section should always have spaces between keys
                    comments.put(pathBuilder.build(), "\n");
                }
            }

            return yamlBuilder.toString();
        }
    }
}
