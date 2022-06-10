package net.guizhanss.guizhanlib.utils;

import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import lombok.experimental.UtilityClass;
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
     * @return 玩家头颅物品 {@link ItemStack}
     */
    @Nonnull
    public static ItemStack getFromHash(@Nonnull String hashCode) {
        Preconditions.checkNotNull(hashCode, "hash code cannot be null");
        return PlayerHead.getItemStack(PlayerSkin.fromHashCode(hashCode));
    }

    /**
     * 使用hash获取玩家头颅物品 {@link ItemStack}，并指定物品ID。
     * 可用于定义材质包
     *
     * @param hashCode 玩家头颅 hash
     * @param id       物品 ID
     * @return 玩家头颅物品 {@link ItemStack}
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    public static ItemStack getFromHash(String hashCode, String id) {
        Preconditions.checkNotNull(hashCode, "id cannot be null");
        return new SlimefunItemStack(id, getFromHash(hashCode));
    }


    /**
     * 使用base64获取玩家头颅物品 {@link ItemStack}
     *
     * @param base64 玩家头颅 base64
     * @return 玩家头颅物品 {@link ItemStack}
     */
    @Nonnull
    public static ItemStack getFromBase64(@Nonnull String base64) {
        Preconditions.checkNotNull(base64, "base64 cannot be null");
        return PlayerHead.getItemStack(PlayerSkin.fromBase64(base64));
    }

    /**
     * 使用base64获取玩家头颅物品 {@link ItemStack}，并指定物品ID。
     * 可用于定义材质包
     *
     * @param base64 玩家头颅 base64
     * @param id     物品 ID
     * @return 玩家头颅物品 {@link ItemStack}
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    public static ItemStack getFromBase64(String base64, String id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        return new SlimefunItemStack(id, getFromBase64(base64));
    }
}
