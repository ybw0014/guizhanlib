package net.guizhanss.guizhanlib.utils;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility methods about {@link String}.
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("ConstantConditions")
public final class StringUtil {
    private static final Pattern UNDERSCORE_PATTERN = Pattern.compile("_");
    private static final String NULL_STRING_MESSAGE = "The string cannot be null";

    /**
     * Make the string readable.
     *
     * @param str
     *     The original {@link String}.
     *
     * @return Readable {@link String}.
     */
    @Nonnull
    public static String humanize(@Nonnull String str) {
        Preconditions.checkArgument(str != null, NULL_STRING_MESSAGE);

        StringBuilder builder = new StringBuilder();
        str = str.toLowerCase(Locale.ROOT)
            .replace(" ", "_")
            .replace("-", "_");
        String[] segments = UNDERSCORE_PATTERN.split(str);
        builder.append(capitalize(segments[0]));
        for (int i = 1; i < segments.length; i++) {
            builder.append(" ");
            builder.append(capitalize(segments[i]));
        }
        return builder.toString();
    }

    /**
     * Make string to uppercase format and replace hyphen and spaces to underscore.
     *
     * @param str
     *     The original {@link String}.
     *
     * @return Readable string.
     */
    @Nonnull
    public static String dehumanize(@Nonnull String str) {
        Preconditions.checkArgument(str != null, NULL_STRING_MESSAGE);

        return str.toUpperCase(Locale.ROOT)
            .replace(" ", "_")
            .replace("-", "_");
    }

    /**
     * Capitalize each word in the {@link String}.
     * DOES NOT lower case other letters in a word.
     *
     * @param str
     *     The original {@link String}.
     *
     * @return Capitalized {@link String}.
     */
    @Nonnull
    public static String capitalize(@Nonnull String str) {
        Preconditions.checkArgument(str != null, NULL_STRING_MESSAGE);

        int strLen = str.length();
        StringBuilder builder = new StringBuilder(strLen);
        boolean capitalizeNext = true;

        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);
            if (Character.isWhitespace(ch)) {
                builder.append(ch);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                builder.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                builder.append(ch);
            }
        }

        return builder.toString();
    }

    /**
     * Check if given {@link String} is blank.
     *
     * @param str
     *     The {@link String} to be checked.
     *
     * @return If the {@link String} is blank.
     */
    public static boolean isBlank(@Nullable String str) {
        if (str == null) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
