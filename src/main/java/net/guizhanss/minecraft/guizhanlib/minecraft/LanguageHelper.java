package net.guizhanss.minecraft.guizhanlib.minecraft;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 语言助手
 * 从 Minecraft 语言文件中提取字符串
 *
 * @author ybw0014
 */
public class LanguageHelper {

    private static final String filename = "minecraft.zh_cn.json";
    private static Map<String, String> lang;

    LanguageHelper() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            LanguageHelper.class.getResourceAsStream(filename), StandardCharsets.UTF_8
        ));
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        lang = gson.fromJson(reader, type);
    }

    /**
     * 返回物品({@link ItemStack})的显示名称
     *
     * @param item {@link ItemStack} 物品
     * @return 物品的显示名称
     */
    public @Nonnull String getItemDisplayName(@Nonnull ItemStack item) {
        Validate.notNull(item, "物品不能为空");

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
            return item.getItemMeta().getDisplayName();
        else
            return getItemName(item);
    }

    /**
     * 返回物品({@link ItemStack})的中文名称
     *
     * @param item {@link ItemStack} 物品
     * @return 物品的中文名称，如果获取失败则返回对应的键名
     */
    public @Nonnull String getItemName(@Nonnull ItemStack item) {
        Validate.notNull(item, "物品不能为空");

        if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION || item.getType() == Material.TIPPED_ARROW) {
            String potion = ((PotionMeta) item.getItemMeta()).getBasePotionData().getType().toString().toLowerCase();
            return translateToLocal(getMaterialKey(item.getType()) + ".effect." + potion);
        } else if (item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD) { // is player's skull
            return getPlayerSkullName(item);
        }

        return translateToLocal(getMaterialKey(item.getType()));
    }

    /**
     * 返回材料({@link Material})的中文名称
     *
     * @param mat {@link Material} 材料
     * @return 材料的中文名称，如果获取失败则返回对应的键名
     */
    public @Nonnull String getMaterialName(@Nonnull Material mat) {
        return translateToLocal(getMaterialKey(mat));
    }

    /**
     * 返回头颅物品({@link ItemStack})的中文名称
     * @param skull {@link ItemStack} 头颅物品
     * @return 头颅物品的中文名称
     */
    private @Nonnull String getPlayerSkullName(@Nonnull ItemStack skull) {
        Validate.notNull(skull, "物品不能为空");

        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null && meta.hasOwner()) {
            return String.format(translateToLocal("block.minecraft.player_head.named"),
                meta.getOwningPlayer().getName());
        } else return translateToLocal("block.minecraft.player_head");
    }

    /**
     * 返回生物群系({@link Biome})的中文名
     *
     * @param biome {@link Biome} 生物群系
     * @return 生物群系的中文名,如果获取失败则返回键名
     */
    public @Nonnull String getBiomeName(@Nonnull Biome biome) {
        return translateToLocal(getBiomeKey(biome));
    }

    /**
     * 返回实体({@link Entity})的显示名称
     *
     * @param entity {@link Entity} 实体
     * @return 实体的显示名称
     */
    public @Nonnull String getEntityDisplayName(@Nonnull Entity entity) {
        return entity.getCustomName() != null ? entity.getCustomName() :
            getEntityName(entity);
    }

    /**
     * 返回实体({@link Entity})的中文名称
     *
     * @param entity {@link Entity} 实体
     * @return 实体的中文名称,如果获取失败则返回键名
     */
    public @Nonnull String getEntityName(@Nonnull Entity entity) {
        return translateToLocal(getEntityKey(entity));
    }

    /**
     * 返回实体类型({@link EntityType})的中文名
     *
     * @param entityType {@link EntityType} 实体类型
     * @return 实体类型的中文名称,如果获取失败则返回键名
     */
    public @Nonnull String getEntityName(@Nonnull EntityType entityType) {
        return translateToLocal(getEntityKey(entityType));
    }

    /**
     * 返回附魔等级的名称
     *
     * @param level 附魔等级
     * @return 附魔等级的名称
     */
    public @Nonnull String getEnchantmentLevelName(int level) {
        return translateToLocal(getEnchantmentLevelKey(level));
    }

    /**
     * 返回附魔({@link Enchantment})的中文名称
     *
     * @param enchantment {@link Enchantment} 附魔
     * @return 附魔的中文名称,如果获取失败则返回键名
     */
    public @Nonnull String getEnchantmentName(@Nonnull Enchantment enchantment) {
        return translateToLocal(getEnchantmentKey(enchantment));
    }

    /**
     * 返回附魔({@link Enchantment})的中文名称与附魔等级
     *
     * @param enchantment {@link Enchantment} 附魔
     * @param level 附魔等级
     * @return 附魔的中文名称与等级
     */
    public @Nonnull String getEnchantmentDisplayName(@Nonnull Enchantment enchantment, int level) {
        Validate.notNull(enchantment, "附魔不能为空");

        String name = getEnchantmentName(enchantment);
        String enchLevel = getEnchantmentLevelName(level);
        return name + (enchLevel.length() > 0 ? " " + enchLevel : "");
    }

    /**
     * 返回附魔({@link Enchantment})的中文名称与附魔等级
     *
     * @param entry {@code Map.Entry<Enchantment, Integer>} 附魔与附魔等级
     * @return The name of the item
     */
    public @Nonnull String getEnchantmentDisplayName(@Nonnull Map.Entry<Enchantment, Integer> entry) {
        return getEnchantmentDisplayName(entry.getKey(), entry.getValue());
    }

    /**
     * 获取物品材料({@link Material})的键名
     *
     * @param mat {@link Material} 物品材料
     * @return 物品材料的键名
     */
    public @Nonnull String getMaterialKey(@Nonnull Material mat) {
        Validate.notNull(mat, "材料不能为空");

        return (mat.isBlock() ? "block" : "item") + "."
            + mat.getKey().getNamespace() + "."
            + mat.getKey().getKey();
    }

    /**
     * 获取生物群系({@link Biome})的键名
     *
     * @param biome {@link Biome} 生物群系
     * @return 生物群系的键名
     */
    public @Nonnull String getBiomeKey(@Nonnull Biome biome) {
        Validate.notNull(biome, "生物群系不能为空");

        return "biome.minecraft." + biome.toString().toLowerCase();
    }

    /**
     * 获取实体({@link Entity})的键名
     *
     * @param entity {@link Entity} 实体
     * @return 实体的键名
     */
    public @Nonnull String getEntityKey(@Nonnull Entity entity) {
        Validate.notNull(entity, "实体不能为空");

        return getEntityKey(entity.getType());
    }

    /**
     * 获取实体类型({@link EntityType})的键名
     *
     * @param entityType {@link EntityType} 实体类型
     * @return 实体类型的键名
     */
    public @Nonnull String getEntityKey(@Nonnull EntityType entityType) {
        Validate.notNull(entityType, "实体类型不能为空");

        if (entityType == EntityType.SNOWMAN)
            return "entity.minecraft.snow_golem";

        return "entity.minecraft." + entityType.toString().toLowerCase();
    }

    /**
     * 获取附魔等级的键名
     *
     * @param level 附魔等级
     * @return 附魔等级的键名
     */
    public @Nonnull String getEnchantmentLevelKey(int level) {
        return "enchantment.level." + level;
    }

    /**
     * 获取附魔({@link Enchantment})的键名
     *
     * @param enchantment {@link Enchantment} 附魔
     * @return 附魔的键名
     */
    public @Nonnull String getEnchantmentKey(@Nonnull Enchantment enchantment) {
        return "enchantment.minecraft." + enchantment.getKey().getKey();
    }

    /**
     * 获取语言文件中指定键名的内容
     *
     * @param key {@link String} 键名
     * @return 键名内容
     */
    public String translateToLocal(@Nonnull String key) {
        Validate.notNull(key, "键名不能为空");

        return lang.get(key);
    }
}
