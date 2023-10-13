package net.guizhanss.guzihanlib.minecraft.utils;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import net.guizhanss.guizhanlib.minecraft.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class TestItemUtil {
    @Test
    @DisplayName("Test ItemUtil.appendLore(...)")
    void testAppendLore() {
        ItemStack itemStack = new CustomItemStack(
            Material.DIAMOND,
            "&aTest Item",
            "&cOriginal Lore"
        );

        ItemStack expected = new CustomItemStack(
            Material.DIAMOND,
            "&aTest Item",
            "&cOriginal Lore",
            "&aNew Lore"
        );

        ItemUtil.appendLore(itemStack, "&aNew Lore");

        Assertions.assertTrue(itemStack.hasItemMeta());

        ItemMeta meta = itemStack.getItemMeta();
        Assertions.assertTrue(meta.hasLore());

        List<String> lore = meta.getLore();

        Assertions.assertEquals(lore, expected.getItemMeta().getLore());
    }
}
