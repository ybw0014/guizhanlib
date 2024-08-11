package net.guizhanss.guizhanlib.slimefun.machines;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * A {@link MenuBlock} is a {@link SlimefunItem} with {@link MenuBlockPreset}.
 * <p>
 * Modified from InfinityLib
 *
 * @author Mooy1
 * @author ybw0014
 */
@ParametersAreNonnullByDefault
public abstract class MenuBlock extends SlimefunItem {

    /**
     * Constructor of {@link MenuBlock}.
     * Add events on break and place
     *
     * @param itemGroup  the {@link ItemGroup} of this {@link MenuBlock}
     * @param item       the {@link SlimefunItemStack} of this {@link MenuBlock}
     * @param recipeType the {@link RecipeType} of this {@link MenuBlock}
     * @param recipe     the recipe of this {@link MenuBlock}
     */
    @ParametersAreNonnullByDefault
    protected MenuBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(
            new BlockBreakHandler(false, false) {
                @Override
                @ParametersAreNonnullByDefault
                public void onPlayerBreak(BlockBreakEvent e, ItemStack itemStack, List<ItemStack> list) {
                    BlockMenu menu = BlockStorage.getInventory(e.getBlock());
                    if (menu != null) {
                        onBreak(e, menu);
                    }
                }
            },
            new BlockPlaceHandler(false) {
                @Override
                @ParametersAreNonnullByDefault
                public void onPlayerPlace(BlockPlaceEvent e) {
                    onPlace(e, e.getBlockPlaced());
                }
            }
        );
    }

    /**
     * Be sure to call {@code super.postRegister()} if you override this method.
     */
    @Override
    public void postRegister() {
        new MenuBlockPreset(this);
    }

    /**
     * Set up the {@link BlockMenuPreset} of this block.
     *
     * @param preset The {@link BlockMenuPreset} of this block.
     */
    @ParametersAreNonnullByDefault
    protected abstract void setup(BlockMenuPreset preset);

    /**
     * Returns the slots for different {@link ItemTransportFlow}. For insert, return input slots. For withdraw, return output slots.
     *
     * @param menu The {@link DirtyChestMenu} of this block.
     * @param flow The {@link ItemTransportFlow}.
     * @param item The {@link ItemStack} to transport.
     * @return The slots for different {@link ItemTransportFlow}.
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    protected final int[] getTransportSlots(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        return switch (flow) {
            case INSERT -> getInputSlots(menu, item);
            case WITHDRAW -> getOutputSlots();
        };
    }

    /**
     * The input slots for the given {@link ItemStack}. By default, it returns all the input slots.
     *
     * @param menu The {@link DirtyChestMenu} of this block.
     * @param item The {@link ItemStack} to transport.
     * @return The input slots for the given {@link ItemStack}.
     */
    @ParametersAreNonnullByDefault
    protected int[] getInputSlots(DirtyChestMenu menu, ItemStack item) {
        return getInputSlots();
    }

    /**
     * All the input slots.
     *
     * @return All the input slots.
     */
    protected abstract int[] getInputSlots();

    /**
     * All the output slots.
     *
     * @return All the output slots.
     */
    protected abstract int[] getOutputSlots();

    /**
     * This method is called when a new instance of this block is created. (usually after placed, or after server start)
     *
     * @param menu The {@link BlockMenu} of this block.
     * @param b    The {@link Block}.
     */
    @ParametersAreNonnullByDefault
    protected void onNewInstance(BlockMenu menu, Block b) {
    }

    /**
     * The default handler when block breaks. Drop items in input and output slots.
     *
     * @param e    The {@link BlockBreakEvent}.
     * @param menu The {@link BlockMenu} of this block.
     */
    @ParametersAreNonnullByDefault
    protected void onBreak(BlockBreakEvent e, BlockMenu menu) {
        Location l = menu.getLocation();
        menu.dropItems(l, getInputSlots());
        menu.dropItems(l, getOutputSlots());
    }

    /**
     * The default handler when block places.
     *
     * @param e The {@link BlockPlaceEvent}.
     * @param b The {@link Block} placed.
     */
    @ParametersAreNonnullByDefault
    protected void onPlace(BlockPlaceEvent e, Block b) {
    }
}
