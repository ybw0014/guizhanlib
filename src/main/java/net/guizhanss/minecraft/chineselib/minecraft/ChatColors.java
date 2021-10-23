package net.guizhanss.minecraft.chineselib.minecraft;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Minecraft - 聊天颜色
 */
public enum ChatColors {
    AQUA("Aqua", "天蓝色"),
    BLACK("Black", "黑色"),
    BLUE("Blue", "蓝色"),
    BOLD("Bold", "加粗"),
    DARK_AQUA("Dark Aqua", "湖蓝色"),
    DARK_BLUE("Dark Blue", "深蓝色"),
    DARK_GRAY("Dark Gray", "深灰色"),
    DARK_GREEN("Dark Green", "深绿色"),
    DARK_PURPLE("Dark Purple", "紫色"),
    DARK_RED("Dark Red", "深红色"),
    GOLD("Gold", "金色"),
    GRAY("Gray", "灰色"),
    GREEN("Green", "绿色"),
    ITALIC("Italic", "斜体"),
    LIGHT_PURPLE("Light Purple", "粉红色"),
    MAGIC("Magic", "随机"),
    RED("Red", "红色"),
    RESET("Reset", "重置"),
    STRIKETHROUGH("Strikethrough", "删除线"),
    UNDERLINE("Underline", "下划线"),
    WHITE("White", "白色"),
    YELLOW("Yellow", "黄色");

    public static final char COLOR_CHAR = '\u00a7';

    private @Getter String english;
    private @Getter String chinese;

    ChatColors(@Nonnull String english, @Nonnull String chinese) {
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
    public static ChatColors fromEnglish(String english) {
        String humanized = StringUtil.humanize(english);
        for (ChatColors color : ChatColors.values()) {
            if (color.getEnglish().equals(humanized)) {
                return color;
            }
        }
        return null;
    }
}
