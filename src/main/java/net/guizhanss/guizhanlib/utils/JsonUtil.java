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
     * @return 解析后的 {@link JsonObject}
     */
    public static JsonElement parse(String json) {
        return new JsonParser().parse(json);
    }
}
