package net.guizhanss.minecraft.chineselib.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStringUtil {
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
}
