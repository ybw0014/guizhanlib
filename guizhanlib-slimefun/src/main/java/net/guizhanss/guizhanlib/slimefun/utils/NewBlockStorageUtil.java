package net.guizhanss.guizhanlib.slimefun.utils;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * BlockStorage utilities for Simplified Chinese fork of Slimefun4.
 *
 * @author ybw0014
 */
@UtilityClass
public final class NewBlockStorageUtil {

    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunBlockData createBlock(Location l, String sfId) {
        return Slimefun.getDatabaseManager().getBlockDataController().createBlock(l, sfId);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunBlockData createBlock(Block b, String sfId) {
        return createBlock(b.getLocation(), sfId);
    }

    public static void removeBlock(@Nonnull Location l) {
        Slimefun.getDatabaseManager().getBlockDataController().removeBlock(l);
    }

    public static void removeBlock(@Nonnull Block b) {
        removeBlock(b.getLocation());
    }

    public static boolean hasBlock(@Nonnull Location l) {
        return StorageCacheUtils.hasBlock(l);
    }

    public static boolean hasBlock(@Nonnull Block b) {
        return hasBlock(b.getLocation());
    }

    @Nullable
    public static SlimefunBlockData getBlock(@Nonnull Location l) {
        return StorageCacheUtils.getBlock(l);
    }

    @Nullable
    public static SlimefunBlockData getBlock(@Nonnull Block b) {
        return getBlock(b.getLocation());
    }

    @ParametersAreNonnullByDefault
    public static boolean isBlock(Location l, String sfId) {
        return StorageCacheUtils.isBlock(l, sfId);
    }

    @ParametersAreNonnullByDefault
    public static boolean isBlock(Block b, String sfId) {
        return isBlock(b.getLocation(), sfId);
    }

    @ParametersAreNonnullByDefault
    public static boolean isBlock(Location l, SlimefunItem sfItem) {
        SlimefunBlockData data = getBlock(l);
        return data != null && data.getSfId().equals(sfItem.getId());
    }

    @ParametersAreNonnullByDefault
    public static boolean isBlock(Block b, SlimefunItem sfItem) {
        return isBlock(b.getLocation(), sfItem);
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public static String getData(Location l, String key) {
        return StorageCacheUtils.getData(l, key);
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public static String getData(Block b, String key) {
        return getData(b.getLocation(), key);
    }

    public static void setData(@Nonnull Location l, @Nonnull String key, @Nullable String value) {
        if (value != null) {
            StorageCacheUtils.setData(l, key, value);
        } else {
            StorageCacheUtils.removeData(l, key);
        }
    }

    public static void setData(@Nonnull Block b, @Nonnull String key, @Nullable String value) {
        setData(b.getLocation(), key, value);
    }

    @Nullable
    public static BlockMenu getMenu(@Nonnull Location l) {
        return StorageCacheUtils.getMenu(l);
    }

    @Nullable
    public static BlockMenu getMenu(@Nonnull Block b) {
        return getMenu(b.getLocation());
    }

    public static void requestLoad(@Nonnull SlimefunBlockData data) {
        StorageCacheUtils.requestLoad(data);
    }

    public static void requestLoad(@Nonnull Location l) {
        SlimefunBlockData data = getBlock(l);
        if (data == null) {
            return;
        }
        StorageCacheUtils.requestLoad(data);
    }

    public static void requestLoad(@Nonnull Block b) {
        requestLoad(b.getLocation());
    }

    @ParametersAreNonnullByDefault
    public static void executeAfterLoad(SlimefunBlockData data, Runnable runnable, boolean runOnMainThread) {
        StorageCacheUtils.executeAfterLoad(data, runnable, runOnMainThread);
    }

    @ParametersAreNonnullByDefault
    public static void executeAfterLoad(Location l, Runnable runnable, boolean runOnMainThread) {
        SlimefunBlockData data = getBlock(l);
        if (data == null) {
            return;
        }
        executeAfterLoad(data, runnable, runOnMainThread);
    }

    @ParametersAreNonnullByDefault
    public static void executeAfterLoad(Block b, Runnable runnable, boolean runOnMainThread) {
        executeAfterLoad(b.getLocation(), runnable, runOnMainThread);
    }
}
