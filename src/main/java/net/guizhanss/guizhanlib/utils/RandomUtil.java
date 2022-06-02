package net.guizhanss.guizhanlib.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数相关工具包
 *
 * @author ybw0014
 */
@UtilityClass
public class RandomUtil {
    /**
     * 获得 min(包括) 至 max(包括) 的随机整数
     *
     * @param min 最小范围
     * @param max 最大范围
     *
     * @return 随机数
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max + 1) + min;
    }

    /**
     * 获得 min(包括) 至 max(包括) 的随机小数
     *
     * @param min 最小范围
     * @param max 最大范围
     *
     * @return 随机数
     */
    public static double randomDouble(double min, double max) {
        double precision = 1_000_000D;
        double number = ThreadLocalRandom.current().nextInt((int) ((max - min) * precision + 1)) + min * precision;
        return number / precision;
    }

    /**
     * 以 chance / bound 的概率进行尝试
     *
     * @param chance 分子
     * @param bound 分母
     *
     * @return 是否成功
     */
    public static boolean testChance(int chance, int bound) {
        return randomInt(1, chance) <= chance;
    }
}
