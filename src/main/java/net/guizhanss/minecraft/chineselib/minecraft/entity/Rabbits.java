package net.guizhanss.minecraft.chineselib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Minecraft - 兔子
 * @author ybw0014
 */
public class Rabbits {
    /**
     * 所有兔子的类型
     */
    public enum Type {
        BLACK("Black", "黑色"),
        BLACK_AND_WHITE("Black And White", "黑白相间"),
        BROWN("Brown", "褐色"),
        GOLD("Gold", "金色"),
        SALT_AND_PEPPER("Salt And Pepper", "胡椒盐色"),
        THE_KILLER_BUNNY("The Killer Bunny", "杀手兔"),
        WHITE("White", "白色");

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
