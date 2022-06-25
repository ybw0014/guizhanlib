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
 * Utility methods about player head.
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("ConstantConditions")
public final class PlayerHeadUtil {
    /**
     * Get player head {@link ItemStack} from hash.
     *
     * @param hashCode Hash code of player head.
     * @return The {@link ItemStack} of player head.
     */
    @Nonnull
    public static ItemStack getFromHash(@Nonnull String hashCode) {
        Preconditions.checkArgument(hashCode != null, "hash code cannot be null");
        return PlayerHead.getItemStack(PlayerSkin.fromHashCode(hashCode));
    }

    /**
     * Get player head {@link SlimefunItemStack} from hash.
     * <p>
     * Can be used for resource packs.
     *
     * @param hashCode Hash code of player head.
     * @param id       The item id.
     * @return The {@link SlimefunItemStack} of player head.
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    public static SlimefunItemStack getFromHash(String hashCode, String id) {
        Preconditions.checkArgument(hashCode != null, "hash code cannot be null");
        return new SlimefunItemStack(id, getFromHash(hashCode));
    }

    /**
     * Get player head {@link ItemStack} from base64.
     *
     * @param base64 Base64 of player head.
     * @return The {@link ItemStack} of player head.
     */
    @Nonnull
    public static ItemStack getFromBase64(@Nonnull String base64) {
        Preconditions.checkArgument(base64 != null, "base64 cannot be null");
        return PlayerHead.getItemStack(PlayerSkin.fromBase64(base64));
    }

    /**
     * Get player head {@link SlimefunItemStack} from base64.
     * <p>
     * Can be used for resource packs.
     *
     * @param base64 Base64 of player head.
     * @param id       The item id.
     * @return The {@link SlimefunItemStack} of player head.
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    public static ItemStack getFromBase64(String base64, String id) {
        Preconditions.checkArgument(id != null, "id cannot be null");
        return new SlimefunItemStack(id, getFromBase64(base64));
    }
}
