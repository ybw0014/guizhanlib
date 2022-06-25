package net.guizhanss.guizhanlib.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility methods about chatting.
 *
 * @author ybw0014
 */
@UtilityClass
public final class ChatUtil {
    /**
     * Translate color code of a {@link String}.
     *
     * @param str The {@link String} to be translated.
     * @return String with color code translated.
     */
    @Nonnull
    public static String color(@Nonnull String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    /**
     * Translate color code of a {@link String} {@link List}.
     *
     * @param strList The {@link String} {@link List} to be translated.
     * @return {@link String} {@link List} with color code translated.
     */
    @Nonnull
    public static List<String> color(@Nonnull List<String> strList) {
        return strList.stream().map(ChatUtil::color).collect(Collectors.toList());
    }

    /**
     * Send message to {@link CommandSender}, the color codes in the message will be translated
     *
     * @param sender  {@link CommandSender} 接收者
     * @param message 消息
     */
    @ParametersAreNonnullByDefault
    public static void send(CommandSender sender, String message, Object... args) {
        sender.sendMessage(color(MessageFormat.format(message, args)));
    }
}
