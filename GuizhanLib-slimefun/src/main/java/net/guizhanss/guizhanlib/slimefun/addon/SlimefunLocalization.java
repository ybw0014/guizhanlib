package net.guizhanss.guizhanlib.slimefun.addon;

import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import lombok.Setter;
import net.guizhanss.guizhanlib.localization.MinecraftLocalization;
import net.guizhanss.guizhanlib.minecraft.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

/**
 * Localization service for Slimefun addons.
 *
 * @author ybw0014
 */
@SuppressWarnings("ConstantConditions")
public class SlimefunLocalization extends MinecraftLocalization {

    private static final String KEY_NAME = ".name.";
    private static final String KEY_LORE = ".lore.";
    private static final String MSG_ID_NULL = "id cannot be null";
    private static final String MSG_MATERIAL_NULL = "Material cannot be null";
    private static final String MSG_ITEMSTACK_NULL = "ItemStack cannot be null";
    private static final String MSG_TEXTURE_NULL = "Texture cannot be null";

    /**
     * The key of item groups in language file.
     */
    @Setter
    private String itemGroupKey = "categories";

    /**
     * They key of items in language file.
     */
    @Setter
    private String itemsKey = "items";

    /**
     * Constructor
     *
     * @param plugin
     *     The {@link JavaPlugin} instance
     */
    @ParametersAreNonnullByDefault
    public SlimefunLocalization(JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Constructor
     *
     * @param plugin
     *     The {@link JavaPlugin} instance
     * @param folderName
     *     The name of the folder that holds all language files
     */
    @ParametersAreNonnullByDefault
    public SlimefunLocalization(JavaPlugin plugin, String folderName) {
        super(plugin, folderName);
    }

    /**
     * Constructor
     *
     * @param plugin
     *     The {@link JavaPlugin} instance
     * @param folderName
     *     The name of the folder that holds all language files
     * @param langFile
     *     The name of language file (without file extension .yml)
     */
    @ParametersAreNonnullByDefault
    public SlimefunLocalization(JavaPlugin plugin, String folderName, String langFile) {
        super(plugin, folderName, langFile);
    }

    /**
     * Get the {@link SlimefunItemStack} for ItemGroup.
     *
     * @param id
     *     The id of category.
     * @param itemStack
     *     The {@link ItemStack} of item.
     *
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItemGroupItem(String id, ItemStack itemStack) {
        Preconditions.checkArgument(id != null, MSG_ID_NULL);
        Preconditions.checkArgument(itemStack != null, MSG_ITEMSTACK_NULL);

        id = id.toLowerCase(Locale.ROOT);
        return new SlimefunItemStack(
            id,
            itemStack,
            getString(itemGroupKey + KEY_NAME + id)
        );
    }

    /**
     * Get the {@link SlimefunItemStack} for ItemGroup.
     *
     * @param id
     *     The id of category.
     * @param material
     *     The {@link Material} of item.
     *
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItemGroupItem(String id, Material material) {
        Preconditions.checkArgument(id != null, MSG_ID_NULL);
        Preconditions.checkArgument(material != null, MSG_MATERIAL_NULL);

        id = id.toLowerCase(Locale.ROOT);
        return new SlimefunItemStack(
            id,
            material,
            getString(itemGroupKey + KEY_NAME + id)
        );
    }

    /**
     * Get the {@link SlimefunItemStack} for ItemGroup.
     *
     * @param id
     *     The id of category.
     * @param texture
     *     The texture of head.
     *
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItemGroupItem(String id, String texture) {
        Preconditions.checkArgument(id != null, MSG_ID_NULL);
        Preconditions.checkArgument(texture != null, MSG_TEXTURE_NULL);

        id = id.toLowerCase(Locale.ROOT);
        return new SlimefunItemStack(
            id,
            texture,
            getString(itemGroupKey + KEY_NAME + id)
        );
    }

    /**
     * Get the {@link SlimefunItemStack} with specified id and {@link Material}.
     *
     * @param id
     *     The id of item.
     * @param material
     *     The {@link Material} of item.
     *
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItem(String id, Material material) {
        Preconditions.checkArgument(id != null, MSG_ID_NULL);
        Preconditions.checkArgument(material != null, MSG_MATERIAL_NULL);

        id = id.toLowerCase(Locale.ROOT);
        return new SlimefunItemStack(
            id,
            material,
            getString(itemsKey + KEY_NAME + id),
            getStringArray(itemsKey + KEY_LORE + id)
        );
    }

    /**
     * Get the {@link SlimefunItemStack} with specified id and {@link Material}.
     * The extra lore will be appended to the end of current lore.
     *
     * @param id
     *     The id of item.
     * @param material
     *     The {@link Material} of item.
     * @param appendLore
     *     The extra lore of item.
     *
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItem(String id, Material material, String... appendLore) {
        return (SlimefunItemStack) ItemUtil.appendLore(getItem(id, material), appendLore);
    }

    /**
     * Get the {@link SlimefunItemStack} with specified id and head texture.
     *
     * @param id
     *     The id of item.
     * @param texture
     *     The texture of head.
     *
     * @return The {@link SlimefunItemStack} with specified id and head texture.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItem(String id, String texture) {
        Preconditions.checkArgument(id != null, MSG_ID_NULL);
        Preconditions.checkArgument(texture != null, MSG_TEXTURE_NULL);

        id = id.toLowerCase(Locale.ROOT);
        return new SlimefunItemStack(
            id,
            texture,
            getString(itemsKey + KEY_NAME + id),
            getStringArray(itemsKey + KEY_LORE + id)
        );
    }

    /**
     * Get the {@link SlimefunItemStack} with specified id and head texture.
     * The extra lore will be appended to the end of current lore.
     *
     * @param id
     *     The id of item.
     * @param texture
     *     The texture of head.
     * @param appendLore
     *     The extra lore of item.
     *
     * @return The {@link SlimefunItemStack} with specified id and head texture.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItem(String id, String texture, String... appendLore) {
        return (SlimefunItemStack) ItemUtil.appendLore(getItem(id, texture), appendLore);
    }

    /**
     * Get the {@link SlimefunItemStack} with specified id and {@link ItemStack}.
     *
     * @param id
     *     The id of item.
     * @param itemStack
     *     The {@link ItemStack} of item.
     *
     * @return The {@link SlimefunItemStack} with specified id and {@link ItemStack}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItem(String id, ItemStack itemStack) {
        Preconditions.checkArgument(id != null, MSG_ID_NULL);
        Preconditions.checkArgument(itemStack != null, MSG_ITEMSTACK_NULL);

        id = id.toLowerCase(Locale.ROOT);
        return new SlimefunItemStack(
            id,
            itemStack,
            getString(itemsKey + KEY_NAME + id),
            getStringArray(itemsKey + KEY_LORE + id)
        );
    }

    /**
     * Get the {@link SlimefunItemStack} with specified id and {@link ItemStack}.
     * The extra lore will be appended to the end of current lore.
     *
     * @param id
     *     The id of item.
     * @param itemStack
     *     The {@link ItemStack} of item.
     * @param appendLore
     *     The extra lore of item.
     *
     * @return The {@link SlimefunItemStack} with specified id and {@link ItemStack}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItem(String id, ItemStack itemStack, String... appendLore) {
        return (SlimefunItemStack) ItemUtil.appendLore(getItem(id, itemStack), appendLore);
    }
}
