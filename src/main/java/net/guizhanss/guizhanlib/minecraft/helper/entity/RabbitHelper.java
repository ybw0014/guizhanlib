package net.guizhanss.guizhanlib.minecraft.helper.entity;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.apache.commons.lang.Validate;
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
public class RabbitHelper {
    /**
     * 所有兔子的类型
     */
    public enum Type {
        BLACK(Rabbit.Type.BLACK, "Black", "黑色"),
        BLACK_AND_WHITE(Rabbit.Type.BLACK_AND_WHITE, "Black And White", "黑白相间"),
        BROWN(Rabbit.Type.BROWN, "Brown", "褐色"),
        GOLD(Rabbit.Type.GOLD, "Gold", "金色"),
        SALT_AND_PEPPER(Rabbit.Type.SALT_AND_PEPPER, "Salt And Pepper", "胡椒盐色"),
        THE_KILLER_BUNNY(Rabbit.Type.THE_KILLER_BUNNY, "The Killer Bunny", "杀手兔"),
        WHITE(Rabbit.Type.WHITE, "White", "白色");

        private final @Getter Rabbit.Type type;
        private final @Getter String english;
        private final @Getter String chinese;

        @ParametersAreNonnullByDefault
        Type(Rabbit.Type type, String english, String chinese) {
            this.type = type;
            this.english = english;
            this.chinese = chinese;
        }

        @Override
        public String toString() {
            return this.getChinese();
        }

        /**
         * 根据兔子的类型返回对应的枚举
         * @param rabbitType {@link Rabbit.Type} 兔子的类型
         * @return 对应的枚举
         */
        public static @Nonnull Type fromType(@Nonnull Rabbit.Type rabbitType) {
            Validate.notNull(rabbitType, "兔子的类型不能为空");

            for (Type type : Type.values()) {
                if (type.getType() == rabbitType) {
                    return type;
                }
            }
            throw new IllegalArgumentException("无效的兔子的类型");
        }

        /**
         * 根据英文返回对应的枚举
         * @param english {@link String} 提供的英文
         * @return 对应的枚举
         */
        public static @Nullable Type fromEnglish(@Nonnull String english) {
            Validate.notNull(english, "英文不能为空");

            String humanized = StringUtil.humanize(english);
            for (Type type : Type.values()) {
                if (type.getEnglish().equals(humanized)) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 获取兔子的类型({@link Rabbit.Type})的中文
     * @param type {@link Rabbit.Type} 兔子的类型
     * @return 兔子的类型的中文
     */
    public static @Nonnull String getType(@Nonnull Rabbit.Type type) {
        return Type.fromType(type).getChinese();
    }
}
