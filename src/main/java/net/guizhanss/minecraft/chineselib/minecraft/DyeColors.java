package net.guizhanss.minecraft.chineselib.minecraft;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;
import org.bukkit.DyeColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 所有染料颜色
 * @author ybw0014
 */
public enum DyeColors {
    BLACK(DyeColor.BLACK, "Black", "黑色"),
    BLUE(DyeColor.BLUE, "Blue", "蓝色"),
    BROWN(DyeColor.BROWN, "Brown", "棕色"),
    CYAN(DyeColor.CYAN, "Cyan", "青色"),
    GRAY(DyeColor.GRAY, "Gray", "灰色"),
    GREEN(DyeColor.GREEN, "Green", "绿色"),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE, "Light Blue", "淡蓝色"),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY, "Light Gray", "淡灰色"),
    LIME(DyeColor.LIME, "Lime", "黄绿色"),
    MAGENTA(DyeColor.MAGENTA, "Magenta", "品红色"),
    ORANGE(DyeColor.ORANGE, "Orange", "橙色"),
    PINK(DyeColor.PINK, "Pink", "粉红色"),
    PURPLE(DyeColor.PURPLE, "Purple", "紫色"),
    RED(DyeColor.RED, "Red", "红色"),
    WHITE(DyeColor.WHITE, "White", "白色"),
    YELLOW(DyeColor.YELLOW, "Yellow", "黄色");

    private final @Getter DyeColor color;
    private final @Getter String english;
    private final @Getter String chinese;

    @ParametersAreNonnullByDefault
    DyeColors(DyeColor color, String english, String chinese) {
        this.color = color;
        this.english = english;
        this.chinese = chinese;
    }

    @Override
    public String toString() {
        return this.chinese;
    }

    /**
     * 根据染料颜色返回对应的枚举类型
     * @param dyeColor DyeColor
     * @return 对应的枚举类型
     */
    public static @Nonnull DyeColors fromDyeColor(@Nonnull DyeColor dyeColor) {
        for (DyeColors color : DyeColors.values()) {
            if (color.getColor() == dyeColor) {
                return color;
            }
        }
        throw new IllegalArgumentException("无效的DyeColor");
    }

    /**
     * 根据英文返回对应的枚举类型
     * @param english 提供的英文类型
     * @return 对应的枚举类型
     */

    public static @Nullable DyeColors fromEnglish(@Nonnull String english) {
        String humanized = StringUtil.humanize(english);
        for (DyeColors color : DyeColors.values()) {
            if (color.getEnglish().equals(humanized)) {
                return color;
            }
        }
        return null;
    }
}
