package net.guizhanss.minecraft.guizhanlib.minecraft.helper;

import lombok.experimental.UtilityClass;
import net.guizhanss.minecraft.guizhanlib.minecraft.LanguageHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;

/**
 * 物品({@link ItemStack})
 *
 * @author ybw0014
 */
@UtilityClass
public class ItemStackHelper {
    /**
     * 返回物品({@link ItemStack})的显示名称
     *
     * @param item {@link ItemStack} 物品
     * @return 物品的显示名称
     */
    public static @Nonnull String getItemDisplayName(@Nonnull ItemStack item) {
        Validate.notNull(item, "物品不能为空");

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
            return item.getItemMeta().getDisplayName();
        else
            return getItemName(item);
    }

    /**
     * 返回物品({@link ItemStack})的中文名称
     *
     * @param item {@link ItemStack} 物品
     * @return 物品的中文名称，如果获取失败则返回对应的键名
     */
    public static @Nonnull String getItemName(@Nonnull ItemStack item) {
        Validate.notNull(item, "物品不能为空");

        if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION || item.getType() == Material.TIPPED_ARROW) {
            String potion = ((PotionMeta) item.getItemMeta()).getBasePotionData().getType().toString().toLowerCase();
            return LanguageHelper.getLang(getMaterialKey(item.getType()) + ".effect." + potion);
        } else if (item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD) {
            return getPlayerSkullName(item);
        }

        return LanguageHelper.getLang(getMaterialKey(item.getType()));
    }

    /**
     * 返回材料({@link Material})的中文名称
     *
     * @param mat {@link Material} 材料
     * @return 材料的中文名称，如果获取失败则返回对应的键名
     */
    public static @Nonnull String getMaterialName(@Nonnull Material mat) {
        return LanguageHelper.getLang(getMaterialKey(mat));
    }

    /**
     * 返回头颅物品({@link ItemStack})的中文名称
     * @param skull {@link ItemStack} 头颅物品
     * @return 头颅物品的中文名称
     */
    private static @Nonnull String getPlayerSkullName(@Nonnull ItemStack skull) {
        Validate.notNull(skull, "物品不能为空");

        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null && meta.hasOwner()) {
            return String.format(LanguageHelper.getLang("block.minecraft.player_head.named"),
                meta.getOwningPlayer().getName());
        } else return LanguageHelper.getLang("block.minecraft.player_head");
    }

    /**
     * 获取物品材料({@link Material})的键名
     *
     * @param mat {@link Material} 物品材料
     * @return 物品材料的键名
     */
    public static @Nonnull String getMaterialKey(@Nonnull Material mat) {
        Validate.notNull(mat, "材料不能为空");

        return (mat.isBlock() ? "block" : "item") + "."
            + mat.getKey().getNamespace() + "."
            + mat.getKey().getKey();
    }
}
