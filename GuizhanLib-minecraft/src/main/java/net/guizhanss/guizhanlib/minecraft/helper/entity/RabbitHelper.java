package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.entity.Rabbit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 兔子({@link Rabbit})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("unused")
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
        BLACK(Rabbit.Type.BLACK, "Black", "黑色"),
        /**
         * 黑白相间
         */
        BLACK_AND_WHITE(Rabbit.Type.BLACK_AND_WHITE, "Black And White", "黑白相间"),
        /**
         * 褐色
         */
        BROWN(Rabbit.Type.BROWN, "Brown", "褐色"),
        /**
         * 金色
         */
        GOLD(Rabbit.Type.GOLD, "Gold", "金色"),
        /**
         * 胡椒盐色
         */
        SALT_AND_PEPPER(Rabbit.Type.SALT_AND_PEPPER, "Salt And Pepper", "胡椒盐色"),
        /**
         * 杀手兔
         */
        THE_KILLER_BUNNY(Rabbit.Type.THE_KILLER_BUNNY, "The Killer Bunny", "杀手兔"),
        /**
         * 白色
         */
        WHITE(Rabbit.Type.WHITE, "White", "白色");

        @Getter
        private final Rabbit.Type type;
        @Getter
        private final String english;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Type(Rabbit.Type type, String english, String chinese) {
            this.type = type;
            this.english = english;
            this.chinese = chinese;
        }

        /**
         * 根据兔子的类型返回对应的枚举
         *
         * @param rabbitType {@link Rabbit.Type} 兔子的类型
         *
         * @return 对应的枚举
         */
        @Nonnull
        public static Type fromType(@Nonnull Rabbit.Type rabbitType) {
            Preconditions.checkArgument(rabbitType != null, "兔子的类型不能为空");

            for (Type type : Type.values()) {
                if (type.getType() == rabbitType) {
                    return type;
                }
            }
            throw new IllegalArgumentException("无效的兔子的类型");
        }

        /**
         * 根据英文返回对应的枚举
         *
         * @param english {@link String} 提供的英文
         *
         * @return 对应的枚举
         */
        @Nullable
        public static Type fromEnglish(@Nonnull String english) {
            Preconditions.checkArgument(english != null, "英文不能为空");

            String humanized = StringUtil.humanize(english);
            for (Type type : Type.values()) {
                if (type.getEnglish().equals(humanized)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return this.getChinese();
        }
    }
}
