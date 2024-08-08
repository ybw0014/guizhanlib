package net.guizhanss.guizhanlib.common.utils;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
     * The value from given path of {@link JsonObject}.
     *
     * @param root Root {@link JsonElement}.
     * @param path The path of target value.
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
