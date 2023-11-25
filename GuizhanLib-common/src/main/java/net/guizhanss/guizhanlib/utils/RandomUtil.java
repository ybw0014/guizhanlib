package net.guizhanss.guizhanlib.utils;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility methods about random numbers.
 *
 * @author ybw0014
 */
@UtilityClass
public final class RandomUtil {
    private static final double DOUBLE_PRECISION = 1_000_000D;

    /**
     * Get a random {@link Integer} between min (inclusive) and max (inclusive).
     *
     * @param min
     *     The minimum range.
     * @param max
     *     The maximum range.
     *
     * @return The random {@link Integer}
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max + 1) + min;
    }

    /**
     * Get a random {@link Double} between min (inclusive) and max (inclusive).
     *
     * @param min
     *     The minimum range.
     * @param max
     *     The maximum range.
     *
     * @return The random {@link Double}
     */
    public static double randomDouble(double min, double max) {
        double number = ThreadLocalRandom.current().nextInt((int) ((max - min) * DOUBLE_PRECISION + 1)) + min * DOUBLE_PRECISION;
        return number / DOUBLE_PRECISION;
    }

    /**
     * Test chance of chance / bound.
     *
     * @param chance
     *     Numerator (must be greater than 0)
     * @param bound
     *     Denominator (must be greater than 0)
     *
     * @return If the roll succeed.
     */
    public static boolean testChance(int chance, int bound) {
        Preconditions.checkArgument(chance > 0, "Chance must be greater than 0");
        Preconditions.checkArgument(bound > 0, "Bound must be greater than 0");
        return randomInt(1, bound) <= chance;
    }
}
