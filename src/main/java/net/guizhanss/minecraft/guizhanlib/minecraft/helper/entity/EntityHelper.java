package net.guizhanss.minecraft.guizhanlib.minecraft.helper.entity;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

/**
 * 实体({@link Entity})
 *
 * @author ybw0014
 */
@UtilityClass
public class EntityHelper {
    /**
     * 返回实体({@link Entity})的显示名称
     *
     * @param entity {@link Entity} 实体
     * @return 实体的显示名称
     */
    public static @Nonnull String getEntityDisplayName(@Nonnull Entity entity) {
        Validate.notNull(entity, "实体不能为空");

        return entity.getCustomName() != null ? entity.getCustomName() :
            getEntityName(entity);
    }

    /**
     * 返回实体({@link Entity})的中文名称
     *
     * @param entity {@link Entity} 实体
     * @return 实体的中文名称,如果获取失败则返回键名
     */
    public static @Nonnull String getEntityName(@Nonnull Entity entity) {
        Validate.notNull(entity, "实体不能为空");

        return EntityTypeHelper.getEntityTypeName(entity.getType());
    }
}
