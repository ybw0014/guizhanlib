package net.guizhanss.minecraft.chineselib.minecraft;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Minecraft - 聊天颜色
 */
public enum ChatColors {
    AQUA(ChatColor.AQUA, "Aqua", "天蓝色"),
    BLACK(ChatColor.BLACK, "Black", "黑色"),
    BLUE(ChatColor.BLUE, "Blue", "蓝色"),
    BOLD(ChatColor.BOLD, "Bold", "加粗"),
    DARK_AQUA(ChatColor.DARK_AQUA, "Dark Aqua", "湖蓝色"),
    DARK_BLUE(ChatColor.DARK_BLUE, "Dark Blue", "深蓝色"),
    DARK_GRAY(ChatColor.DARK_GRAY, "Dark Gray", "深灰色"),
    DARK_GREEN(ChatColor.DARK_GREEN, "Dark Green", "深绿色"),
    DARK_PURPLE(ChatColor.DARK_PURPLE, "Dark Purple", "紫色"),
    DARK_RED(ChatColor.DARK_RED, "Dark Red", "深红色"),
    GOLD(ChatColor.GOLD, "Gold", "金色"),
    GRAY(ChatColor.GRAY, "Gray", "灰色"),
    GREEN(ChatColor.GREEN, "Green", "绿色"),
    ITALIC(ChatColor.ITALIC, "Italic", "斜体"),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, "Light Purple", "粉红色"),
    MAGIC(ChatColor.MAGIC, "Magic", "随机"),
    RED(ChatColor.RED, "Red", "红色"),
    RESET(ChatColor.RESET, "Reset", "重置"),
    STRIKETHROUGH(ChatColor.STRIKETHROUGH, "Strikethrough", "删除线"),
    UNDERLINE(ChatColor.UNDERLINE, "Underline", "下划线"),
    WHITE(ChatColor.WHITE, "White", "白色"),
    YELLOW(ChatColor.YELLOW, "Yellow", "黄色");

    public static final char COLOR_CHAR = '\u00a7';

    private final @Getter ChatColor color;
    private final @Getter String english;
    private final @Getter String chinese;

    @ParametersAreNonnullByDefault
    ChatColors(ChatColor color, String english, String chinese) {
        this.color = color;
        this.english = english;
        this.chinese = chinese;
    }

    @Override
    public String toString() {
        return this.chinese;
    }

    /**
     * 根据聊天颜色返回对应的枚举类型
     * @param chatColor ChatColor
     * @return 对应的枚举类型
     */
    public static @Nonnull ChatColors fromChatColor(@Nonnull ChatColor chatColor) {
        for (ChatColors color : ChatColors.values()) {
            if (color.getColor() == chatColor) {
                return color;
            }
        }
        throw new IllegalArgumentException("无效的ChatColor");
    }

    /**
     * 根据英文返回对应的枚举类型
     * @param english 提供的英文类型
     * @return 对应的枚举类型
     */
    public static @Nullable ChatColors fromEnglish(@Nonnull String english) {
        String humanized = StringUtil.humanize(english);
        for (ChatColors color : ChatColors.values()) {
            if (color.getEnglish().equals(humanized)) {
                return color;
            }
        }
        return null;
    }
}
