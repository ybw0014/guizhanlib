package net.guizhanss.guizhanlib.slimefun.items.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBasicMetal {
    @Test
    void testFromEnglish() {
        assertEquals(BasicMetal.GOLD, BasicMetal.fromEnglish("Gold"));
        assertEquals(BasicMetal.IRON, BasicMetal.fromEnglish("IRON"));
        assertEquals(BasicMetal.TIN, BasicMetal.fromEnglish("tin"));
        assertEquals(BasicMetal.ZINC, BasicMetal.fromEnglish("ziNC"));
    }

    @Test
    void testFromChinese() {
        assertEquals(BasicMetal.GOLD, BasicMetal.fromChinese("金"));
        assertEquals(BasicMetal.IRON, BasicMetal.fromChinese("铁"));
        assertEquals(BasicMetal.TIN, BasicMetal.fromChinese("锡"));
        assertEquals(BasicMetal.ZINC, BasicMetal.fromChinese("锌"));
    }
}
