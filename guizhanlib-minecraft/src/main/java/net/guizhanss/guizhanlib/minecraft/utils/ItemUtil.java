package net.guizhanss.guizhanlib.minecraft.utils;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
     * @param itemStack The {@link ItemStack} to append lore.
     * @param extraLore The extra lore to append.
     * @return The original {@link ItemStack} with lore appended.
     */
    public static <T extends ItemStack> T appendLore(@Nonnull T itemStack, @Nullable String... extraLore) {
        Preconditions.checkArgument(itemStack != null, "ItemStack cannot be null");

        if (extraLore == null || extraLore.length == 0) {
            return itemStack;
        }
        return appendLore(itemStack, Arrays.asList(extraLore));
    }

    /**
     * Append lore to an {@link ItemStack}.
     *
     * @param itemStack The {@link ItemStack} to append lore.
     * @param extraLore The extra lore to append.
     * @return The original {@link ItemStack} with lore appended.
     */
    public static <T extends ItemStack> T appendLore(@Nonnull T itemStack, @Nonnull List<String> extraLore) {
        Preconditions.checkArgument(itemStack != null, "ItemStack cannot be null");
        Preconditions.checkArgument(extraLore != null, "extraLore cannot be null");

        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.addAll(ChatUtil.color(extraLore));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Check if the given {@link ItemStack} is not null or air.
     * @param itemStack The {@link ItemStack} to check.
     * @return True if the item is valid.
     */
    public static boolean isValid(@Nullable ItemStack itemStack) {
        return itemStack != null && !itemStack.getType().isAir();
    }

    /**
     * Remove the damage from an {@link ItemStack}.
     *
     * @param item The {@link ItemStack} to remove the damage from.
     * @return A cloned {@link ItemStack} with 0 damage.
     */
    @Nonnull
    public static ItemStack removeDamage(@Nonnull ItemStack item) {
        Preconditions.checkArgument(item != null, "Item cannot be null");

        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();
        if (meta instanceof Damageable damageable) {
            damageable.setDamage(0);
            clone.setItemMeta(meta);
        }
        return clone;
    }

    /**
     * This method checks if two {@link ItemStack}s can stack together.
     *
     * @param a The item A to check.
     * @param b The item B to check.
     * @return True if the two items can stack together.
     */
    public static boolean canStack(@Nullable ItemStack a, @Nullable ItemStack b) {
        if (a != null && b != null) {
            if (a.getType() == b.getType() && a.hasItemMeta() == b.hasItemMeta()) {
                if (a.hasItemMeta()) {
                    ItemMeta aMeta = a.getItemMeta();
                    ItemMeta bMeta = b.getItemMeta();
                    if (aMeta instanceof Damageable != (bMeta instanceof Damageable)) {
                        return false;
                    }

                    if (aMeta instanceof Damageable damageable && damageable.getDamage() != ((Damageable) bMeta).getDamage()) {
                        return false;
                    }

                    if (aMeta instanceof LeatherArmorMeta != (bMeta instanceof LeatherArmorMeta)) {
                        return false;
                    }

                    if (aMeta instanceof LeatherArmorMeta leatherArmorMeta && !leatherArmorMeta.getColor().equals(((LeatherArmorMeta) bMeta).getColor())) {
                        return false;
                    }

                    if (aMeta.hasCustomModelData() != bMeta.hasCustomModelData()) {
                        return false;
                    }

                    if (aMeta.hasCustomModelData() && aMeta.getCustomModelData() != bMeta.getCustomModelData()) {
                        return false;
                    }

                    if (!aMeta.getPersistentDataContainer().equals(bMeta.getPersistentDataContainer())) {
                        return false;
                    }

                    if (!aMeta.getEnchants().equals(bMeta.getEnchants())) {
                        return false;
                    }

                    if (aMeta.hasDisplayName() != bMeta.hasDisplayName()) {
                        return false;
                    }

                    if (aMeta.hasDisplayName() && !aMeta.getDisplayName().equals(bMeta.getDisplayName())) {
                        return false;
                    }

                    if (aMeta.hasLore() != bMeta.hasLore()) {
                        return false;
                    }

                    if (aMeta.hasLore()) {
                        List<String> aLore = aMeta.getLore();
                        List<String> bLore = bMeta.getLore();
                        if (aLore.size() != bLore.size()) {
                            return false;
                        }

                        for (int i = 0; i < aLore.size(); ++i) {
                            if (!aLore.get(i).equals(bLore.get(i))) {
                                return false;
                            }
                        }
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
