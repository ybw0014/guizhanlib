package net.guizhanss.guizhanlib.minecraft.helper.inventory;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import net.guizhanss.guizhanlib.minecraft.MinecraftTag;
import net.guizhanss.guizhanlib.minecraft.helper.MaterialHelper;
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
public final class ItemStackHelper {
    /**
     * 返回物品({@link ItemStack})的显示名称,
     * 如果无显示名称则返回中文名称
     *
     * @param item {@link ItemStack} 物品
     *
     * @return 物品的显示名称
     */
    @Nonnull
    public static String getDisplayName(@Nonnull ItemStack item) {
        Preconditions.checkNotNull(item, "物品不能为空");

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        } else {
            return getName(item);
        }
    }

    /**
     * 返回物品({@link ItemStack})的中文名称
     *
     * @param item {@link ItemStack} 物品
     *
     * @return 物品的中文名称，如果获取失败则返回对应的键名
     */
    @Nonnull
    public static String getName(@Nonnull ItemStack item) {
        Preconditions.checkNotNull(item, "物品不能为空");

        if (MinecraftTag.POTION_WITH_TIPPED_ARROW.isTagged(item)) {
            String potion = ((PotionMeta) item.getItemMeta()).getBasePotionData().getType().toString().toLowerCase();
            return LanguageHelper.getLangOrKey(MaterialHelper.getKey(item.getType()) + ".effect." + potion);
        } else if (item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD) {
            return getPlayerSkullName(item);
        }

        return MaterialHelper.getName(item.getType());
    }

    /**
     * 返回头颅物品({@link ItemStack})的中文名称
     *
     * @param skull {@link ItemStack} 头颅物品
     *
     * @return 头颅物品的中文名称
     */
    @Nonnull
    private static String getPlayerSkullName(@Nonnull ItemStack skull) {
        Preconditions.checkNotNull(skull, "物品不能为空");

        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null && meta.hasOwner()) {
            return String.format(LanguageHelper.getLangOrKey("block.minecraft.player_head.named"),
                meta.getOwningPlayer().getName());
        } else {
            return LanguageHelper.getLangOrKey("block.minecraft.player_head");
        }
    }
}
