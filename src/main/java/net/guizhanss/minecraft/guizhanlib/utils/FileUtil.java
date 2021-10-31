package net.guizhanss.minecraft.guizhanlib.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 文件工具包
 * @author ybw0014
 */
@UtilityClass
public class FileUtil {
    /**
     * 从文件中读取json
     * @param filename {@link BufferedReader} 内容
     * @return json map
     */
    public static @Nonnull Map<String, String> readJsonFromFile(@Nonnull String filename){
        Validate.notNull(filename, "文件名不能为空");
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            FileUtil.class.getResourceAsStream(filename), StandardCharsets.UTF_8
        ));
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(reader, type);
    }
}
