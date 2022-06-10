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
 * Json 解析工具包
 *
 * @author ybw0014
 */
@UtilityClass
public final class JsonUtil {
    /**
     * 解析JSON字符串
     *
     * @param json 需要进行解析的字符串
     * @return 解析后的 {@link JsonElement}
     */
    @Nonnull
    public static JsonElement parse(@Nonnull String json) {
        Preconditions.checkNotNull(json, "JSON string should not be null");
        return new JsonParser().parse(json);
    }

    /**
     * 解析JSON字符串 (来自 {@link BufferedReader})
     *
     * @param reader 需要进行解析的字符串
     * @return 解析后的 {@link JsonElement}
     */
    @Nonnull
    public static JsonElement parse(@Nonnull BufferedReader reader) {
        Preconditions.checkNotNull(reader, "reader should not be null");
        return new JsonParser().parse(reader);
    }

    /**
     * 从 {@link JsonObject} 中获取指定路径的值
     *
     * @param root 根节点 {@link JsonElement}
     * @param path 路径
     * @return 指定路径的值
     */
    @Nullable
    public static JsonElement getFromPath(@Nonnull JsonObject root, @Nonnull String path) {
        Preconditions.checkNotNull(root, "root object should not be null");
        Preconditions.checkNotNull(path, "path should not be null");

        String[] seg = Pattern.compile("\\.").split(path);
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
