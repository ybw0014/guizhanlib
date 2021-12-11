package net.guizhanss.guizhanlib.minecraft.helper.potion;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
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

        String key = type.getName().toLowerCase();

        // 语言文件与PotionEffectType不一致的情况
        if (type == PotionEffectType.CONFUSION) {
            key = "nausea";
        } else if (type == PotionEffectType.DAMAGE_RESISTANCE) {
            key = "resistance";
        } else if (type == PotionEffectType.FAST_DIGGING) {
            key = "haste";
        } else if (type == PotionEffectType.HARM) {
            key = "instant_damage";
        } else if (type == PotionEffectType.HEAL) {
            key = "instant_health";
        } else if (type == PotionEffectType.INCREASE_DAMAGE) {
            key = "strength";
        } else if (type == PotionEffectType.JUMP) {
            key = "jump_boost";
        } else if (type == PotionEffectType.SLOW) {
            key = "slowness";
        } else if (type == PotionEffectType.SLOW_DIGGING) {
            key = "mining_fatigue";
        }

        return "effect.minecraft." + key;
    }
}
