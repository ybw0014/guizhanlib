package net.guizhanss.minecraft.chineselib.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.WordUtils;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 字符串工具包
 * @author ybw0014
 */
@UtilityClass
public class StringUtil {
    /**
     * 将字符串转化为可读形式
     * @param str 要转化的字符
     * @return 转化后的字符串
     */
    public static @Nonnull String humanize(@Nonnull String str) {
        StringBuilder builder = new StringBuilder();
        str = str.toLowerCase(Locale.ROOT).replace(" ", "_");
        String[] segments = Pattern.compile("_").split(str);
        builder.append(WordUtils.capitalize(segments[0]));
        for (int i = 1; i < segments.length; i++) {
            builder.append(" ");
            builder.append(WordUtils.capitalize(segments[i]));
        }
        return builder.toString();
    }

    /**
     * 将字符串转化为大写+下划线形式
     * @param str 要转化的字符
     * @return 转化后的字符串
     */
    public static @Nonnull String dehumanize(@Nonnull String str) {
        return str.toUpperCase(Locale.ROOT)
            .replace(" ", "_")
            .replace("-", "_");
    }
}
