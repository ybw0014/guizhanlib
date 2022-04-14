package net.guizhanss.guizhanlib.minecraft.helper.block;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.block.Biome;

import javax.annotation.Nonnull;

/**
 * 生物群系({@link Biome})
 *
 * @author ybw0014
 */
@UtilityClass
public class BiomeHelper {
    /**
     * 返回生物群系({@link Biome})的中文名
     *
     * @param biome {@link Biome} 生物群系
     * @return 生物群系的中文名,如果获取失败则返回键名
     */
    public static @Nonnull String getName(@Nonnull Biome biome) {
        return LanguageHelper.getLangOrKey(getKey(biome));
    }

    /**
     * 获取生物群系({@link Biome})的键名
     *
     * @param biome {@link Biome} 生物群系
     * @return 生物群系的键名
     */
    public static @Nonnull String getKey(@Nonnull Biome biome) {
        Validate.notNull(biome, "生物群系不能为空");

        return "biome.minecraft." + biome.toString().toLowerCase();
    }
}
