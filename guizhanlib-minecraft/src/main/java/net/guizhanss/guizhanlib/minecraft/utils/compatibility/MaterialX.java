package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.Material;

import javax.annotation.Nullable;

@UtilityClass
public class MaterialX {

    public static final Material SHORT_GRASS;
    public static final Material TURTLE_SCUTE;

    static {
        SHORT_GRASS = MinecraftVersionUtil.isAtLeast(20, 3) ? getByName("SHORT_GRASS") : getByName("GRASS");
        TURTLE_SCUTE = MinecraftVersionUtil.isAtLeast(20, 5) ? getByName("TURTLE_SCUTE") : getByName("SCUTE");
    }

    @Nullable
    private static Material getByName(String name) {
        return Material.getMaterial(name);
    }
}
