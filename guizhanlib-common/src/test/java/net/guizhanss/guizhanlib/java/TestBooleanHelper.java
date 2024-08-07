package net.guizhanss.guizhanlib.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestBooleanHelper {
    @Test
    void testYesOrNo() {
        assertEquals("是", BooleanHelper.yesOrNo(true));
        assertEquals("否", BooleanHelper.yesOrNo(false));
    }

    @Test
    void testEnabledOrDisabled() {
        assertEquals("已启用", BooleanHelper.enabledOrDisabled(true));
        assertEquals("已禁用", BooleanHelper.enabledOrDisabled(false));
    }
}
