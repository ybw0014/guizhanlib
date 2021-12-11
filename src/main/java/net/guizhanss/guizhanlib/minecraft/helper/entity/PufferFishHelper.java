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
public class PufferFishHelper {
    /**
     * 获取河豚的膨胀状态的中文
     * @param level 膨胀状态
     * @return 膨胀状态的中文
     */
    public static @Nonnull String getPuffState(int level){
        switch(level){
            case 0:
                return "未膨胀";
            case 1:
                return "半膨胀";
            case 2:
                return "完全膨胀";
            default:
                return "未知";
        }
    }
}
