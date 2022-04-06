package net.guizhanss.guizhanlib.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 玩家背包相关工具包
 *
 * @author ybw0014
 */
@UtilityClass
public class InventoryUtil {
    /**
     * This method push all {@link ItemStack} to {@link Player}'s inventory
     * if some items cannot be pushed, they will be dropped at {@link Player}'s {@link Location}
     *
     * @param p the {@link Player}
     * @param itemStacks all the {@link ItemStack}s to be pushed
     */
    public static void push(@Nonnull Player p, @Nonnull ItemStack... itemStacks) {
        Validate.notNull(p, "player should not be null");
        Validate.notNull(itemStacks, "at least one ItemStack is required");

        // filter null ItemStacks
        itemStacks = Arrays.stream(itemStacks).filter(Objects::nonNull).toArray(ItemStack[]::new);

        Map<Integer, ItemStack> remainingItemMap = p.getInventory().addItem(itemStacks);

        for (ItemStack item : remainingItemMap.values()) {
            p.getWorld().dropItem(p.getLocation(), item.clone());
        }
    }
}
