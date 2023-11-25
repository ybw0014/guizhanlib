package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.entity.Axolotl;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;

/**
 * 美西螈({@link Axolotl})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("ConstantConditions")
public final class AxolotlHelper {
    /**
     * 获取美西螈的变种({@link Axolotl.Variant})的中文
     *
     * @param variant
     *     {@link Axolotl.Variant} 美西螈的变种
     *
     * @return 美西螈的变种的中文
     */
    @Nonnull
    public static String getVariant(@Nonnull Axolotl.Variant variant) {
        return Variant.fromType(variant).getChinese();
    }

    /**
     * 获取美西螈的变种({@link Axolotl.Variant})的中文
     *
     * @param variant
     *     {@link String} 美西螈的变种
     *
     * @return 美西螈的变种的中文
     */
    @Nonnull
    public static String getVariant(@Nonnull String variant) {
        Preconditions.checkArgument(variant != null, "美西螈的变种不能为空");

        try {
            Axolotl.Variant axolotlVariant = Axolotl.Variant.valueOf(variant);
            return Variant.fromType(axolotlVariant).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(variant);
        }
    }

    /**
     * 所有美西螈的变种
     */
    public enum Variant {
        /**
         * 粉红色
         */
        LUCY(Axolotl.Variant.LUCY, "粉红色"),
        /**
         * 棕色
         */
        WILD(Axolotl.Variant.WILD, "棕色"),
        /**
         * 金色
         */
        GOLD(Axolotl.Variant.GOLD, "金色"),
        /**
         * 青色
         */
        CYAN(Axolotl.Variant.CYAN, "青色"),
        /**
         * 蓝色
         */
        BLUE(Axolotl.Variant.BLUE, "蓝色");

        private static final Variant[] cachedValues = values();
        private static final Map<Axolotl.Variant, Variant> variantLookup = new EnumMap<>(Axolotl.Variant.class);

        static {
            for (Variant variant : cachedValues) {
                variantLookup.put(variant.getAxolotlVariant(), variant);
            }
        }

        @Getter
        private final Axolotl.Variant axolotlVariant;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Variant(Axolotl.Variant variant, String chinese) {
            this.axolotlVariant = variant;
            this.chinese = chinese;
        }

        /**
         * 获取美西螈的变种
         *
         * @param axolotlVariant
         *     {@link Axolotl.Variant} 美西螈的变种
         *
         * @return 美西螈的变种
         */
        @Nonnull
        public static Variant fromType(@Nonnull Axolotl.Variant axolotlVariant) {
            Preconditions.checkArgument(axolotlVariant != null, "美西螈变种不能为空");

            return variantLookup.get(axolotlVariant);
        }

        @Nonnull
        @Override
        public String toString() {
            return this.getChinese();
        }
    }
}
