package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.inventory.ItemFlag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * This class holds {@link ItemFlag} that are renamed in 1.20.5.
 */
@SuppressWarnings("unused")
@UtilityClass
public class ItemFlagX {

    public static final ItemFlag HIDE_ADDITIONAL_TOOLTIP;

    static {
        boolean isPost205 = MinecraftVersionUtil.isAtLeast(20, 5);

        HIDE_ADDITIONAL_TOOLTIP = isPost205 ? ItemFlag.HIDE_ADDITIONAL_TOOLTIP : getField("HIDE_POTION_EFFECTS");
    }

    @Nullable
    private static ItemFlag getField(@Nonnull String key) {
        try {
            Field field = ItemFlag.class.getDeclaredField(key);
            return (ItemFlag) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
