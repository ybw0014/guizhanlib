package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * This class holds {@link PotionType} that are renamed in 1.20.5.
 */
@SuppressWarnings("unused")
@UtilityClass
public class PotionTypeX {

    public static final PotionType LEAPING;
    public static final PotionType SWIFTNESS;
    public static final PotionType HEALING;
    public static final PotionType HARMING;
    public static final PotionType REGENERATION;

    static {
        boolean isAtLeast1_20_5 = MinecraftVersionUtil.isAtLeast(20, 5);

        LEAPING = isAtLeast1_20_5 ? PotionType.LEAPING : getField("JUMP");
        SWIFTNESS = isAtLeast1_20_5 ? PotionType.SWIFTNESS : getField("SPEED");
        HEALING = isAtLeast1_20_5 ? PotionType.HEALING : getField("INSTANT_HEAL");
        HARMING = isAtLeast1_20_5 ? PotionType.HARMING : getField("INSTANT_DAMAGE");
        REGENERATION = isAtLeast1_20_5 ? PotionType.REGENERATION : getField("REGEN");
    }

    @Nullable
    private static PotionType getField(@Nonnull String key) {
        try {
            Field field = PotionType.class.getDeclaredField(key);
            return (PotionType) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
