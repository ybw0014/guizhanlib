package net.guizhanss.minecraft.guizhanlib.minecraft.helper.entity;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.minecraft.guizhanlib.utils.StringUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Fox;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 狐狸({@link Fox})
 *
 * @author ybw0014
 */
@UtilityClass
public class FoxHelper {
    /**
     * 所有狐狸的类型
     */
    public enum Type {
        RED(Fox.Type.RED, "Red", "红色"),
        SNOW(Fox.Type.SNOW, "Snow", "白色");

        private final @Getter Fox.Type type;
        private final @Getter String english;
        private final @Getter String chinese;

        @ParametersAreNonnullByDefault
        Type(Fox.Type type, String english, String chinese) {
            this.type = type;
            this.english = english;
            this.chinese = chinese;
        }

        @Override
        public String toString() {
            return this.getChinese();
        }

        /**
         * 根据狐狸的类型返回对应的枚举
         * @param foxType {@link Fox.Type} 狐狸的类型
         * @return 对应的枚举
         */
        public static @Nonnull Type fromType(@Nonnull Fox.Type foxType) {
            Validate.notNull(foxType, "狐狸类型不能为空");

            for (Type type : Type.values()) {
                if (type.getType() == foxType) {
                    return type;
                }
            }
            throw new IllegalArgumentException("无效的狐狸类型");
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
     * 获取狐狸的类型({@link Fox.Type})的中文
     * @param type {@link Fox.Type} 狐狸的类型
     * @return 狐狸的类型的中文
     */
    public static @Nonnull String getType(@Nonnull Fox.Type type) {
        return Type.fromType(type).getChinese();
    }
}
