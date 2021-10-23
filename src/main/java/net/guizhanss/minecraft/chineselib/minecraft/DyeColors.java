package net.guizhanss.minecraft.chineselib.minecraft;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 所有染料颜色
 * @author ybw0014
 */
public enum DyeColors {
    BLACK("Black", "黑色"),
    BLUE("Blue", "蓝色"),
    BROWN("Brown", "棕色"),
    CYAN("Cyan", "青色"),
    GRAY("Gray", "灰色"),
    GREEN("Green", "绿色"),
    LIGHT_BLUE("Light Blue", "淡蓝色"),
    LIGHT_GRAY("Light Gray", "淡灰色"),
    LIME("Lime", "黄绿色"),
    MAGENTA("Magenta", "品红色"),
    ORANGE("Orange", "橙色"),
    PINK("Pink", "粉红色"),
    PURPLE("Purple", "紫色"),
    RED("Red", "红色"),
    WHITE("White", "白色"),
    YELLOW("Yellow", "黄色");

    private @Getter String english;
    private @Getter String chinese;

    DyeColors(@Nonnull String english, @Nonnull String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    @Override
    public String toString() {
        return this.chinese;
    }

    /**
     * 根据英文返回对应的枚举类型
     * @param english 提供的英文类型
     * @return 对应的枚举类型
     */
    @Nullable
    public static DyeColors fromEnglish(String english) {
        String humanized = StringUtil.humanize(english);
        for (DyeColors color : DyeColors.values()) {
            if (color.getEnglish().equals(humanized)) {
                return color;
            }
        }
        return null;
    }
}
