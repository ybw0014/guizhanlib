package net.guizhanss.minecraft.chineselib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Minecraft - 马
 * @author ybw0014
 */
public class Horses {
    /**
     * 所有马的颜色
     */
    public enum Color {
        BLACK("Black", "黑色"),
        BROWN("Brown", "褐色"),
        CHESTNUT("Chestnut", "栗色"),
        CREAMY("Creamy", "奶油色"),
        DARK_BROWN("Dark Brown", "深褐色"),
        GRAY("Gray", "灰色"),
        WHITE("White", "白色");

        private @Getter String english;
        private @Getter String chinese;

        Color(@Nonnull String english, @Nonnull String chinese) {
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
        public static Color fromEnglish(String english) {
            String humanized = StringUtil.humanize(english);
            for (Color color : Color.values()) {
                if (color.getEnglish().equals(humanized)) {
                    return color;
                }
            }
            return null;
        }
    }

    /**
     * 所有马的样式
     */
    public enum Style {
        BLACK_DOTS("Black Dots", "黑色斑点"),
        NONE("None", "无"),
        WHITE("White", "白色"),
        WHITE_DOTS("White Dots", "白色斑点"),
        WHITEFIELD("Whitefield", "白色条纹");

        private @Getter String english;
        private @Getter String chinese;

        Style(@Nonnull String english, @Nonnull String chinese) {
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
        public static Style fromEnglish(String english) {
            String humanized = StringUtil.humanize(english);
            for (Style style : Style.values()) {
                if (style.getEnglish().equals(humanized)) {
                    return style;
                }
            }
            return null;
        }
    }
}
