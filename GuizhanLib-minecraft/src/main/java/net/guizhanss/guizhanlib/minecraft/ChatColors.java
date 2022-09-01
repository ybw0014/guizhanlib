package net.guizhanss.guizhanlib.minecraft;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;

/**
 * 聊天颜色
 *
 * @author ybw0014
 * @see ChatColor
 */
@SuppressWarnings("ConstantConditions")
public enum ChatColors {
    /**
     * 天蓝色
     */
    AQUA(ChatColor.AQUA, "天蓝色"),
    /**
     * 黑色
     */
    BLACK(ChatColor.BLACK, "黑色"),
    /**
     * 蓝色
     */
    BLUE(ChatColor.BLUE, "蓝色"),
    /**
     * 加粗
     */
    BOLD(ChatColor.BOLD, "加粗"),
    /**
     * 湖蓝色
     */
    DARK_AQUA(ChatColor.DARK_AQUA, "湖蓝色"),
    /**
     * 深蓝色
     */
    DARK_BLUE(ChatColor.DARK_BLUE, "深蓝色"),
    /**
     * 深灰色
     */
    DARK_GRAY(ChatColor.DARK_GRAY, "深灰色"),
    /**
     * 深绿色
     */
    DARK_GREEN(ChatColor.DARK_GREEN, "深绿色"),
    /**
     * 紫色
     */
    DARK_PURPLE(ChatColor.DARK_PURPLE, "紫色"),
    /**
     * 深红色
     */
    DARK_RED(ChatColor.DARK_RED, "深红色"),
    /**
     * 金色
     */
    GOLD(ChatColor.GOLD, "金色"),
    /**
     * 灰色
     */
    GRAY(ChatColor.GRAY, "灰色"),
    /**
     * 绿色
     */
    GREEN(ChatColor.GREEN, "绿色"),
    /**
     * 斜体
     */
    ITALIC(ChatColor.ITALIC, "斜体"),
    /**
     * 粉红色
     */
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, "粉红色"),
    /**
     * 随机
     */
    MAGIC(ChatColor.MAGIC, "随机"),
    /**
     * 红色
     */
    RED(ChatColor.RED, "红色"),
    /**
     * 重置
     */
    RESET(ChatColor.RESET, "重置"),
    /**
     * 删除线
     */
    STRIKETHROUGH(ChatColor.STRIKETHROUGH, "删除线"),
    /**
     * 下划线
     */
    UNDERLINE(ChatColor.UNDERLINE, "下划线"),
    /**
     * 白色
     */
    WHITE(ChatColor.WHITE, "白色"),
    /**
     * 黄色
     */
    YELLOW(ChatColor.YELLOW, "黄色");

    /**
     * 颜色代码前缀
     */
    public static final char COLOR_CHAR = '\u00a7';

    private static final ChatColors[] valuesCache = values();
    private static final Map<ChatColor, ChatColors> colorLookup = new EnumMap<>(ChatColor.class);

    static {
        for (ChatColors color : valuesCache) {
            colorLookup.put(color.getColor(), color);
        }
    }

    @Getter
    private final ChatColor color;
    @Getter
    private final String chinese;

    /**
     * 构造函数
     *
     * @param color   {@link ChatColor}
     * @param chinese 中文
     */
    @ParametersAreNonnullByDefault
    ChatColors(ChatColor color, String chinese) {
        this.color = color;
        this.chinese = chinese;
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
        Preconditions.checkArgument(chatColor != null, "聊天颜色不能为空");

        return colorLookup.get(chatColor);
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
}
