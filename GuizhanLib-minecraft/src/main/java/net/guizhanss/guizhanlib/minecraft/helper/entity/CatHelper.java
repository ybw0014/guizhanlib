package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.entity.Cat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;

/**
 * 猫({@link Cat})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("ConstantConditions")
public final class CatHelper {
    /**
     * 获取猫的类型({@link Cat.Type})的中文
     *
     * @param type
     *     {@link Cat.Type} 猫的类型
     *
     * @return 猫的类型的中文
     */
    @Nonnull
    public static String getType(@Nonnull Cat.Type type) {
        return Type.fromType(type).getChinese();
    }

    /**
     * 获取猫的类型({@link Cat.Type})的中文
     *
     * @param type
     *     {@link String} 猫的类型
     *
     * @return 猫的类型的中文
     */
    @Nonnull
    public static String getType(@Nonnull String type) {
        Preconditions.checkArgument(type != null, "猫类型不能为空");

        try {
            Cat.Type catType = Cat.Type.valueOf(type);
            return Type.fromType(catType).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(type);
        }
    }

    /**
     * 所有猫的类型
     */
    public enum Type {
        /**
         * 黑猫
         */
        ALL_BLACK(Cat.Type.ALL_BLACK, "黑猫"),
        /**
         * 西服猫
         */
        BLACK(Cat.Type.BLACK, "西服猫"),
        /**
         * 英国短毛猫
         */
        BRITISH_SHORTHAIR(Cat.Type.BRITISH_SHORTHAIR, "英国短毛猫"),
        /**
         * 花猫
         */
        CALICO(Cat.Type.CALICO, "花猫"),
        /**
         * Jellie
         */
        JELLIE(Cat.Type.JELLIE, "Jellie"),
        /**
         * 波斯猫
         */
        PERSIAN(Cat.Type.PERSIAN, "波斯猫"),
        /**
         * 布偶猫
         */
        RAGDOLL(Cat.Type.RAGDOLL, "布偶猫"),
        /**
         * 红虎斑猫
         */
        RED(Cat.Type.RED, "红虎斑猫"),
        /**
         * 暹罗猫
         */
        SIAMESE(Cat.Type.SIAMESE, "暹罗猫"),
        /**
         * 虎斑猫
         */
        TABBY(Cat.Type.TABBY, "虎斑猫"),
        /**
         * 白猫
         */
        WHITE(Cat.Type.WHITE, "白猫");

        private static final Type[] cachedValues = values();
        private static final Map<Cat.Type, Type> typeLookup = new EnumMap<>(Cat.Type.class);

        static {
            for (Type type : cachedValues) {
                typeLookup.put(type.getCatType(), type);
            }
        }

        @Getter
        private final Cat.Type catType;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Type(Cat.Type type, String chinese) {
            this.catType = type;
            this.chinese = chinese;
        }

        /**
         * 获取猫的类型
         *
         * @param catType
         *     {@link Cat.Type} 猫的类型
         *
         * @return 猫的类型
         */
        @Nonnull
        public static Type fromType(@Nonnull Cat.Type catType) {
            Preconditions.checkArgument(catType != null, "猫类型不能为空");

            return typeLookup.get(catType);
        }

        @Nonnull
        @Override
        public String toString() {
            return this.getChinese();
        }
    }
}
