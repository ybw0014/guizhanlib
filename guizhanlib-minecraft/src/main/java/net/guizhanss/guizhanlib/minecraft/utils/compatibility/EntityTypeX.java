package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class holds {@link EntityType} that are renamed in 1.20.5.
 */
@SuppressWarnings("unused")
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
        ITEM = getByKey("item");
        LEASH_KNOT = getByKey("leash_knot");
        EYE_OF_ENDER = getByKey("eye_of_ender");
        POTION = getByKey("potion");
        EXPERIENCE_BOTTLE = getByKey("experience_bottle");
        TNT = getByKey("tnt");
        FIREWORK_ROCKET = getByKey("firework_rocket");
        COMMAND_BLOCK_MINECART = getByKey("command_block_minecart");
        CHEST_MINECART = getByKey("chest_minecart");
        FURNACE_MINECART = getByKey("furnace_minecart");
        TNT_MINECART = getByKey("tnt_minecart");
        HOPPER_MINECART = getByKey("hopper_minecart");
        SPAWNER_MINECART = getByKey("spawner_minecart");
        MOOSHROOM = getByKey("mooshroom");
        SNOW_GOLEM = getByKey("snow_golem");
        FISHING_BOBBER = getByKey("fishing_bobber");
        LIGHTNING_BOLT = getByKey("lightning_bolt");
    }

    @Nullable
    private static EntityType getByKey(@Nonnull String key) {
        return Registry.ENTITY_TYPE.get(NamespacedKey.minecraft(key));
    }
}
