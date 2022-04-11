package net.guizhanss.guizhanlib.java;

import lombok.experimental.UtilityClass;

/**
 * 整数({@link Integer})助手
 *
 * @author ybw0014
 */
@UtilityClass
public class IntegerHelper {
    /**
     * 将字符串{@link String}转为整型{@link Integer}，并限制在范围内
     *
     * @param sequence 字符串 {@link String}
     * @param min 最小值
     * @param max 最大值
     *
     * @throws NumberFormatException 给定的字符串不包含可转换的数字
     *
     * @return 范围内的整型
     */
    public static int parseInt(String sequence, int min, int max) {
        int n = Integer.parseInt(sequence);
        n = Math.max(min, n);
        n = Math.min(max, n);
        return n;
    }
}
