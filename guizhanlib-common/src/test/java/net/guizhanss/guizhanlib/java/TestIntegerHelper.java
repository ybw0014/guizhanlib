package net.guizhanss.guizhanlib.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestIntegerHelper {
    @Test
    void testParseInt() {
        // normal
        assertEquals(514, IntegerHelper.parseInt("114514", 114, 514));
        assertEquals(1919, IntegerHelper.parseInt("1919", 810, 114514));

        // invalid number string
        assertEquals(114, IntegerHelper.parseInt("", 114, 514));
        assertEquals(114, IntegerHelper.parseInt("asdad", 114, 514));

        // null string
        assertEquals(114, IntegerHelper.parseInt(null, 114, 514));
    }
}
