package net.guizhanss.guizhanlib.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * 聊天相关工具包
 *
 * @author ybw0014
 */
@UtilityClass
public class ChatUtil {
    /**
     * 转换颜色代码
     * @param message 消息
     * @return 转换后的消息
     */
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * 向 {@link CommandSender} 发送消息
     * @param sender {@link CommandSender} 接收者
     * @param message 消息
     */
    public static void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }
}
