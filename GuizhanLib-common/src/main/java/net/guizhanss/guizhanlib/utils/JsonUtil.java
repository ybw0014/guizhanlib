package net.guizhanss.guizhanlib.utils;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.util.regex.Pattern;

/**
 * Utility methods about json parser.
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("ConstantConditions")
public final class JsonUtil {
    private static final Pattern DOT_SPLIT = Pattern.compile("\\.");

    /**
     * Parse json {@link String}.
     *
     * @param json The string to be parsed.
     *
     * @return The parsed {@link JsonElement}.
     */
    @Nonnull
    public static JsonElement parse(@Nonnull String json) {
        Preconditions.checkArgument(json != null, "JSON string should not be null");
        return new JsonParser().parse(json);
    }

    /**
     * Parse json from {@link BufferedReader}.
     *
     * @param reader The {@link BufferedReader} to read json from.
     *
     * @return The parsed {@link JsonElement}.
     */
    @Nonnull
    public static JsonElement parse(@Nonnull BufferedReader reader) {
        Preconditions.checkArgument(reader != null, "reader should not be null");
        return new JsonParser().parse(reader);
    }

    /**
     * The value from given path of {@link JsonObject}.
     *
     * @param root Root {@link JsonElement}.
     * @param path The path of target value.
     *
     * @return The value got from path, {@code null} if not found.
     */
    @Nullable
    public static JsonElement getFromPath(@Nonnull JsonObject root, @Nonnull String path) {
        Preconditions.checkArgument(root != null, "root object should not be null");
        Preconditions.checkArgument(path != null, "path should not be null");

        String[] seg = DOT_SPLIT.split(path);
        for (String element : seg) {
            if (root != null) {
                JsonElement elem = root.get(element);
                if (elem == null) {
                    return null;
                }
                if (!elem.isJsonObject()) {
                    return elem;
                } else {
                    root = elem.getAsJsonObject();
                }
            } else {
                return null;
            }
        }
        return root;
    }
}
