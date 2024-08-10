package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

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
        boolean isAtLeast1_20_5 = MinecraftVersionUtil.isAtLeast(20, 5);

        PROTECTION = isAtLeast1_20_5 ? Enchantment.PROTECTION : getKey("PROTECTION_ENVIRONMENTAL");
        FIRE_PROTECTION = isAtLeast1_20_5 ? Enchantment.FIRE_PROTECTION : getKey("PROTECTION_FIRE");
        FEATHER_FALLING = isAtLeast1_20_5 ? Enchantment.FEATHER_FALLING : getKey("PROTECTION_FALL");
        BLAST_PROTECTION = isAtLeast1_20_5 ? Enchantment.BLAST_PROTECTION : getKey("PROTECTION_EXPLOSIONS");
        PROJECTILE_PROTECTION = isAtLeast1_20_5 ? Enchantment.PROJECTILE_PROTECTION : getKey("PROTECTION_PROJECTILE");
        RESPIRATION = isAtLeast1_20_5 ? Enchantment.RESPIRATION : getKey("OXYGEN");
        AQUA_AFFINITY = isAtLeast1_20_5 ? Enchantment.AQUA_AFFINITY : getKey("WATER_WORKER");
        SHARPNESS = isAtLeast1_20_5 ? Enchantment.SHARPNESS : getKey("DAMAGE_ALL");
        SMITE = isAtLeast1_20_5 ? Enchantment.SMITE : getKey("DAMAGE_UNDEAD");
        BANE_OF_ARTHROPODS = isAtLeast1_20_5 ? Enchantment.BANE_OF_ARTHROPODS : getKey("DAMAGE_ARTHROPODS");
        LOOTING = isAtLeast1_20_5 ? Enchantment.LOOTING : getKey("LOOT_BONUS_MOBS");
        EFFICIENCY = isAtLeast1_20_5 ? Enchantment.EFFICIENCY : getKey("DIG_SPEED");
        UNBREAKING = isAtLeast1_20_5 ? Enchantment.UNBREAKING : getKey("DURABILITY");
        FORTUNE = isAtLeast1_20_5 ? Enchantment.FORTUNE : getKey("LOOT_BONUS_BLOCKS");
        POWER = isAtLeast1_20_5 ? Enchantment.POWER : getKey("ARROW_DAMAGE");
        PUNCH = isAtLeast1_20_5 ? Enchantment.PUNCH : getKey("ARROW_KNOCKBACK");
        FLAME = isAtLeast1_20_5 ? Enchantment.FLAME : getKey("ARROW_FIRE");
        INFINITY = isAtLeast1_20_5 ? Enchantment.INFINITY : getKey("ARROW_INFINITE");
        LUCK_OF_THE_SEA = isAtLeast1_20_5 ? Enchantment.LUCK_OF_THE_SEA : getKey("LUCK");
        SWEEPING_EDGE = isAtLeast1_20_5 ? Enchantment.SWEEPING_EDGE : getKey("SWEEPING");
    }

    @Nullable
    private static Enchantment getKey(@Nonnull String key) {
        try {
            Field field = Enchantment.class.getDeclaredField(key);
            return (Enchantment) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
