package net.guizhanss.guizhanlib.slimefun.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBasicMetals {
    @Test
    void testFromEnglish() {
        assertEquals(BasicMetals.GOLD, BasicMetals.fromEnglish("Gold"));
        assertEquals(BasicMetals.IRON, BasicMetals.fromEnglish("IRON"));
        assertEquals(BasicMetals.TIN, BasicMetals.fromEnglish("tin"));
        assertEquals(BasicMetals.ZINC, BasicMetals.fromEnglish("ziNC"));
    }

    @Test
    void testFromChinese() {
        assertEquals(BasicMetals.GOLD, BasicMetals.fromChinese("金"));
        assertEquals(BasicMetals.IRON, BasicMetals.fromChinese("铁"));
        assertEquals(BasicMetals.TIN, BasicMetals.fromChinese("锡"));
        assertEquals(BasicMetals.ZINC, BasicMetals.fromChinese("锌"));
    }
}
