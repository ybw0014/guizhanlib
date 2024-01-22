package net.guizhanss.guizhanlib.minecraft.utils;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
@SuppressWarnings("ConstantConditions")
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
        Preconditions.checkArgument(strList != null, "String list cannot be null");
        return strList.stream().map(ChatUtil::color).collect(Collectors.toList());
    }

    /**
     * Send message to {@link CommandSender}, the color codes in the message will be translated.
     * <p>
     * Will use {@code MessageFormat.format()} to format the message.
     *
     * @param sender  {@link CommandSender}
     * @param message Message
     * @param args    Arguments
     */
    @ParametersAreNonnullByDefault
    public static void send(CommandSender sender, String message, Object... args) {
        sender.sendMessage(color(MessageFormat.format(message, args)));
    }

    /**
     * Send action bar message to {@link Player}, the color codes in the message will be translated.
     * <p>
     * Will use {@code MessageFormat.format()} to format the message.
     *
     * @param player  {@link Player}
     * @param message Message
     * @param args    Arguments
     */
    @ParametersAreNonnullByDefault
    public static void sendActionBar(Player player, String message, Object... args) {
        BaseComponent[] components = TextComponent.fromLegacyText(color(MessageFormat.format(message, args)));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
    }
}
