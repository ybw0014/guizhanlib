package net.guizhanss.guizhanlib.utils;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Utility methods about player inventory.
 *
 * @author ybw0014
 */
@SuppressWarnings("ConstantConditions")
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
        Preconditions.checkArgument(p != null, "player should not be null");
        Preconditions.checkArgument(itemStacks != null, "at least one ItemStack is required");
        Preconditions.checkArgument(itemStacks.length > 0, "at least one ItemStack is required");

        // filter null ItemStacks
        itemStacks = Arrays.stream(itemStacks).filter(Objects::nonNull).toArray(ItemStack[]::new);

        Map<Integer, ItemStack> remainingItemMap = p.getInventory().addItem(itemStacks);

        for (ItemStack item : remainingItemMap.values()) {
            p.getWorld().dropItem(p.getLocation(), item.clone());
        }
    }
}
