package net.guizhanss.guizhanlib.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 包含玩家头颅相关工具包
 *
 * @author ybw0014
 */
@UtilityClass
public class PlayerHeadUtil {
    /**
     * 使用hash获取玩家头颅物品 {@link ItemStack}
     *
     * @param hashCode 玩家头颅 hash
     *
     * @return 玩家头颅物品 {@link ItemStack}
     */
    public static @Nonnull ItemStack getFromHash(@Nonnull String hashCode) {
        Validate.notNull(hashCode, "hash code cannot be null");
        return PlayerHead.getItemStack(PlayerSkin.fromHashCode(hashCode));
    }

    /**
     * 使用hash获取玩家头颅物品 {@link ItemStack}，并指定物品ID。
     * 可用于定义材质包
     *
     * @param hashCode 玩家头颅 hash
     * @param id 物品 ID
     *
     * @return 玩家头颅物品 {@link ItemStack}
     */
    @ParametersAreNonnullByDefault
    public static @Nonnull ItemStack getFromHash(String hashCode, String id) {
        Validate.notNull(hashCode, "id cannot be null");
        return new SlimefunItemStack(id, getFromHash(hashCode));
    }


    /**
     * 使用base64获取玩家头颅物品 {@link ItemStack}
     *
     * @param base64 玩家头颅 base64
     *
     * @return 玩家头颅物品 {@link ItemStack}
     */
    public static @Nonnull ItemStack getFromBase64(@Nonnull String base64) {
        Validate.notNull(base64, "base64 cannot be null");
        return PlayerHead.getItemStack(PlayerSkin.fromBase64(base64));
    }

    /**
     * 使用base64获取玩家头颅物品 {@link ItemStack}，并指定物品ID。
     * 可用于定义材质包
     *
     * @param base64 玩家头颅 base64
     * @param id 物品 ID
     *
     * @return 玩家头颅物品 {@link ItemStack}
     */
    @ParametersAreNonnullByDefault
    public static @Nonnull ItemStack getFromBase64(String base64, String id) {
        Validate.notNull(id, "id cannot be null");
        return new SlimefunItemStack(id, getFromBase64(base64));
    }
}
