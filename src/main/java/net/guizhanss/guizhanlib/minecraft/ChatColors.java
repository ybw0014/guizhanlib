package net.guizhanss.guizhanlib.minecraft;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

/**
 * 聊天颜色
 *
 * @author ybw0014
 *
 * @see ChatColor
 */
public enum ChatColors {
    /**
     * 天蓝色
     */
    AQUA(ChatColor.AQUA, "Aqua", "天蓝色"),
    /**
     * 黑色
     */
    BLACK(ChatColor.BLACK, "Black", "黑色"),
    /**
     * 蓝色
     */
    BLUE(ChatColor.BLUE, "Blue", "蓝色"),
    /**
     * 加粗
     */
    BOLD(ChatColor.BOLD, "Bold", "加粗"),
    /**
     * 湖蓝色
     */
    DARK_AQUA(ChatColor.DARK_AQUA, "Dark Aqua", "湖蓝色"),
    /**
     * 深蓝色
     */
    DARK_BLUE(ChatColor.DARK_BLUE, "Dark Blue", "深蓝色"),
    /**
     * 深灰色
     */
    DARK_GRAY(ChatColor.DARK_GRAY, "Dark Gray", "深灰色"),
    /**
     * 深绿色
     */
    DARK_GREEN(ChatColor.DARK_GREEN, "Dark Green", "深绿色"),
    /**
     * 紫色
     */
    DARK_PURPLE(ChatColor.DARK_PURPLE, "Dark Purple", "紫色"),
    /**
     * 深红色
     */
    DARK_RED(ChatColor.DARK_RED, "Dark Red", "深红色"),
    /**
     * 金色
     */
    GOLD(ChatColor.GOLD, "Gold", "金色"),
    /**
     * 灰色
     */
    GRAY(ChatColor.GRAY, "Gray", "灰色"),
    /**
     * 绿色
     */
    GREEN(ChatColor.GREEN, "Green", "绿色"),
    /**
     * 斜体
     */
    ITALIC(ChatColor.ITALIC, "Italic", "斜体"),
    /**
     * 粉红色
     */
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, "Light Purple", "粉红色"),
    /**
     * 随机
     */
    MAGIC(ChatColor.MAGIC, "Magic", "随机"),
    /**
     * 红色
     */
    RED(ChatColor.RED, "Red", "红色"),
    /**
     * 重置
     */
    RESET(ChatColor.RESET, "Reset", "重置"),
    /**
     * 删除线
     */
    STRIKETHROUGH(ChatColor.STRIKETHROUGH, "Strikethrough", "删除线"),
    /**
     * 下划线
     */
    UNDERLINE(ChatColor.UNDERLINE, "Underline", "下划线"),
    /**
     * 白色
     */
    WHITE(ChatColor.WHITE, "White", "白色"),
    /**
     * 黄色
     */
    YELLOW(ChatColor.YELLOW, "Yellow", "黄色");

    /**
     * 颜色代码前缀
     */
    public static final char COLOR_CHAR = '\u00a7';

    private static final ChatColors[] valuesCache = values();
    private static final Map<ChatColor, ChatColors> colorLookup = new HashMap<>();

    static {
        for (ChatColors color : valuesCache) {
            colorLookup.put(color.getColor(), color);
        }
    }

    private final @Getter ChatColor color;
    private final @Getter String english;
    private final @Getter String chinese;

    /**
     * 构建函数
     * @param color {@link ChatColor}
     * @param english 英文
     * @param chinese 中文
     */
    @ParametersAreNonnullByDefault
    ChatColors(ChatColor color, String english, String chinese) {
        this.color = color;
        this.english = english;
        this.chinese = chinese;
    }

    /**
     * 获取中文名称
     *
     * @return 中文名称
     */
    @Override
    public String toString() {
        return this.getChinese();
    }

    /**
     * 获取带颜色代码的颜色名
     *
     * @return 带颜色代码的颜色名
     */
    public String toStringWithColor() {
        return this.getColor() +
            this.getChinese();
    }

    /**
     * 根据聊天颜色返回对应的枚举
     *
     * @param chatColor {@link ChatColor} 聊天颜色
     *
     * @return 对应的枚举
     */
    @Nonnull
    public static ChatColors fromChatColor(@Nonnull ChatColor chatColor) {
        Preconditions.checkNotNull(chatColor, "聊天颜色不能为空");

        return colorLookup.get(chatColor);
    }

    /**
     * 根据英文返回对应的枚举
     *
     * @param english {@link String} 提供的英文
     *
     * @return 对应的枚举
     */
    @Nullable
    public static ChatColors fromEnglish(@Nonnull String english) {
        Preconditions.checkNotNull(english, "英文不能为空");

        String humanized = StringUtil.humanize(english);
        for (ChatColors color : valuesCache) {
            if (color.getEnglish().equals(humanized)) {
                return color;
            }
        }
        return null;
    }
}
