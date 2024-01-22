package net.guizhanss.guizhanlib.java;

import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;

/**
 * 整数({@link Integer})助手
 *
 * @author ybw0014
 */
@UtilityClass
public final class IntegerHelper {
    /**
     * 将字符串{@link String}转为整型{@link Integer}，并限制在范围内
     * 如果给定字符串为空，则默认取最小值
     *
     * @param sequence 字符串 {@link String}
     * @param min      最小值
     * @param max      最大值
     * @return 范围内的整型
     */
    public static int parseInt(@Nullable String sequence, int min, int max) {
        int num;
        try {
            num = Integer.parseInt(sequence);
        } catch (NumberFormatException ex) {
            num = min;
        }
        num = Math.max(min, num);
        num = Math.min(max, num);
        return num;
    }
}
