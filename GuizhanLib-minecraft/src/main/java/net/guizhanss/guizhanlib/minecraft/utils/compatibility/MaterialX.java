package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.Material;

@UtilityClass
public class MaterialX {

    public static final Material SHORT_GRASS;
    public static final Material TURTLE_SCUTE;

    static {
        SHORT_GRASS = MinecraftVersionUtil.isAtLeast(20, 3) ? Material.getMaterial("SHORT_GRASS") : Material.getMaterial("GRASS");
        TURTLE_SCUTE = MinecraftVersionUtil.isAtLeast(20, 5) ? Material.getMaterial("TURTLE_SCUTE") : Material.getMaterial("SCUTE");
    }
}
