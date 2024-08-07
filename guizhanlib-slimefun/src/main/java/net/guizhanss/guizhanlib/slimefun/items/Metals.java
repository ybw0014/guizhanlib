package net.guizhanss.guizhanlib.slimefun.items;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.slimefun.items.core.AdvancedMetal;
import net.guizhanss.guizhanlib.slimefun.items.core.BasicMetal;
import net.guizhanss.guizhanlib.utils.StringUtil;

import javax.annotation.Nonnull;

/**
 * Slimefun - 金属
 *
 * @author ybw0014
 */
@UtilityClass
public final class Metals {
    /**
     * 获取金属的中文类型
     *
     * @param type {@link String} 金属类型
     * @return 中文类型, 如果无匹配则返回英文
     */
    @Nonnull
    public static String getType(@Nonnull String type) {

        // 是否属于Slimefun基础金属
        BasicMetal basicMetal = BasicMetal.fromEnglish(type);
        if (basicMetal != null)
            return basicMetal.toString();

        // 是否属于Slimefun合金
        AdvancedMetal advancedMetal = AdvancedMetal.fromEnglish(type);
        if (advancedMetal != null)
            return advancedMetal.toString();

        return StringUtil.humanize(type);
    }
}
