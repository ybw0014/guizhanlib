package net.guizhanss.guzihanlib.minecraft.utils;

import be.seeseemelk.mockbukkit.MockBukkit;
import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;
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

    @BeforeAll
    public static void setUp() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test ItemUtil.appendLore(...)")
    void testAppendLore() {
        final ItemStack item = new ItemStack(Material.DIAMOND);
        final ItemMeta meta = item.getItemMeta();
        meta.setLore(ChatUtil.color(List.of("&aTest Item", "&cOriginal Lore")));
        item.setItemMeta(meta);

        final ItemStack expected = new ItemStack(Material.DIAMOND);
        final ItemMeta expectedMeta = expected.getItemMeta();
        expectedMeta.setLore(ChatUtil.color(List.of("&aTest Item", "&cOriginal Lore", "&aNew Lore")));
        expected.setItemMeta(expectedMeta);

        ItemUtil.appendLore(item, "&aNew Lore");

        Assertions.assertTrue(item.hasItemMeta());

        ItemMeta itemMeta = item.getItemMeta();
        Assertions.assertTrue(itemMeta.hasLore());

        List<String> lore = itemMeta.getLore();

        Assertions.assertEquals(lore, expected.getItemMeta().getLore());
    }
}
