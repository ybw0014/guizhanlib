package net.guizhanss.guizhanlib.minecraft.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility methods about {@link ItemStack}.
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("deprecation")
public class ItemUtil {
    /**
     * Append lore to an {@link ItemStack}.
     *
     * @param itemStack
     *     The {@link ItemStack} to append lore.
     * @param extraLore
     *     The lore to append.
     *
     * @return The {@link ItemStack} with lore appended.
     */
    public static ItemStack appendLore(ItemStack itemStack, String... extraLore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.addAll(ChatUtil.color(Arrays.asList(extraLore)));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
