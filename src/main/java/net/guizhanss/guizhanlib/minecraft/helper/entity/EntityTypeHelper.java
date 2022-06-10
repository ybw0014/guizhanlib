package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.LanguageHelper;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;

/**
 * 实体类型({@link EntityType})
 *
 * @author ybw0014
 */
@UtilityClass
public final class EntityTypeHelper {
    /**
     * 返回实体类型({@link EntityType})的中文名
     *
     * @param entityType {@link EntityType} 实体类型
     *
     * @return 实体类型的中文名称,如果获取失败则返回键名
     */
    @Nonnull
    public static String getName(@Nonnull EntityType entityType) {
        Preconditions.checkNotNull(entityType, "实体类型不能为空");

        // 1.16 中移除的类型
        if (entityType.toString().equals("PIG_ZOMBIE"))
            return "僵尸猪人";

        return LanguageHelper.getLangOrKey(getKey(entityType));
    }

    /**
     * 获取实体类型({@link EntityType})的键名
     *
     * @param entityType {@link EntityType} 实体类型
     *
     * @return 实体类型的键名
     */
    @Nonnull
    public static String getKey(@Nonnull EntityType entityType) {
        Preconditions.checkNotNull(entityType, "实体类型不能为空");
        Preconditions.checkArgument(entityType != EntityType.UNKNOWN, "实体类型不能为无效类型");

        return "entity.minecraft." + entityType.getName().toLowerCase();
    }

    /**
     * 根据给定的实体类型字符串返回中文名称
     *
     * @param entityType 实体类型的{@link String}格式
     *
     * @return 实体类型的中文名称，如果获取失败则返回对应的键名
     */
    @Nonnull
    public static String getName(@Nonnull String entityType) {
        try {
            EntityType type = EntityType.valueOf(entityType);
            return getName(type);
        } catch (IllegalArgumentException ex) {
            return "";
        }
    }
}
