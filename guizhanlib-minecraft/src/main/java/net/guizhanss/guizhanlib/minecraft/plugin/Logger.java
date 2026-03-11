package net.guizhanss.guizhanlib.minecraft.plugin;

import com.google.common.base.Preconditions;
import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.MessageFormat;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * A wrapper for {@link Plugin}'s {@link java.util.logging.Logger} that automatically formats messages with {@link MessageFormat} and color codes.
 *
 * @author ybw0014
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"ConstantConditions", "unused"})
public final class Logger {
    private final Plugin plugin;

    /**
     * Create a new {@link Logger} instance.
     *
     * @param plugin the plugin instance
     */
    public Logger(Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "Plugin instance cannot be null");
        this.plugin = plugin;
    }

    /**
     * Get the {@link Plugin}'s {@link java.util.logging.Logger} instance.
     *
     * @return the {@link java.util.logging.Logger} instance
     */
    public java.util.logging.Logger getLogger() {
        return plugin.getLogger();
    }

    @Nonnull
    private String formatMessage(String msg, @Nullable Object... args) {
        return ChatUtil.color(MessageFormat.format(msg, args));
    }

    /**
     * Log a message.
     *
     * @param level       the log level
     * @param msgSupplier the message supplier
     * @param args        the message arguments
     */
    public void log(Level level, Supplier<String> msgSupplier, @Nullable Object... args) {
        if (!getLogger().isLoggable(level)) {
            return;
        }
        getLogger().log(level, formatMessage(msgSupplier.get(), args));
    }

    /**
     * Log a throwable with a message.
     *
     * @param level       the log level
     * @param msgSupplier the message supplier
     * @param throwable   the throwable to log
     */
    public void logThrowable(Level level, Supplier<String> msgSupplier, Throwable throwable) {
        if (!getLogger().isLoggable(level)) {
            return;
        }
        getLogger().log(level, formatMessage(msgSupplier.get()), throwable);
    }
}
