package net.guizhanss.guizhanlib.slimefun.addon;

import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import lombok.Setter;
import net.guizhanss.guizhanlib.minecraft.localization.MinecraftLocalization;
import net.guizhanss.guizhanlib.minecraft.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

    private static final String KEY_NAME = ".name";
    private static final String KEY_LORE = ".lore";
    private static final String MSG_KEY_NULL = "key cannot be null";
    private static final String MSG_ID_NULL = "id cannot be null";
    private static final String MSG_MATERIAL_NULL = "Material cannot be null";
    private static final String MSG_ITEMSTACK_NULL = "ItemStack cannot be null";
    private static final String MSG_TEXTURE_NULL = "Texture cannot be null";

    @Getter
    @Setter
    private String idPrefix = "";

    /**
     * The key of ItemGroups in language file.
     */
    @Setter
    private String itemGroupKey = "categories";

    /**
     * The key of items in language file.
     */
    @Setter
    private String itemsKey = "items";

    /**
     * The key of recipe types in language file.
     */
    @Setter
    private String recipesKey = "recipes";

    /**
     * Constructor
     *
     * @param plugin The {@link JavaPlugin} instance
     */
    @ParametersAreNonnullByDefault
    public SlimefunLocalization(JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Constructor
     *
     * @param plugin     The {@link JavaPlugin} instance
     * @param folderName The name of the folder that holds all language files
     */
    @ParametersAreNonnullByDefault
    public SlimefunLocalization(JavaPlugin plugin, String folderName) {
        super(plugin, folderName);
    }

    /**
     * Constructor
     *
     * @param plugin     The {@link JavaPlugin} instance
     * @param folderName The name of the folder that holds all language files
     * @param langFile   The name of language file (without file extension .yml)
     */
    @ParametersAreNonnullByDefault
    public SlimefunLocalization(JavaPlugin plugin, String folderName, String langFile) {
        super(plugin, folderName, langFile);
    }

    /**
     * Get the {@link SlimefunItemStack} with specified key, id and {@link Material}.
     *
     * @param key       The key to read item from in language file.
     * @param id        The id of item.
     * @param material  The {@link Material} of item.
     * @param extraLore The extra lore to append to the end of current lore.
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItemBy(String key, String id, Material material, String... extraLore) {
        Preconditions.checkArgument(key != null, MSG_KEY_NULL);
        Preconditions.checkArgument(id != null, MSG_ID_NULL);
        Preconditions.checkArgument(material != null, MSG_MATERIAL_NULL);

        return ItemUtil.appendLore(
            new SlimefunItemStack(
                (idPrefix + id).toUpperCase(Locale.ROOT),
                material,
                getString(key + "." + id + KEY_NAME),
                getStringArray(key + "." + id + KEY_LORE)
            ),
            extraLore
        );
    }

    /**
     * Get the {@link SlimefunItemStack} with specified key, id and head texture.
     *
     * @param key       The key to read item from in language file.
     * @param id        The id of item.
     * @param texture   The texture of head.
     * @param extraLore The extra lore to append to the end of current lore.
     * @return The {@link SlimefunItemStack} with specified id and head texture.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItemBy(String key, String id, String texture, String... extraLore) {
        Preconditions.checkArgument(key != null, MSG_KEY_NULL);
        Preconditions.checkArgument(id != null, MSG_ID_NULL);
        Preconditions.checkArgument(texture != null, MSG_TEXTURE_NULL);

        return ItemUtil.appendLore(
            new SlimefunItemStack(
                (idPrefix + id).toUpperCase(Locale.ROOT),
                texture,
                getString(key + "." + id + KEY_NAME),
                getStringArray(key + "." + id + KEY_LORE)
            ),
            extraLore
        );
    }

    /**
     * Get the {@link SlimefunItemStack} with specified key, id and {@link ItemStack}.
     *
     * @param key       The key to read item from in language file.
     * @param id        The id of item.
     * @param itemStack The {@link ItemStack}.
     * @param extraLore The extra lore to append to the end of current lore.
     * @return The {@link SlimefunItemStack} with specified id and {@link ItemStack}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItemBy(String key, String id, ItemStack itemStack, String... extraLore) {
        Preconditions.checkArgument(key != null, MSG_KEY_NULL);
        Preconditions.checkArgument(id != null, MSG_ID_NULL);
        Preconditions.checkArgument(itemStack != null, MSG_ITEMSTACK_NULL);

        return ItemUtil.appendLore(
            new SlimefunItemStack(
                (idPrefix + id).toUpperCase(Locale.ROOT),
                itemStack,
                getString(key + "." + id + KEY_NAME),
                getStringArray(key + "." + id + KEY_LORE)
            ),
            extraLore
        );
    }

    /**
     * Get the {@link SlimefunItemStack} for ItemGroup.
     *
     * @param id       The id of category.
     * @param material The {@link Material} of item.
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItemGroupItem(String id, Material material) {
        return getItemBy(itemGroupKey, id, material);
    }

    /**
     * Get the {@link SlimefunItemStack} for ItemGroup.
     *
     * @param id      The id of category.
     * @param texture The texture of head.
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItemGroupItem(String id, String texture) {
        return getItemBy(itemGroupKey, id, texture);
    }

    /**
     * Get the {@link SlimefunItemStack} for ItemGroup.
     *
     * @param id        The id of category.
     * @param itemStack The {@link ItemStack} of item.
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItemGroupItem(String id, ItemStack itemStack) {
        return getItemBy(itemGroupKey, id, itemStack);
    }

    /**
     * Get the {@link SlimefunItemStack} with specified id and {@link Material}.
     *
     * @param id        The id of item.
     * @param material  The {@link Material} of item.
     * @param extraLore The extra lore to append to the end of current lore.
     * @return The {@link SlimefunItemStack} with specified id and {@link Material}.
     */
    public SlimefunItemStack getItem(String id, Material material, String... extraLore) {
        return getItemBy(itemsKey, id, material, extraLore);
    }

    /**
     * Get the {@link SlimefunItemStack} with specified id and head texture.
     *
     * @param id        The id of item.
     * @param texture   The texture of head.
     * @param extraLore The extra lore to append to the end of current lore.
     * @return The {@link SlimefunItemStack} with specified id and head texture.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItem(String id, String texture, String... extraLore) {
        return getItemBy(itemsKey, id, texture, extraLore);
    }

    /**
     * Get the {@link SlimefunItemStack} with specified id and {@link Material}.
     *
     * @param id        The id of item.
     * @param itemStack The {@link ItemStack}.
     * @param extraLore The extra lore to append to the end of current lore.
     * @return The {@link SlimefunItemStack} with specified id and {@link ItemStack}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public SlimefunItemStack getItem(String id, ItemStack itemStack, String... extraLore) {
        return getItemBy(itemsKey, id, itemStack, extraLore);
    }

    /**
     * Get the {@link RecipeType} with specified id and {@link Material}.
     *
     * @param id        The id of item.
     * @param material  The {@link Material} of item.
     * @param extraLore The extra lore to append to the end of current lore.
     * @return The {@link RecipeType} with specified id and {@link Material}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public RecipeType getRecipeType(String id, Material material, String... extraLore) {
        return new RecipeType(
            new NamespacedKey(getPlugin(), id),
            getItemBy(recipesKey, id, material, extraLore)
        );
    }

    /**
     * Get the {@link RecipeType} with specified id and head texture.
     *
     * @param id        The id of item.
     * @param texture   The texture of head.
     * @param extraLore The extra lore to append to the end of current lore.
     * @return The {@link RecipeType} with specified id and head texture.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public RecipeType getRecipeType(String id, String texture, String... extraLore) {
        return new RecipeType(
            new NamespacedKey(getPlugin(), id),
            getItemBy(recipesKey, id, texture, extraLore)
        );
    }

    /**
     * Get the {@link RecipeType} with specified id and {@link ItemStack}.
     *
     * @param id        The id of item.
     * @param itemStack The {@link ItemStack}.
     * @param extraLore The extra lore to append to the end of current lore.
     * @return The {@link RecipeType} with specified id and {@link ItemStack}.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public RecipeType getRecipeType(String id, ItemStack itemStack, String... extraLore) {
        return new RecipeType(
            new NamespacedKey(getPlugin(), id),
            getItemBy(recipesKey, id, itemStack, extraLore)
        );
    }
}
