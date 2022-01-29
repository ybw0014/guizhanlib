package net.guizhanss.guizhanlib.minecraft.helper.entity;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;

/**
 * 实体类型({@link EntityType})
 *
 * @author ybw0014
 */
@UtilityClass
public class EntityTypeHelper {
    /**
     * 返回实体类型({@link EntityType})的中文名
     *
     * @param entityType {@link EntityType} 实体类型
     * @return 实体类型的中文名称,如果获取失败则返回键名
     */
    public static @Nonnull String getName(@Nonnull EntityType entityType) {
        Validate.notNull(entityType, "实体类型不能为空");

        // 1.16 中移除的类型
        if (entityType.toString().equals("PIG_ZOMBIE"))
            return "僵尸猪人";

        return LanguageHelper.getLang(getKey(entityType));
    }

    /**
     * 获取实体类型({@link EntityType})的键名
     *
     * @param entityType {@link EntityType} 实体类型
     * @return 实体类型的键名
     */
    public static @Nonnull String getKey(@Nonnull EntityType entityType) {
        Validate.notNull(entityType, "实体类型不能为空");

        if (entityType == EntityType.SNOWMAN)
            return "entity.minecraft.snow_golem";

        return "entity.minecraft." + entityType.toString().toLowerCase();
    }

    /**
     * 根据给定的实体类型字符串返回中文名称
     * @param entityType 实体类型的{@link String}格式
     * @return 实体类型的中文名称，如果获取失败则返回对应的键名
     */
    public static @Nonnull String getNameByString(@Nonnull String entityType) {
        try {
            EntityType type = EntityType.valueOf(entityType);
            return getName(type);
        } catch (IllegalArgumentException ex) {
            return "";
        }
    }
}
