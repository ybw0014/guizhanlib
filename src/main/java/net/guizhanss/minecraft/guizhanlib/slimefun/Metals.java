package net.guizhanss.minecraft.guizhanlib.slimefun;

import lombok.experimental.UtilityClass;
import net.guizhanss.minecraft.guizhanlib.slimefun.core.BasicMetals;
import net.guizhanss.minecraft.guizhanlib.utils.StringUtil;

import javax.annotation.Nonnull;

/**
 * Slimefun - 金属
 * @author ybw0014
 */
@UtilityClass
public class Metals {
    /**
     * 获取金属的中文类型
     * @param type {@link String} 金属类型
     * @return 中文类型,如果无匹配则返回英文
     */
    public static @Nonnull String getType(@Nonnull String type){

        // 是否属于Slimefun基础金属
        BasicMetals basicMetal = BasicMetals.fromEnglish(type);
        if (basicMetal != null)
            return basicMetal.toString();

        return StringUtil.humanize(type);
    }
}
