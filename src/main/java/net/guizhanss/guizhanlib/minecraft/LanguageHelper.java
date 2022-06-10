package net.guizhanss.guizhanlib.minecraft;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 从Minecraft的语言文件中获取内容
 *
 * @author ybw0014
 */
@UtilityClass
public final class LanguageHelper {

    private static final String filename = "/minecraft.zh_cn.json";
    private static final Map<String, String> lang;

    static {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            LanguageHelper.class.getResourceAsStream(filename), StandardCharsets.UTF_8
        ));
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        lang = gson.fromJson(reader, type);
    }

    /**
     * 获取语言文件中指定键名的内容
     *
     * @param key {@link String} 键名
     * @return 键名内容
     */
    @Nonnull
    public static String getLangOrKey(@Nonnull String key) {
        String lang = getLangOrNull(key);
        return lang != null ? lang : key;
    }

    /**
     * 获取语言文件中指定键名的内容
     *
     * @param key {@link String} 键名
     * @return 键名内容
     */
    @Nullable
    public static String getLangOrNull(@Nonnull String key) {
        Preconditions.checkNotNull(key, "键名不能为空");

        return lang.get(key);
    }
}
