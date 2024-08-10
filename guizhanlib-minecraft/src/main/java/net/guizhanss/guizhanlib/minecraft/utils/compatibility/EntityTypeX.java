package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

@UtilityClass
public class EntityTypeX {

    public static final EntityType ITEM;
    public static final EntityType LEASH_KNOT;
    public static final EntityType EYE_OF_ENDER;
    public static final EntityType POTION;
    public static final EntityType EXPERIENCE_BOTTLE;
    public static final EntityType TNT;
    public static final EntityType FIREWORK_ROCKET;
    public static final EntityType COMMAND_BLOCK_MINECART;
    public static final EntityType CHEST_MINECART;
    public static final EntityType FURNACE_MINECART;
    public static final EntityType TNT_MINECART;
    public static final EntityType HOPPER_MINECART;
    public static final EntityType SPAWNER_MINECART;
    public static final EntityType MOOSHROOM;
    public static final EntityType SNOW_GOLEM;
    public static final EntityType FISHING_BOBBER;
    public static final EntityType LIGHTNING_BOLT;

    static {
        boolean isAtLeast1_20_5 = MinecraftVersionUtil.isAtLeast(20, 5);

        ITEM = isAtLeast1_20_5 ? EntityType.ITEM : getKey("DROPPED_ITEM");
        LEASH_KNOT = isAtLeast1_20_5 ? EntityType.LEASH_KNOT : getKey("LEASH_HITCH");
        EYE_OF_ENDER = isAtLeast1_20_5 ? EntityType.EYE_OF_ENDER : getKey("ENDER_SIGNAL");
        POTION = isAtLeast1_20_5 ? EntityType.POTION : getKey("SPLASH_POTION");
        EXPERIENCE_BOTTLE = isAtLeast1_20_5 ? EntityType.EXPERIENCE_BOTTLE : getKey("THROWN_EXP_BOTTLE");
        TNT = isAtLeast1_20_5 ? EntityType.TNT : getKey("PRIMED_TNT");
        FIREWORK_ROCKET = isAtLeast1_20_5 ? EntityType.FIREWORK_ROCKET : getKey("FIREWORK");
        COMMAND_BLOCK_MINECART = isAtLeast1_20_5 ? EntityType.COMMAND_BLOCK_MINECART : getKey("MINECART_COMMAND");
        CHEST_MINECART = isAtLeast1_20_5 ? EntityType.CHEST_MINECART : getKey("MINECART_CHEST");
        FURNACE_MINECART = isAtLeast1_20_5 ? EntityType.FURNACE_MINECART : getKey("MINECART_FURNACE");
        TNT_MINECART = isAtLeast1_20_5 ? EntityType.TNT_MINECART : getKey("MINECART_TNT");
        HOPPER_MINECART = isAtLeast1_20_5 ? EntityType.HOPPER_MINECART : getKey("MINECART_HOPPER");
        SPAWNER_MINECART = isAtLeast1_20_5 ? EntityType.SPAWNER_MINECART : getKey("MINECART_MOB_SPAWNER");
        MOOSHROOM = isAtLeast1_20_5 ? EntityType.MOOSHROOM : getKey("MUSHROOM_COW");
        SNOW_GOLEM = isAtLeast1_20_5 ? EntityType.SNOW_GOLEM : getKey("SNOWMAN");
        FISHING_BOBBER = isAtLeast1_20_5 ? EntityType.FISHING_BOBBER : getKey("FISHING_HOOK");
        LIGHTNING_BOLT = isAtLeast1_20_5 ? EntityType.LIGHTNING_BOLT : getKey("LIGHTNING");
    }

    @Nullable
    private static EntityType getKey(@Nonnull String key) {
        try {
            Field field = EntityType.class.getDeclaredField(key);
            return (EntityType) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
