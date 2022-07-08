package net.guizhanss.guizhanlib.slimefun.addon;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A class for scheduling tasks
 * Modified from InfinityLib
 *
 * @author Mooy1
 * @author ybw0014
 */
@ParametersAreNonnullByDefault
@SuppressWarnings("ConstantConditions")
public final class Scheduler {

    private final Plugin plugin;

    public Scheduler(@Nonnull Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "Plugin instance cannot be null");
        this.plugin = plugin;
    }

    public void run(@Nonnull Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public void runAsync(@Nonnull Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public void run(int delayTicks, @Nonnull Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks);
    }

    public void runAsync(int delayTicks, @Nonnull Runnable runnable) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delayTicks);
    }

    public void repeat(int intervalTicks, @Nonnull Runnable runnable) {
        repeat(intervalTicks, 1, runnable);
    }

    public void repeatAsync(int intervalTicks, @Nonnull Runnable runnable) {
        repeatAsync(intervalTicks, 1, runnable);
    }

    public void repeat(int intervalTicks, int delayTicks, @Nonnull Runnable runnable) {
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, Math.max(1, intervalTicks));
    }

    public void repeatAsync(int intervalTicks, int delayTicks, @Nonnull Runnable runnable) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delayTicks, Math.max(1, intervalTicks));
    }

}
