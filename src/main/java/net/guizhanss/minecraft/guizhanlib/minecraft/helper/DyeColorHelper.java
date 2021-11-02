package net.guizhanss.minecraft.guizhanlib.minecraft.helper;

import lombok.experimental.UtilityClass;
import net.guizhanss.minecraft.guizhanlib.minecraft.LanguageHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;

import javax.annotation.Nonnull;

/**
 * 染料颜色({@link DyeColor})
 *
 * @author ybw0014
 */
@UtilityClass
public class DyeColorHelper {
    /**
     * 返回染料颜色({@link DyeColor})的中文名
     *
     * @param dyeColor {@link DyeColor} 生物群系
     * @return 生物群系的中文名,如果获取失败则返回键名
     */
    public static @Nonnull String getDyeColorName(@Nonnull DyeColor dyeColor) {
        return LanguageHelper.getLang(getDyeColorKey(dyeColor));
    }

    /**
     * 获取染料颜色({@link DyeColor})的键名
     *
     * @param dyeColor {@link DyeColor} 生物群系
     * @return 生物群系的键名
     */
    public static @Nonnull String getDyeColorKey(@Nonnull DyeColor dyeColor) {
        Validate.notNull(dyeColor, "染料颜色不能为空");

        return "color.minecraft." + dyeColor.toString().toLowerCase();
    }
}
