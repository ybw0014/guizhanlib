package net.guizhanss.guizhanlib.minecraft.helper.attribute;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;

import javax.annotation.Nonnull;

/**
 * 属性({@link Attribute})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("unused")
public final class AttributeHelper {
    /**
     * 返回属性({@link Attribute})的中文名
     *
     * @param attribute
     *     {@link Attribute} 属性
     *
     * @return 属性的中文名, 如果获取失败则返回键名
     */
    @Nonnull
    public static String getName(@Nonnull Attribute attribute) {
        return LanguageHelper.getLangOrKey(getKey(attribute));
    }

    /**
     * 获取属性({@link Biome})的键名
     *
     * @param attribute
     *     {@link Biome} 属性
     *
     * @return 属性的键名
     */
    @Nonnull
    public static String getKey(@Nonnull Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "属性不能为空");

        return "attribute.name." + attribute.getKey().getKey();
    }
}
