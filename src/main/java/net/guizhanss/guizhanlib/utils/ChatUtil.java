package net.guizhanss.guizhanlib.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.MessageFormat;

/**
 * 聊天相关工具包
 *
 * @author ybw0014
 */
@UtilityClass
public final class ChatUtil {
    /**
     * 转换颜色代码
     *
     * @param message 消息
     * @return 转换后的消息
     */
    @Nonnull
    public static String color(@Nonnull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * 向 {@link CommandSender} 发送消息
     *
     * @param sender  {@link CommandSender} 接收者
     * @param message 消息
     */
    @ParametersAreNonnullByDefault
    public static void send(CommandSender sender, String message, Object... args) {
        sender.sendMessage(color(MessageFormat.format(message, args)));
    }
}
