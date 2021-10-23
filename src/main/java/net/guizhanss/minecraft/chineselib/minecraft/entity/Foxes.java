package net.guizhanss.minecraft.chineselib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Minecraft - 狐狸
 * @author ybw0014
 */
public class Foxes {
    /**
     * 所有狐狸的类型
     */
    public enum Type {
        RED("Red", "红色"),
        SNOW("Snow", "白色");

        private @Getter String english;
        private @Getter String chinese;

        Type(@Nonnull String english, @Nonnull String chinese) {
            this.english = english;
            this.chinese = chinese;
        }

        @Override
        public String toString() {
            return this.chinese;
        }

        /**
         * 根据英文返回对应的枚举类型
         * @param english 提供的英文类型
         * @return 对应的枚举类型
         */
        @Nullable
        public static Type fromEnglish(String english) {
            String humanized = StringUtil.humanize(english);
            for (Type type : Type.values()) {
                if (type.getEnglish().equals(humanized)) {
                    return type;
                }
            }
            return null;
        }
    }
}
