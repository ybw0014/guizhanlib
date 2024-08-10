package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

@UtilityClass
public class MaterialX {

    public static final Material SHORT_GRASS;
    public static final Material TURTLE_SCUTE;

    static {
        SHORT_GRASS = MinecraftVersionUtil.isAtLeast(20, 3) ? Material.SHORT_GRASS : getKey("GRASS");
        TURTLE_SCUTE = MinecraftVersionUtil.isAtLeast(20, 5) ? Material.TURTLE_SCUTE : getKey("SCUTE");
    }

    @Nullable
    private static Material getKey(@Nonnull String key) {
        try {
            Field field = Material.class.getDeclaredField(key);
            return (Material) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
