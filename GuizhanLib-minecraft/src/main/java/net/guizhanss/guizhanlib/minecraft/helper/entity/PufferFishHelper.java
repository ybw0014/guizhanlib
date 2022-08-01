package net.guizhanss.guizhanlib.minecraft.helper.entity;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.PufferFish;

import javax.annotation.Nonnull;

/**
 * 河豚({@link PufferFish})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings({"unused"})
public final class PufferFishHelper {
    /**
     * 获取河豚的膨胀状态的中文
     *
     * @param level 膨胀状态
     *
     * @return 膨胀状态的中文
     */
    @Nonnull
    public static String getPuffState(int level) {
        return switch (level) {
            case 0 -> "未膨胀";
            case 1 -> "半膨胀";
            case 2 -> "完全膨胀";
            default -> "未知";
        };
    }
}
