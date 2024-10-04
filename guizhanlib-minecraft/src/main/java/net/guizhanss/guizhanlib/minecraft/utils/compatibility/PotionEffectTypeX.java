package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * This class holds {@link PotionEffectType} that are renamed in 1.20.5.
 */
@SuppressWarnings("unused")
@UtilityClass
public class PotionEffectTypeX {

    public static final PotionEffectType SLOWNESS;
    public static final PotionEffectType HASTE;
    public static final PotionEffectType MINING_FATIGUE;
    public static final PotionEffectType STRENGTH;
    public static final PotionEffectType INSTANT_HEALTH;
    public static final PotionEffectType INSTANT_DAMAGE;
    public static final PotionEffectType JUMP_BOOST;
    public static final PotionEffectType NAUSEA;
    public static final PotionEffectType RESISTANCE;

    static {
        // EFFECT registry was added in 1.20.3, use legacy field access before that
        boolean isAtLeast1_20_3 = MinecraftVersionUtil.isAtLeast(20, 3);

        SLOWNESS = isAtLeast1_20_3 ? getByKey("slowness") : getField("SLOW");
        HASTE = isAtLeast1_20_3 ? getByKey("haste") : getField("FAST_DIGGING");
        MINING_FATIGUE = isAtLeast1_20_3 ? getByKey("mining_fatigue") : getField("SLOW_DIGGING");
        STRENGTH = isAtLeast1_20_3 ? getByKey("strength") : getField("INCREASE_DAMAGE");
        INSTANT_HEALTH = isAtLeast1_20_3 ? getByKey("instant_health") : getField("HEAL");
        INSTANT_DAMAGE = isAtLeast1_20_3 ? getByKey("instant_damage") : getField("HARM");
        JUMP_BOOST = isAtLeast1_20_3 ? getByKey("jump_boost") : getField("JUMP");
        NAUSEA = isAtLeast1_20_3 ? getByKey("nausea") : getField("CONFUSION");
        RESISTANCE = isAtLeast1_20_3 ? getByKey("resistance") : getField("DAMAGE_RESISTANCE");
    }

    @Nullable
    private static PotionEffectType getByKey(@Nonnull String key) {
        return Registry.EFFECT.get(NamespacedKey.minecraft(key));
    }

    @Nullable
    private static PotionEffectType getField(@Nonnull String key) {
        try {
            Field field = PotionEffectType.class.getDeclaredField(key);
            return (PotionEffectType) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
