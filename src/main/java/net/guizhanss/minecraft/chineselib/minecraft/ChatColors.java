package net.guizhanss.minecraft.chineselib.minecraft;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Minecraft - 聊天颜色
 * @author ybw0014
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
        return this.getChinese();
    }

    /**
     * 获取带颜色代码的颜色名
     * @return 带颜色代码的颜色名
     */
    public String toStringWithColor() {
        return this.getColor() +
            this.getChinese();
    }

    /**
     * 根据聊天颜色返回对应的枚举
     * @param chatColor {@link ChatColor} 聊天颜色
     * @return 对应的枚举
     */
    public static @Nonnull ChatColors fromChatColor(@Nonnull ChatColor chatColor) {
        Validate.notNull(chatColor, "聊天颜色不能为空");

        for (ChatColors color : ChatColors.values()) {
            if (color.getColor() == chatColor) {
                return color;
            }
        }
        throw new IllegalArgumentException("无效的ChatColor");
    }

    /**
     * 根据英文返回对应的枚举
     * @param english {@link String} 提供的英文
     * @return 对应的枚举
     */
    public static @Nullable ChatColors fromEnglish(@Nonnull String english) {
        Validate.notNull(english, "英文不能为空");

        String humanized = StringUtil.humanize(english);
        for (ChatColors color : ChatColors.values()) {
            if (color.getEnglish().equals(humanized)) {
                return color;
            }
        }
        return null;
    }
}
