package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.Particle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * This class holds {@link Particle} that are renamed in 1.20.5.
 */
@UtilityClass
public class ParticleX {

    public static final Particle POOF;
    public static final Particle EXPLOSION;
    public static final Particle EXPLOSION_EMITTER;
    public static final Particle FIREWORK;
    public static final Particle BUBBLE;
    public static final Particle SPLASH;
    public static final Particle FISHING;
    public static final Particle UNDERWATER;
    public static final Particle ENCHANTED_HIT;
    public static final Particle SMOKE;
    public static final Particle LARGE_SMOKE;
    public static final Particle EFFECT;
    public static final Particle INSTANT_EFFECT;
    public static final Particle ENTITY_EFFECT;
    public static final Particle WITCH;
    public static final Particle DRIPPING_WATER;
    public static final Particle DRIPPING_LAVA;
    public static final Particle ANGRY_VILLAGER;
    public static final Particle HAPPY_VILLAGER;
    public static final Particle MYCELIUM;
    public static final Particle ENCHANT;
    public static final Particle DUST;
    public static final Particle ITEM_SNOWBALL;
    public static final Particle ITEM_SLIME;
    public static final Particle ITEM;
    public static final Particle BLOCK;
    public static final Particle RAIN;
    public static final Particle ELDER_GUARDIAN;
    public static final Particle TOTEM_OF_UNDYING;
    public static final Particle GUST_EMITTER_LARGE;

    static {
        boolean isAtLeast1_20_5 = MinecraftVersionUtil.isAtLeast(20, 5);

        POOF = isAtLeast1_20_5 ? Particle.POOF : getKey("EXPLOSION_NORMAL");
        EXPLOSION = isAtLeast1_20_5 ? Particle.EXPLOSION : getKey("EXPLOSION_LARGE");
        EXPLOSION_EMITTER = isAtLeast1_20_5 ? Particle.EXPLOSION_EMITTER : getKey("EXPLOSION_HUGE");
        FIREWORK = isAtLeast1_20_5 ? Particle.FIREWORK : getKey("FIREWORKS_SPARK");
        BUBBLE = isAtLeast1_20_5 ? Particle.BUBBLE : getKey("WATER_BUBBLE");
        SPLASH = isAtLeast1_20_5 ? Particle.SPLASH : getKey("WATER_SPLASH");
        FISHING = isAtLeast1_20_5 ? Particle.FISHING : getKey("WATER_WAKE");
        UNDERWATER = isAtLeast1_20_5 ? Particle.UNDERWATER : getKey("SUSPENDED");
        ENCHANTED_HIT = isAtLeast1_20_5 ? Particle.ENCHANTED_HIT : getKey("CRIT_MAGIC");
        SMOKE = isAtLeast1_20_5 ? Particle.SMOKE : getKey("SMOKE_NORMAL");
        LARGE_SMOKE = isAtLeast1_20_5 ? Particle.LARGE_SMOKE : getKey("SMOKE_LARGE");
        EFFECT = isAtLeast1_20_5 ? Particle.EFFECT : getKey("SPELL");
        INSTANT_EFFECT = isAtLeast1_20_5 ? Particle.INSTANT_EFFECT : getKey("SPELL_INSTANT");
        ENTITY_EFFECT = isAtLeast1_20_5 ? Particle.ENTITY_EFFECT : getKey("SPELL_MOB");
        WITCH = isAtLeast1_20_5 ? Particle.WITCH : getKey("SPELL_WITCH");
        DRIPPING_WATER = isAtLeast1_20_5 ? Particle.DRIPPING_WATER : getKey("DRIP_WATER");
        DRIPPING_LAVA = isAtLeast1_20_5 ? Particle.DRIPPING_LAVA : getKey("DRIP_LAVA");
        ANGRY_VILLAGER = isAtLeast1_20_5 ? Particle.ANGRY_VILLAGER : getKey("VILLAGER_ANGRY");
        HAPPY_VILLAGER = isAtLeast1_20_5 ? Particle.HAPPY_VILLAGER : getKey("VILLAGER_HAPPY");
        MYCELIUM = isAtLeast1_20_5 ? Particle.MYCELIUM : getKey("TOWN_AURA");
        ENCHANT = isAtLeast1_20_5 ? Particle.ENCHANT : getKey("ENCHANTMENT_TABLE");
        DUST = isAtLeast1_20_5 ? Particle.DUST : getKey("REDSTONE");
        ITEM_SNOWBALL = isAtLeast1_20_5 ? Particle.ITEM_SNOWBALL : getKey("SNOWBALL");
        ITEM_SLIME = isAtLeast1_20_5 ? Particle.ITEM_SLIME : getKey("SLIME");
        ITEM = isAtLeast1_20_5 ? Particle.ITEM : getKey("ITEM_CRACK");
        BLOCK = isAtLeast1_20_5 ? Particle.BLOCK : getKey("BLOCK_DUST");
        RAIN = isAtLeast1_20_5 ? Particle.RAIN : getKey("WATER_DROP");
        ELDER_GUARDIAN = isAtLeast1_20_5 ? Particle.ELDER_GUARDIAN : getKey("MOB_APPEARANCE");
        TOTEM_OF_UNDYING = isAtLeast1_20_5 ? Particle.TOTEM_OF_UNDYING : getKey("TOTEM");

        GUST_EMITTER_LARGE = MinecraftVersionUtil.isBefore(20) ? null :
            isAtLeast1_20_5 ? Particle.GUST_EMITTER_LARGE : getKey("GUST_EMITTER");
    }

    @Nullable
    private static Particle getKey(@Nonnull String key) {
        try {
            Field field = Particle.class.getDeclaredField(key);
            return (Particle) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
