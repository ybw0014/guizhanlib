package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class holds {@link Enchantment} that are renamed in 1.20.5.
 */
@UtilityClass
public class EnchantmentX {

    public static final Enchantment PROTECTION;
    public static final Enchantment FIRE_PROTECTION;
    public static final Enchantment FEATHER_FALLING;
    public static final Enchantment BLAST_PROTECTION;
    public static final Enchantment PROJECTILE_PROTECTION;
    public static final Enchantment RESPIRATION;
    public static final Enchantment AQUA_AFFINITY;
    public static final Enchantment SHARPNESS;
    public static final Enchantment SMITE;
    public static final Enchantment BANE_OF_ARTHROPODS;
    public static final Enchantment LOOTING;
    public static final Enchantment EFFICIENCY;
    public static final Enchantment UNBREAKING;
    public static final Enchantment FORTUNE;
    public static final Enchantment POWER;
    public static final Enchantment PUNCH;
    public static final Enchantment FLAME;
    public static final Enchantment INFINITY;
    public static final Enchantment LUCK_OF_THE_SEA;
    public static final Enchantment SWEEPING_EDGE;

    static {
        PROTECTION = getByKey("protection");
        FIRE_PROTECTION = getByKey("fire_protection");
        FEATHER_FALLING = getByKey("feather_falling");
        BLAST_PROTECTION = getByKey("blast_protection");
        PROJECTILE_PROTECTION = getByKey("projectile_protection");
        RESPIRATION = getByKey("respiration");
        AQUA_AFFINITY = getByKey("aqua_affinity");
        SHARPNESS = getByKey("sharpness");
        SMITE = getByKey("smite");
        BANE_OF_ARTHROPODS = getByKey("bane_of_arthropods");
        LOOTING = getByKey("looting");
        EFFICIENCY = getByKey("efficiency");
        UNBREAKING = getByKey("unbreaking");
        FORTUNE = getByKey("fortune");
        POWER = getByKey("power");
        PUNCH = getByKey("punch");
        FLAME = getByKey("flame");
        INFINITY = getByKey("infinity");
        LUCK_OF_THE_SEA = getByKey("luck_of_the_sea");
        SWEEPING_EDGE = getByKey("sweeping_edge");
    }

    @Nullable
    private static Enchantment getByKey(@Nonnull String key) {
        return Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key));
    }
}
