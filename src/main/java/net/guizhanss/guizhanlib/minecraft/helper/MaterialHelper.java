package net.guizhanss.guizhanlib.minecraft.helper;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import org.apache.commons.lang.Validate;
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
    public static @Nonnull String getName(@Nonnull Material mat) {
        return LanguageHelper.getLang(getKey(mat));
    }

    /**
     * 获取物品材料({@link Material})的键名
     *
     * @param mat {@link Material} 物品材料
     * @return 物品材料的键名
     */
    public static @Nonnull String getKey(@Nonnull Material mat) {
        Validate.notNull(mat, "材料不能为空");

        return (mat.isBlock() ? "block" : "item") + "."
            + mat.getKey().getNamespace() + "."
            + mat.getKey().getKey();
    }

    /**
     * 根据给定的材料字符串返回中文名称
     * @param material 材料的{@link String}格式
     * @return 物品材料的中文名称，如果获取失败则返回对应的键名
     */
    public static @Nonnull String getNameByString(@Nonnull String material) {
        try {
            Material mat = Material.valueOf(material);
            return getName(mat);
        } catch (IllegalArgumentException ex) {
            return "";
        }
    }
}
