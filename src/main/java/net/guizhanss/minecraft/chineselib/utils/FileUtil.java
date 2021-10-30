package net.guizhanss.minecraft.chineselib.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;

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
     * 获取文件内容
     * @param filename 文件名
     * @return {@link BufferedReader}
     */
    public static @Nonnull BufferedReader readFile(@Nonnull String filename) {
        Validate.notNull(filename, "文件名不能为空");
        return new BufferedReader(new InputStreamReader(
            FileUtil.class.getResourceAsStream(filename), StandardCharsets.UTF_8
        ));
    }

    /**
     * 从内容中读取json
     * @param reader {@link BufferedReader} 内容
     * @return json map
     */
    public static @Nonnull Map<String, String> readJsonFromBufferedReader(@Nonnull BufferedReader reader){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(reader, type);
    }
}
