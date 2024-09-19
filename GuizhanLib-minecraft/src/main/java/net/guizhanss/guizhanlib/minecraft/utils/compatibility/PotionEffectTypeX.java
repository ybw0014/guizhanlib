package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class holds {@link PotionEffectType} that are renamed in 1.20.5.
 */
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
        SLOWNESS = getByKey("slowness");
        HASTE = getByKey("haste");
        MINING_FATIGUE = getByKey("mining_fatigue");
        STRENGTH = getByKey("strength");
        INSTANT_HEALTH = getByKey("instant_health");
        INSTANT_DAMAGE = getByKey("instant_damage");
        JUMP_BOOST = getByKey("jump_boost");
        NAUSEA = getByKey("nausea");
        RESISTANCE = getByKey("resistance");
    }

    @Nullable
    private static PotionEffectType getByKey(@Nonnull String key) {
        return Registry.EFFECT.get(NamespacedKey.minecraft(key));
    }
}
