package net.guizhanss.guizhanlib.minecraft.helper.potion;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

/**
 * 药水效果({@link PotionEffectType})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("unused")
public final class PotionEffectTypeHelper {
    /**
     * 返回药水效果({@link PotionEffectType})的中文名
     *
     * @param type {@link PotionEffectType} 药水效果
     * @return 药水效果的中文名, 如果获取失败则返回键名
     */
    @Nonnull
    public static String getName(@Nonnull PotionEffectType type) {
        return LanguageHelper.getLangOrKey(getKey(type));
    }

    /**
     * 获取药水效果({@link PotionEffectType}的键名
     *
     * @param type {@link PotionEffectType} 药水效果
     * @return 药水效果的键名
     */
    @Nonnull
    public static String getKey(@Nonnull PotionEffectType type) {
        Preconditions.checkArgument(type != null, "药水效果不能为空");

        String key = type.getKey().getKey();

        return "effect.minecraft." + key;
    }
}
