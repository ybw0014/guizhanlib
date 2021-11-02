package net.guizhanss.minecraft.guizhanlib.minecraft;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 语言助手
 * 从语言文件中加载内容
 *
 * @author ybw0014
 */
@UtilityClass
public class LanguageHelper {

    private static final String filename = "/minecraft.zh_cn.json";
    private static final Map<String, String> lang;

    static {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            LanguageHelper.class.getResourceAsStream(filename), StandardCharsets.UTF_8
        ));
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        lang = gson.fromJson(reader, type);
    }

    /**
     * 获取语言文件中指定键名的内容
     *
     * @param key {@link String} 键名
     * @return 键名内容
     */
    public static String getLang(@Nonnull String key) {
        Validate.notNull(key, "键名不能为空");

        return lang.get(key);
    }
}
