package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.entity.Horse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 马({@link Horse})
 *
 * @author ybw0014
 */
@UtilityClass
public final class HorseHelper {
    /**
     * 所有马的颜色
     */
    public enum Color {
        /**
         * 黑色
         */
        BLACK(Horse.Color.BLACK, "Black", "黑色"),
        /**
         * 褐色
         */
        BROWN(Horse.Color.BROWN, "Brown", "褐色"),
        /**
         * 栗色
         */
        CHESTNUT(Horse.Color.CHESTNUT, "Chestnut", "栗色"),
        /**
         * 奶油色
         */
        CREAMY(Horse.Color.CREAMY, "Creamy", "奶油色"),
        /**
         * 深褐色
         */
        DARK_BROWN(Horse.Color.DARK_BROWN, "Dark Brown", "深褐色"),
        /**
         * 灰色
         */
        GRAY(Horse.Color.GRAY, "Gray", "灰色"),
        /**
         * 白色
         */
        WHITE(Horse.Color.WHITE, "White", "白色");

        private final @Getter
        Horse.Color color;
        private final @Getter
        String english;
        private final @Getter
        String chinese;

        @ParametersAreNonnullByDefault
        Color(Horse.Color color, String english, String chinese) {
            this.color = color;
            this.english = english;
            this.chinese = chinese;
        }

        @Nonnull
        @Override
        public String toString() {
            return this.getChinese();
        }

        /**
         * 根据马的颜色返回对应的枚举
         *
         * @param horseColor {@link Horse.Color} 马的颜色
         * @return 对应的枚举
         */
        @Nonnull
        public static Color fromColor(@Nonnull Horse.Color horseColor) {
            Preconditions.checkNotNull(horseColor, "马的颜色不能为空");

            for (Color color : Color.values()) {
                if (color.getColor() == horseColor) {
                    return color;
                }
            }
            throw new IllegalArgumentException("无效的马的颜色");
        }

        /**
         * 根据英文返回对应的枚举
         *
         * @param english {@link String} 提供的英文
         * @return 对应的枚举
         */
        @Nullable
        public static Color fromEnglish(@Nonnull String english) {
            Preconditions.checkNotNull(english, "英文不能为空");

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
        BLACK_DOTS(Horse.Style.BLACK_DOTS, "Black Dots", "黑色斑点"),
        NONE(Horse.Style.NONE, "None", "无"),
        WHITE(Horse.Style.WHITE, "White", "白色"),
        WHITE_DOTS(Horse.Style.WHITE_DOTS, "White Dots", "白色斑点"),
        WHITEFIELD(Horse.Style.WHITEFIELD, "Whitefield", "白色条纹");

        private final @Getter
        Horse.Style style;
        private final @Getter
        String english;
        private final @Getter
        String chinese;

        Style(Horse.Style style, String english, String chinese) {
            this.style = style;
            this.english = english;
            this.chinese = chinese;
        }

        @Override
        public String toString() {
            return this.getChinese();
        }

        /**
         * 根据马的样式返回对应的枚举
         *
         * @param horseStyle {@link Horse.Style} 马的样式
         * @return 对应的枚举
         */
        @Nonnull
        public static Style fromStyle(@Nonnull Horse.Style horseStyle) {
            Preconditions.checkNotNull(horseStyle, "马的样式不能为空");

            for (Style style : Style.values()) {
                if (style.getStyle() == horseStyle) {
                    return style;
                }
            }
            throw new IllegalArgumentException("无效的马的样式");
        }

        /**
         * 根据英文返回对应的枚举
         *
         * @param english {@link String} 提供的英文
         * @return 对应的枚举
         */
        @Nullable
        public static Style fromEnglish(@Nonnull String english) {
            Preconditions.checkNotNull(english, "英文不能为空");

            String humanized = StringUtil.humanize(english);
            for (Style style : Style.values()) {
                if (style.getEnglish().equals(humanized)) {
                    return style;
                }
            }
            return null;
        }
    }

    /**
     * 获取马的颜色({@link Horse.Color})的中文
     *
     * @param color {@link Horse.Color} 马的颜色
     * @return 马的颜色的中文
     */
    @Nonnull
    public static String getColor(@Nonnull Horse.Color color) {
        return Color.fromColor(color).getChinese();
    }

    /**
     * 获取马的样式({@link Horse.Style})的中文
     *
     * @param style {@link Horse.Style} 马的样式
     * @return 马的样式的中文
     */
    @Nonnull
    public static String getStyle(@Nonnull Horse.Style style) {
        return Style.fromStyle(style).getChinese();
    }

    /**
     * 获取马的颜色({@link Horse.Color})的中文
     *
     * @param color {@link String} 马的颜色
     * @return 马的颜色的中文
     */
    @Nonnull
    public static String getColor(@Nonnull String color) {
        Preconditions.checkNotNull(color, "马的颜色不能为空");

        try {
            Horse.Color horseColor = Horse.Color.valueOf(color);
            return Color.fromColor(horseColor).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(color);
        }
    }

    /**
     * 获取马的样式({@link Horse.Style})的中文
     *
     * @param style {@link String} 马的样式
     * @return 马的样式的中文
     */
    @Nonnull
    public static String getStyle(@Nonnull String style) {
        Preconditions.checkNotNull(style, "马的样式不能为空");

        try {
            Horse.Style horseColor = Horse.Style.valueOf(style);
            return Style.fromStyle(horseColor).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(style);
        }
    }
}
