package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Rabbit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;

/**
 * 兔子({@link Rabbit})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings({"unused", "ConstantConditions"})
public final class RabbitHelper {
    /**
     * 获取兔子的类型({@link Rabbit.Type})的中文
     *
     * @param type {@link Rabbit.Type} 兔子的类型
     *
     * @return 兔子的类型的中文
     */
    @Nonnull
    public static String getType(@Nonnull Rabbit.Type type) {
        return Type.fromType(type).getChinese();
    }

    /**
     * 获取兔子的类型({@link Rabbit.Type})的中文
     *
     * @param type {@link String} 兔子的类型
     *
     * @return 兔子的类型的中文
     */
    @Nonnull
    public static String getType(@Nonnull String type) {
        Preconditions.checkArgument(type != null, "兔子的类型不能为空");

        try {
            Rabbit.Type rabbitType = Rabbit.Type.valueOf(type);
            return Type.fromType(rabbitType).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(type);
        }
    }

    /**
     * 所有兔子的类型
     */
    public enum Type {
        /**
         * 黑色
         */
        BLACK(Rabbit.Type.BLACK, "黑色"),
        /**
         * 黑白相间
         */
        BLACK_AND_WHITE(Rabbit.Type.BLACK_AND_WHITE, "黑白相间"),
        /**
         * 褐色
         */
        BROWN(Rabbit.Type.BROWN, "褐色"),
        /**
         * 金色
         */
        GOLD(Rabbit.Type.GOLD, "金色"),
        /**
         * 胡椒盐色
         */
        SALT_AND_PEPPER(Rabbit.Type.SALT_AND_PEPPER, "胡椒盐色"),
        /**
         * 杀手兔
         */
        THE_KILLER_BUNNY(Rabbit.Type.THE_KILLER_BUNNY, "杀手兔"),
        /**
         * 白色
         */
        WHITE(Rabbit.Type.WHITE, "白色");

        private static final Type[] cachedValues = values();
        private static final Map<Rabbit.Type, Type> typeLookup = new EnumMap<>(Rabbit.Type.class);

        static {
            for (Type type : cachedValues) {
                typeLookup.put(type.getType(), type);
            }
        }

        @Getter
        private final Rabbit.Type type;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Type(Rabbit.Type type, String chinese) {
            this.type = type;
            this.chinese = chinese;
        }

        /**
         * 获取兔子的类型
         *
         * @param rabbitType {@link Rabbit.Type} 兔子的类型
         *
         * @return 兔子的类型
         */
        @Nonnull
        public static Type fromType(@Nonnull Rabbit.Type rabbitType) {
            Preconditions.checkArgument(rabbitType != null, "兔子的类型不能为空");

            return typeLookup.get(rabbitType);
        }

        @Nonnull
        @Override
        public String toString() {
            return this.getChinese();
        }
    }
}
