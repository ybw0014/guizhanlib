package net.guizhanss.guzihanlib.minecraft.utils;

import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestChatUtil {
    @Test
    void testColorString() {
        String original1 = "&c114514";
        String expected1 = "§c114514";
        assertEquals(expected1, ChatUtil.color(original1));
    }

    @Test
    void testColorStringList() {
        List<String> original1 = Arrays.asList("&c114514", "&a1919810");
        List<String> expected1 = Arrays.asList("§c114514", "§a1919810");
        assertEquals(expected1, ChatUtil.color(original1));
    }
}
