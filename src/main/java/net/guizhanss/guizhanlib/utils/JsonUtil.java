package net.guizhanss.guizhanlib.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

/**
 * Json 解析工具包
 * @author ybw0014
 */
@UtilityClass
public class JsonUtil {
    /**
     * 解析JSON字符串
     * @param json 需要进行解析的字符串
     * @return 解析后的 {@link JsonElement}
     */
    public static JsonElement parse(String json) {
        return new JsonParser().parse(json);
    }

    /**
     * 从 {@link JsonObject} 中获取指定路径的值
     * @param root 根节点 {@link JsonElement}
     * @param path 路径
     * @return 指定路径的值
     */
    public static JsonElement getFromPath(JsonObject root, String path) {
        String[] seg = path.split("\\.");
        for (String element : seg) {
            if (root != null) {
                JsonElement elem = root.get(element);
                if (!elem.isJsonObject())
                    return elem;
                else
                    root = elem.getAsJsonObject();
            } else {
                return null;
            }
        }
        return root;
    }
}
