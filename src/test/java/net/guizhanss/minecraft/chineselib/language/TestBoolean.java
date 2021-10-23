package net.guizhanss.minecraft.chineselib.language;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBoolean {
    @Test
    void testYesOrNo() {
        assertEquals("是", Boolean.yesOrNo(true));
        assertEquals("否", Boolean.yesOrNo(false));
    }

    @Test
    void testEnabledOrDisabled() {
        assertEquals("已启用", Boolean.enabledOrDisabled(true));
        assertEquals("已禁用", Boolean.enabledOrDisabled(false));
    }
}
