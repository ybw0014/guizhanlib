package net.guizhanss.minecraft.guizhanlib.minecraft.helper.potion;

import lombok.experimental.UtilityClass;
import net.guizhanss.minecraft.guizhanlib.minecraft.LanguageHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

/**
 * 药水效果({@link PotionEffectType})
 *
 * @author ybw0014
 */
@UtilityClass
public class PotionEffectTypeHelper {
    /**
     * 返回药水效果({@link PotionEffectType})的中文名
     *
     * @param type {@link PotionEffectType} 药水效果
     * @return 药水效果的中文名,如果获取失败则返回键名
     */
    public static @Nonnull String getName(@Nonnull PotionEffectType type) {
        return LanguageHelper.getLang(getKey(type));
    }

    /**
     * 获取药水效果({@link PotionEffectType}的键名
     *
     * @param type {@link PotionEffectType} 药水效果
     * @return 药水效果的键名
     */
    public static @Nonnull String getKey(@Nonnull PotionEffectType type) {
        Validate.notNull(type, "药水效果不能为空");

        return "effect.minecraft." + type.toString().toLowerCase();
    }
}
