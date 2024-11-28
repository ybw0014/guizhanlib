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
        boolean isPost205 = MinecraftVersionUtil.isAtLeast(20, 5);

        LEAPING = isPost205 ? PotionType.LEAPING : getField("JUMP");
        SWIFTNESS = isPost205 ? PotionType.SWIFTNESS : getField("SPEED");
        HEALING = isPost205 ? PotionType.HEALING : getField("INSTANT_HEAL");
        HARMING = isPost205 ? PotionType.HARMING : getField("INSTANT_DAMAGE");
        REGENERATION = isPost205 ? PotionType.REGENERATION : getField("REGEN");
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
