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
                public void onPlayerBreak(BlockBreakEvent e, ItemStack itemStack, List<ItemStack> list) {
                    BlockMenu menu = BlockStorage.getInventory(e.getBlock());
                    if (menu != null) {
                        onBreak(e, menu);
                    }
                }
            },
            new BlockPlaceHandler(false) {
                @Override
                public void onPlayerPlace(BlockPlaceEvent e) {
                    onPlace(e, e.getBlockPlaced());
                }
            }
        );
    }

    @Override
    public final void postRegister() {
        new MenuBlockPreset(this);
    }

    @ParametersAreNonnullByDefault
    protected abstract void setup(BlockMenuPreset preset);

    @ParametersAreNonnullByDefault
    @Nonnull
    protected final int[] getTransportSlots(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        return switch (flow) {
            case INSERT -> getInputSlots(menu, item);
            case WITHDRAW -> getOutputSlots();
        };
    }

    @ParametersAreNonnullByDefault
    protected int[] getInputSlots(DirtyChestMenu menu, ItemStack item) {
        return getInputSlots();
    }

    protected abstract int[] getInputSlots();

    protected abstract int[] getOutputSlots();

    @ParametersAreNonnullByDefault
    protected void onNewInstance(BlockMenu menu, Block b) {

    }

    @ParametersAreNonnullByDefault
    protected void onBreak(BlockBreakEvent e, BlockMenu menu) {
        Location l = menu.getLocation();
        menu.dropItems(l, getInputSlots());
        menu.dropItems(l, getOutputSlots());
    }

    @ParametersAreNonnullByDefault
    protected void onPlace(BlockPlaceEvent e, Block b) {

    }
}
