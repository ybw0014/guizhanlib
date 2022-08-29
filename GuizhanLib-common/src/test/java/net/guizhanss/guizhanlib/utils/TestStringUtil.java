package net.guizhanss.guizhanlib.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestStringUtil {
    @Test
    void testHumanize() {
        String str1 = "STRinG_FirSt";
        String expected1 = "String First";
        assertEquals(expected1, StringUtil.humanize(str1));

        String str2 = "Do IT again";
        String expected2 = "Do It Again";
        assertEquals(expected2, StringUtil.humanize(str2));

        String str3 = "the_killer_bunny";
        String expected3 = "The Killer Bunny";
        assertEquals(expected3, StringUtil.humanize(str3));
    }

    @Test
    void testDehumanize() {
        String str1 = "Magma Cube";
        String expected1 = "MAGMA_CUBE";
        assertEquals(expected1, StringUtil.dehumanize(str1));

        String str2 = "a-useLESS_sTring";
        String expected2 = "A_USELESS_STRING";
        assertEquals(expected2, StringUtil.dehumanize(str2));

        String str3 = "the_killer_bunny";
        String expected3 = "THE_KILLER_BUNNY";
        assertEquals(expected3, StringUtil.dehumanize(str3));
    }

    @Test
    void testCapitalize() {
        String str1 = "magma_Cube";
        String expected1 = "Magma_Cube";
        assertEquals(expected1, StringUtil.capitalize(str1));
    }

    @Test
    void testIsBlank() {
        String str1 = null;
        assertTrue(StringUtil.isBlank(str1));

        String str2 = "";
        assertTrue(StringUtil.isBlank(str2));

        String str3 = "notempty";
        assertFalse(StringUtil.isBlank(str3));
    }
}
