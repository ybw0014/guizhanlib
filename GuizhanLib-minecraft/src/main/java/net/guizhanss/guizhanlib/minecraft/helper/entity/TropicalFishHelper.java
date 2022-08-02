package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;

import javax.annotation.Nonnull;

/**
 * 热带鱼({@link TropicalFish})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("ConstantConditions")
public final class TropicalFishHelper {
    /**
     * 返回热带鱼样式({@link Pattern})的中文名
     *
     * @param pattern {@link Pattern} 热带鱼样式
     *
     * @return 热带鱼样式的中文名称, 如果获取失败则返回键名
     */
    @Nonnull
    public static String getPatternName(@Nonnull Pattern pattern) {
        return LanguageHelper.getLangOrKey(getPatternKey(pattern));
    }

    /**
     * 获取热带鱼样式({@link Pattern})的键名
     *
     * @param pattern {@link Pattern} 热带鱼样式
     *
     * @return 热带鱼样式的键名
     */
    @Nonnull
    public static String getPatternKey(@Nonnull Pattern pattern) {
        Preconditions.checkArgument(pattern != null, "热带鱼样式不能为空");

        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
    }
}
