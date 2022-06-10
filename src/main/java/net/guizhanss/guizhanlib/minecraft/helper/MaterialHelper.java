package net.guizhanss.guizhanlib.minecraft.helper;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * 物品材料({@link Material})
 *
 * @author ybw0014
 */
@UtilityClass
public class MaterialHelper {
    /**
     * 返回材料({@link Material})的中文名称
     *
     * @param mat {@link Material} 材料
     * @return 材料的中文名称，如果获取失败则返回对应的键名
     */
    @Nonnull
    public static String getName(@Nonnull Material mat) {
        return LanguageHelper.getLangOrKey(getKey(mat));
    }

    /**
     * 获取物品材料({@link Material})的键名
     *
     * @param mat {@link Material} 物品材料
     * @return 物品材料的键名
     */
    @Nonnull
    public static String getKey(@Nonnull Material mat) {
        Preconditions.checkNotNull(mat, "材料不能为空");

        return (mat.isBlock() ? "block" : "item") + "."
            + mat.getKey().getNamespace() + "."
            + mat.getKey().getKey();
    }

    /**
     * 根据给定的材料字符串返回中文名称
     *
     * @param material 材料的{@link String}格式
     *
     * @return 物品材料的中文名称，如果获取失败则返回材料名可读格式
     */
    @Nonnull
    public static String getName(@Nonnull String material) {
        return getName(material, false);
    }

    /**
     * 根据给定的材料字符串返回中文名称
     *
     * @param material 材料的{@link String}格式
     * @param emptyString 是否返回空字符串
     *
     * @return 物品材料的中文名称，如果获取失败则返回材料名可读格式或空字符串
     */
    @Nonnull
    public static String getName(@Nonnull String material, boolean emptyString) {
        try {
            Material mat = Material.valueOf(material);
            return getName(mat);
        } catch (IllegalArgumentException ex) {
            if (emptyString) {
                return StringUtil.humanize(material);
            } else {
                return "";
            }
        }
    }
}
