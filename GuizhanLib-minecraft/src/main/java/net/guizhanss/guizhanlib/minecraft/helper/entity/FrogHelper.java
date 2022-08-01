package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.entity.Frog;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;

/**
 * 青蛙({@link Frog})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings({"unused", "ConstantConditions"})
public final class FrogHelper {
    /**
     * 获取青蛙肤色({@link Frog.Variant})的中文
     *
     * @param variant {@link Frog.Variant} 青蛙肤色
     *
     * @return 青蛙肤色的中文
     */
    @Nonnull
    public static String getVariant(@Nonnull Frog.Variant variant) {
        return Variant.fromVariant(variant).getChinese();
    }

    /**
     * 获取青蛙肤色({@link Frog.Variant})的中文
     *
     * @param variant {@link String} 青蛙肤色
     *
     * @return 青蛙肤色的中文
     */
    @Nonnull
    public static String getVariant(@Nonnull String variant) {
        Preconditions.checkArgument(variant != null, "青蛙肤色不能为空");

        try {
            Frog.Variant frogVariant = Frog.Variant.valueOf(variant);
            return Variant.fromVariant(frogVariant).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(variant);
        }
    }

    /**
     * 所有青蛙肤色
     */
    public enum Variant {
        /**
         * 橙色
         */
        TEMPERATE(Frog.Variant.TEMPERATE, "橙色"),
        /**
         * 绿色
         */
        COLD(Frog.Variant.COLD, "绿色"),
        /**
         * 白色
         */
        WARM(Frog.Variant.WARM, "白色");

        private static final Variant[] cachedValues = values();
        private static final Map<Frog.Variant, Variant> variantLookup = new EnumMap<>(Frog.Variant.class);

        static {
            for (Variant variant : cachedValues) {
                variantLookup.put(variant.getVariant(), variant);
            }
        }

        @Getter
        private final Frog.Variant variant;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Variant(Frog.Variant variant, String chinese) {
            this.variant = variant;
            this.chinese = chinese;
        }

        /**
         * 获取青蛙肤色
         *
         * @param frogVariant {@link Frog.Variant} 青蛙肤色
         *
         * @return 青蛙肤色
         */
        @Nonnull
        public static Variant fromVariant(@Nonnull Frog.Variant frogVariant) {
            Preconditions.checkArgument(frogVariant != null, "熊猫基因不能为空");

            return variantLookup.get(frogVariant);
        }

        @Nonnull
        @Override
        public String toString() {
            return this.getChinese();
        }
    }
}
