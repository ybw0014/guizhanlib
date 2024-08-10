package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.block.banner.PatternType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * This class holds {@link PatternType} that are renamed in 1.20.5.
 */
@UtilityClass
public class PatternTypeX {

    public static final PatternType DIAGONAL_UP_RIGHT;
    public static final PatternType SMALL_STRIPES;
    public static final PatternType DIAGONAL_UP_LEFT;
    public static final PatternType DIAGONAL_RIGHT;
    public static final PatternType CIRCLE;
    public static final PatternType RHOMBUS;
    public static final PatternType HALF_VERTICAL_RIGHT;
    public static final PatternType HALF_HORIZONTAL_BOTTOM;

    static {
        boolean isAtLeast1_20_5 = MinecraftVersionUtil.isAtLeast(20, 5);

        DIAGONAL_UP_RIGHT = isAtLeast1_20_5 ? PatternType.DIAGONAL_UP_RIGHT : getKey("DIAGONAL_RIGHT");
        SMALL_STRIPES = isAtLeast1_20_5 ? PatternType.SMALL_STRIPES : getKey("STRIPE_SMALL");
        DIAGONAL_UP_LEFT = isAtLeast1_20_5 ? PatternType.DIAGONAL_UP_LEFT : getKey("DIAGONAL_LEFT_MIRROR");
        DIAGONAL_RIGHT = isAtLeast1_20_5 ? PatternType.DIAGONAL_RIGHT : getKey("DIAGONAL_RIGHT_MIRROR");
        CIRCLE = isAtLeast1_20_5 ? PatternType.CIRCLE : getKey("CIRCLE_MIDDLE");
        RHOMBUS = isAtLeast1_20_5 ? PatternType.RHOMBUS : getKey("RHOMBUS_MIDDLE");
        HALF_VERTICAL_RIGHT = isAtLeast1_20_5 ? PatternType.HALF_VERTICAL_RIGHT : getKey("HALF_VERTICAL_MIRROR");
        HALF_HORIZONTAL_BOTTOM = isAtLeast1_20_5 ? PatternType.HALF_HORIZONTAL_BOTTOM : getKey("HALF_HORIZONTAL_MIRROR");
    }

    @Nullable
    private static PatternType getKey(@Nonnull String key) {
        try {
            Field field = PatternType.class.getDeclaredField(key);
            return (PatternType) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
