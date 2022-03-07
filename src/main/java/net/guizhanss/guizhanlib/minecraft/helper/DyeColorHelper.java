package net.guizhanss.guizhanlib.minecraft.helper;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import net.guizhanss.guizhanlib.utils.StringUtil;
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
    public static @Nonnull String getName(@Nonnull DyeColor dyeColor) {
        return LanguageHelper.getLang(getKey(dyeColor));
    }

    /**
     * 获取染料颜色({@link DyeColor})的键名
     *
     * @param dyeColor {@link DyeColor} 生物群系
     * @return 生物群系的键名
     */
    public static @Nonnull String getKey(@Nonnull DyeColor dyeColor) {
        Validate.notNull(dyeColor, "染料颜色不能为空");

        return "color.minecraft." + dyeColor.toString().toLowerCase();
    }

    /**
     * 根据给定的染料颜色字符串返回中文名称
     * @param dyeColor 染料颜色的{@link String}格式
     * @return 染料颜色的中文名称，如果获取失败则返回对应的键名
     */
    public static @Nonnull String getNameByString(@Nonnull String dyeColor) {
        try {
            DyeColor type = DyeColor.valueOf(StringUtil.dehumanize(dyeColor));
            return getName(type);
        } catch (IllegalArgumentException ex) {
            return "";
        }
    }
}
