package net.guizhanss.guizhanlib.utils;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 玩家背包相关工具包
 *
 * @author ybw0014
 */
@UtilityClass
public final class InventoryUtil {
    /**
     * This method push all {@link ItemStack} to {@link Player}'s inventory
     * if some items cannot be pushed, they will be dropped at {@link Player}'s {@link Location}
     *
     * @param p          the {@link Player}
     * @param itemStacks all the {@link ItemStack}s to be pushed
     */
    @ParametersAreNonnullByDefault
    public static void push(Player p, ItemStack... itemStacks) {
        Preconditions.checkNotNull(p, "player should not be null");
        Preconditions.checkNotNull(itemStacks, "at least one ItemStack is required");
        Preconditions.checkArgument(itemStacks.length > 0, "at least one ItemStack is required");

        // filter null ItemStacks
        itemStacks = Arrays.stream(itemStacks).filter(Objects::nonNull).toArray(ItemStack[]::new);

        Map<Integer, ItemStack> remainingItemMap = p.getInventory().addItem(itemStacks);

        for (ItemStack item : remainingItemMap.values()) {
            p.getWorld().dropItem(p.getLocation(), item.clone());
        }
    }
}
