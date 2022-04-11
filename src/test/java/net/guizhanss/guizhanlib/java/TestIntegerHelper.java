package net.guizhanss.guizhanlib.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestIntegerHelper {
    @Test
    void testParseInt() {
        assertEquals(514, IntegerHelper.parseInt("114514", 114, 514));
        assertEquals(1919, IntegerHelper.parseInt("1919", 810, 114514));
    }
}
