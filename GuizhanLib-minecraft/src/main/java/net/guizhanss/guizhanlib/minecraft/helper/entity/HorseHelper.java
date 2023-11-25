package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.entity.Horse;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;

/**
 * 马({@link Horse})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("ConstantConditions")
public final class HorseHelper {
    /**
     * 获取马的颜色({@link Horse.Color})的中文
     *
     * @param color
     *     {@link Horse.Color} 马的颜色
     *
     * @return 马的颜色的中文
     */
    @Nonnull
    public static String getColor(@Nonnull Horse.Color color) {
        return Color.fromColor(color).getChinese();
    }

    /**
     * 获取马的样式({@link Horse.Style})的中文
     *
     * @param style
     *     {@link Horse.Style} 马的样式
     *
     * @return 马的样式的中文
     */
    @Nonnull
    public static String getStyle(@Nonnull Horse.Style style) {
        return Style.fromStyle(style).getChinese();
    }

    /**
     * 获取马的颜色({@link Horse.Color})的中文
     *
     * @param color
     *     {@link String} 马的颜色
     *
     * @return 马的颜色的中文
     */
    @Nonnull
    public static String getColor(@Nonnull String color) {
        Preconditions.checkArgument(color != null, "马的颜色不能为空");

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
     * @param style
     *     {@link String} 马的样式
     *
     * @return 马的样式的中文
     */
    @Nonnull
    public static String getStyle(@Nonnull String style) {
        Preconditions.checkArgument(style != null, "马的样式不能为空");

        try {
            Horse.Style horseColor = Horse.Style.valueOf(style);
            return Style.fromStyle(horseColor).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(style);
        }
    }

    /**
     * 所有马的颜色
     */
    public enum Color {
        /**
         * 黑色
         */
        BLACK(Horse.Color.BLACK, "黑色"),
        /**
         * 褐色
         */
        BROWN(Horse.Color.BROWN, "褐色"),
        /**
         * 栗色
         */
        CHESTNUT(Horse.Color.CHESTNUT, "栗色"),
        /**
         * 奶油色
         */
        CREAMY(Horse.Color.CREAMY, "奶油色"),
        /**
         * 深褐色
         */
        DARK_BROWN(Horse.Color.DARK_BROWN, "深褐色"),
        /**
         * 灰色
         */
        GRAY(Horse.Color.GRAY, "灰色"),
        /**
         * 白色
         */
        WHITE(Horse.Color.WHITE, "白色");

        private static final Color[] cachedValues = values();
        private static final Map<Horse.Color, Color> colorLookup = new EnumMap<>(Horse.Color.class);

        static {
            for (Color color : cachedValues) {
                colorLookup.put(color.getHorseColor(), color);
            }
        }

        @Getter
        private final Horse.Color horseColor;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Color(Horse.Color color, String chinese) {
            this.horseColor = color;
            this.chinese = chinese;
        }

        /**
         * 获取马的颜色
         *
         * @param horseColor
         *     {@link Horse.Color} 马的颜色
         *
         * @return 马的颜色
         */
        @Nonnull
        public static Color fromColor(@Nonnull Horse.Color horseColor) {
            Preconditions.checkArgument(horseColor != null, "马的颜色不能为空");

            return colorLookup.get(horseColor);
        }

        @Nonnull
        @Override
        public String toString() {
            return this.getChinese();
        }
    }

    /**
     * 所有马的样式
     */
    public enum Style {
        /**
         * 黑色斑点
         */
        BLACK_DOTS(Horse.Style.BLACK_DOTS, "黑色斑点"),
        /**
         * 无
         */
        NONE(Horse.Style.NONE, "无"),
        /**
         * 白色
         */
        WHITE(Horse.Style.WHITE, "白色"),
        /**
         * 白色斑点
         */
        WHITE_DOTS(Horse.Style.WHITE_DOTS, "白色斑点"),
        /**
         * 白色条纹
         */
        WHITEFIELD(Horse.Style.WHITEFIELD, "白色条纹");

        private static final Style[] cachedValues = values();
        private static final Map<Horse.Style, Style> styleLookup = new EnumMap<>(Horse.Style.class);

        static {
            for (Style style : cachedValues) {
                styleLookup.put(style.getHorseStyle(), style);
            }
        }

        @Getter
        private final Horse.Style horseStyle;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Style(Horse.Style style, String chinese) {
            this.horseStyle = style;
            this.chinese = chinese;
        }

        /**
         * 获取马的样式
         *
         * @param horseStyle
         *     {@link Horse.Style} 马的样式
         *
         * @return 马的样式
         */
        @Nonnull
        public static Style fromStyle(@Nonnull Horse.Style horseStyle) {
            Preconditions.checkArgument(horseStyle != null, "马的样式不能为空");

            return styleLookup.get(horseStyle);
        }

        @Override
        public String toString() {
            return this.getChinese();
        }
    }
}
