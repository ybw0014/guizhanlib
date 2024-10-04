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

    static {
        boolean isAtLeast1_20_5 = MinecraftVersionUtil.isAtLeast(20, 5);

        GENERIC_JUMP_STRENGTH = isAtLeast1_20_5 ? Attribute.GENERIC_JUMP_STRENGTH : getField("HORSE_JUMP_STRENGTH");
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
