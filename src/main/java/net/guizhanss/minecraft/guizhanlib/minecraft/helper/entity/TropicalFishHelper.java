package net.guizhanss.minecraft.guizhanlib.minecraft.helper.entity;

import lombok.experimental.UtilityClass;
import net.guizhanss.minecraft.guizhanlib.minecraft.LanguageHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;

import javax.annotation.Nonnull;

/**
 * 热带鱼({@link TropicalFish})
 *
 * @author ybw0014
 */
@UtilityClass
public class TropicalFishHelper {
    /**
     * 返回热带鱼样式({@link Pattern})的中文名
     *
     * @param pattern {@link Pattern} 热带鱼样式
     * @return 热带鱼样式的中文名称,如果获取失败则返回键名
     */
    public static @Nonnull String getPatternName(@Nonnull Pattern pattern) {
        return LanguageHelper.getLang(getPatternKey(pattern));
    }

    /**
     * 获取热带鱼样式({@link Pattern})的键名
     *
     * @param pattern {@link Pattern} 热带鱼样式
     * @return 热带鱼样式的键名
     */
    public static @Nonnull String getPatternKey(@Nonnull Pattern pattern) {
        Validate.notNull(pattern, "热带鱼样式不能为空");

        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
    }
}
