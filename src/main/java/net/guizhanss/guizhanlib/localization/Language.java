package net.guizhanss.guizhanlib.localization;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;

/**
 * 该类代表一个语言
 *
 * @author ybw0014
 *
 * @see Localization
 */
public final class Language {

    private final String lang;
    private final FileConfiguration file;

    @ParametersAreNonnullByDefault
    public Language(String lang, File file) {
        Validate.notNull(lang, "语言名称不能为空");
        Validate.notNull(file, "语言文件不能为空");

        this.lang = lang;

        if (!file.exists()) {
            throw new IllegalArgumentException("语言文件不存在");
        }

        this.file = YamlConfiguration.loadConfiguration(file);
    }

    public @Nonnull String getName() {
        return lang;
    }

    public FileConfiguration getFile() {
        return file;
    }
}
