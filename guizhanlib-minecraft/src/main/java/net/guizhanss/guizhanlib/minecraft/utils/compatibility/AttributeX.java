package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.attribute.Attribute;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * This class holds {@link Attribute} that are renamed in 1.20.5.
 */
@SuppressWarnings("unused")
@UtilityClass
public class AttributeX {

    public static final Attribute GENERIC_JUMP_STRENGTH;
    public static final Attribute MAX_HEALTH;
    public static final Attribute FOLLOW_RANGE;
    public static final Attribute KNOCKBACK_RESISTANCE;
    public static final Attribute MOVEMENT_SPEED;
    public static final Attribute FLYING_SPEED;
    public static final Attribute ATTACK_DAMAGE;
    public static final Attribute ATTACK_KNOCKBACK;
    public static final Attribute ATTACK_SPEED;
    public static final Attribute ARMOR;
    public static final Attribute ARMOR_TOUGHNESS;
    public static final Attribute FALL_DAMAGE_MULTIPLIER;
    public static final Attribute LUCK;
    public static final Attribute MAX_ABSORPTION;
    public static final Attribute SAFE_FALL_DISTANCE;
    public static final Attribute SCALE;
    public static final Attribute STEP_HEIGHT;
    public static final Attribute GRAVITY;
    public static final Attribute JUMP_STRENGTH;
    public static final Attribute BURNING_TIME; // 1.21+
    public static final Attribute EXPLOSION_KNOCKBACK_RESISTANCE; // 1.21+
    public static final Attribute MOVEMENT_EFFICIENCY; // 1.21+
    public static final Attribute OXYGEN_BONUS; // 1.21+
    public static final Attribute WATER_MOVEMENT_EFFICIENCY; // 1.21+
    public static final Attribute TEMPT_RANGE;
    public static final Attribute BLOCK_INTERACTION_RANGE;
    public static final Attribute ENTITY_INTERACTION_RANGE;
    public static final Attribute BLOCK_BREAK_SPEED;
    public static final Attribute MINING_EFFICIENCY; // 1.21+
    public static final Attribute SNEAKING_SPEED; // 1.21+
    public static final Attribute SUBMERGED_MINING_SPEED; // 1.21+
    public static final Attribute SWEEPING_DAMAGE_RATIO; // 1.21+
    public static final Attribute SPAWN_REINFORCEMENTS;

    static {
        boolean isPost205 = MinecraftVersionUtil.isAtLeast(20, 5);
        boolean isPost213 = MinecraftVersionUtil.isAtLeast(21, 3);

        // 1.20.5 rename
        GENERIC_JUMP_STRENGTH = isPost213 ? Attribute.JUMP_STRENGTH : isPost205 ? getField("GENERIC_JUMP_STRENGTH") : getField("HORSE_JUMP_STRENGTH");

        // 1.21.3 rename
        MAX_HEALTH = isPost213 ? Attribute.MAX_HEALTH : getField("GENERIC_MAX_HEALTH");
        FOLLOW_RANGE = isPost213 ? Attribute.FOLLOW_RANGE : getField("GENERIC_FOLLOW_RANGE");
        KNOCKBACK_RESISTANCE = isPost213 ? Attribute.KNOCKBACK_RESISTANCE : getField("GENERIC_KNOCKBACK_RESISTANCE");
        MOVEMENT_SPEED = isPost213 ? Attribute.MOVEMENT_SPEED : getField("GENERIC_MOVEMENT_SPEED");
        FLYING_SPEED = isPost213 ? Attribute.FLYING_SPEED : getField("GENERIC_FLYING_SPEED");
        ATTACK_DAMAGE = isPost213 ? Attribute.ATTACK_DAMAGE : getField("GENERIC_ATTACK_DAMAGE");
        ATTACK_KNOCKBACK = isPost213 ? Attribute.ATTACK_KNOCKBACK : getField("GENERIC_ATTACK_KNOCKBACK");
        ATTACK_SPEED = isPost213 ? Attribute.ATTACK_SPEED : getField("GENERIC_ATTACK_SPEED");
        ARMOR = isPost213 ? Attribute.ARMOR : getField("GENERIC_ARMOR");
        ARMOR_TOUGHNESS = isPost213 ? Attribute.ARMOR_TOUGHNESS : getField("GENERIC_ARMOR_TOUGHNESS");
        FALL_DAMAGE_MULTIPLIER = isPost213 ? Attribute.FALL_DAMAGE_MULTIPLIER : getField("GENERIC_FALL_DAMAGE_MULTIPLIER");
        LUCK = isPost213 ? Attribute.LUCK : getField("GENERIC_LUCK");
        MAX_ABSORPTION = isPost213 ? Attribute.MAX_ABSORPTION : getField("GENERIC_MAX_ABSORPTION");
        SAFE_FALL_DISTANCE = isPost213 ? Attribute.SAFE_FALL_DISTANCE : getField("GENERIC_SAFE_FALL_DISTANCE");
        SCALE = isPost213 ? Attribute.SCALE : getField("GENERIC_SCALE");
        STEP_HEIGHT = isPost213 ? Attribute.STEP_HEIGHT : getField("GENERIC_STEP_HEIGHT");
        GRAVITY = isPost213 ? Attribute.GRAVITY : getField("GENERIC_GRAVITY");
        JUMP_STRENGTH = GENERIC_JUMP_STRENGTH;
        BURNING_TIME = isPost213 ? Attribute.BURNING_TIME : getField("GENERIC_BURNING_TIME");
        EXPLOSION_KNOCKBACK_RESISTANCE = isPost213 ? Attribute.EXPLOSION_KNOCKBACK_RESISTANCE : getField("GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE");
        MOVEMENT_EFFICIENCY = isPost213 ? Attribute.MOVEMENT_EFFICIENCY : getField("GENERIC_MOVEMENT_EFFICIENCY");
        OXYGEN_BONUS = isPost213 ? Attribute.OXYGEN_BONUS : getField("GENERIC_OXYGEN_BONUS");
        WATER_MOVEMENT_EFFICIENCY = isPost213 ? Attribute.WATER_MOVEMENT_EFFICIENCY : getField("GENERIC_WATER_MOVEMENT_EFFICIENCY");
        TEMPT_RANGE = isPost213 ? Attribute.TEMPT_RANGE : getField("GENERIC_TEMPT_RANGE");
        BLOCK_INTERACTION_RANGE = isPost213 ? Attribute.BLOCK_INTERACTION_RANGE : getField("PLAYER_BLOCK_INTERACTION_RANGE");
        ENTITY_INTERACTION_RANGE = isPost213 ? Attribute.ENTITY_INTERACTION_RANGE : getField("PLAYER_ENTITY_INTERACTION_RANGE");
        BLOCK_BREAK_SPEED = isPost213 ? Attribute.BLOCK_BREAK_SPEED : getField("PLAYER_BLOCK_BREAK_SPEED");
        MINING_EFFICIENCY = isPost213 ? Attribute.MINING_EFFICIENCY : getField("PLAYER_MINING_EFFICIENCY");
        SNEAKING_SPEED = isPost213 ? Attribute.SNEAKING_SPEED : getField("PLAYER_SNEAKING_SPEED");
        SUBMERGED_MINING_SPEED = isPost213 ? Attribute.SUBMERGED_MINING_SPEED : getField("PLAYER_SUBMERGED_MINING_SPEED");
        SWEEPING_DAMAGE_RATIO = isPost213 ? Attribute.SWEEPING_DAMAGE_RATIO : getField("PLAYER_SWEEPING_DAMAGE_RATIO");
        SPAWN_REINFORCEMENTS = isPost213 ? Attribute.SPAWN_REINFORCEMENTS : getField("ZOMBIE_SPAWN_REINFORCEMENTS");
    }

    @Nullable
    private static Attribute getField(@Nonnull String key) {
        try {
            Field field = Attribute.class.getDeclaredField(key);
            return (Attribute) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
