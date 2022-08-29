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
     * @deprecated {@link #getVariant(Axolotl.Variant)}
     */
    @Deprecated
    @Nonnull
    public static String getType(@Nonnull Axolotl.Variant variant) {
        return getVariant(variant);
    }

    /**
     * @deprecated {@link #getVariant(String)}
     */
    @Deprecated
    @Nonnull
    public static String getType(@Nonnull String variant) {
        return getVariant(variant);
    }

    /**
     * 获取美西螈的变种({@link Axolotl.Variant})的中文
     *
     * @param variant {@link Axolotl.Variant} 美西螈的变种
     *
     * @return 美西螈的变种的中文
     */
    @Nonnull
    public static String getVariant(@Nonnull Axolotl.Variant variant) {
        return Type.fromType(variant).getChinese();
    }

    /**
     * 获取美西螈的变种({@link Axolotl.Variant})的中文
     *
     * @param variant {@link String} 美西螈的变种
     *
     * @return 美西螈的变种的中文
     */
    @Nonnull
    public static String getVariant(@Nonnull String variant) {
        Preconditions.checkArgument(variant != null, "美西螈的变种不能为空");

        try {
            Axolotl.Variant axolotlVariant = Axolotl.Variant.valueOf(variant);
            return Type.fromType(axolotlVariant).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(variant);
        }
    }

    /**
     * 所有美西螈的变种
     */
    public enum Type {
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

        private static final Type[] cachedValues = values();
        private static final Map<Axolotl.Variant, Type> variantLookup = new EnumMap<>(Axolotl.Variant.class);

        static {
            for (Type type : cachedValues) {
                variantLookup.put(type.getType(), type);
            }
        }

        @Getter
        private final Axolotl.Variant type;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Type(Axolotl.Variant type, String chinese) {
            this.type = type;
            this.chinese = chinese;
        }

        /**
         * 获取美西螈的变种
         *
         * @param axolotlVariant {@link Axolotl.Variant} 美西螈的变种
         *
         * @return 美西螈的变种
         */
        @Nonnull
        public static Type fromType(@Nonnull Axolotl.Variant axolotlVariant) {
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
