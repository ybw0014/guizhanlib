package net.guizhanss.guizhanlib.common;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.slimefun.addon.AbstractAddon;
import org.bukkit.Bukkit;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A class for scheduling tasks
 * Modified from InfinityLib
 *
 * @author Mooy1
 * @author ybw0014
 */
@UtilityClass
@ParametersAreNonnullByDefault
public final class Scheduler {

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(AbstractAddon.getInstance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(AbstractAddon.getInstance(), runnable);
    }

    public static void run(int delayTicks, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(AbstractAddon.getInstance(), runnable, delayTicks);
    }

    public static void runAsync(int delayTicks, Runnable runnable) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(AbstractAddon.getInstance(), runnable, delayTicks);
    }

    public static void repeat(int intervalTicks, Runnable runnable) {
        repeat(intervalTicks, 1, runnable);
    }

    public static void repeatAsync(int intervalTicks, Runnable runnable) {
        repeatAsync(intervalTicks, 1, runnable);
    }

    public static void repeat(int intervalTicks, int delayTicks, Runnable runnable) {
        Bukkit.getScheduler().runTaskTimer(AbstractAddon.getInstance(), runnable, delayTicks, Math.max(1, intervalTicks));
    }

    public static void repeatAsync(int intervalTicks, int delayTicks, Runnable runnable) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(AbstractAddon.getInstance(), runnable, delayTicks, Math.max(1, intervalTicks));
    }

}
