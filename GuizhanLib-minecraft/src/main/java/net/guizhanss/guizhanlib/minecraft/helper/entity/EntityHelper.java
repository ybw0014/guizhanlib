package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

/**
 * 实体({@link Entity})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("unused")
public final class EntityHelper {
    /**
     * 返回实体({@link Entity})的显示名称
     *
     * @param entity {@link Entity} 实体
     * @return 实体的显示名称
     */
    @Nonnull
    public static String getDisplayName(@Nonnull Entity entity) {
        Preconditions.checkArgument(entity != null, "实体不能为空");

        return entity.getCustomName() != null ?
            entity.getCustomName() : getName(entity);
    }

    /**
     * 返回实体({@link Entity})的中文名称
     *
     * @param entity {@link Entity} 实体
     * @return 实体的中文名称, 如果获取失败则返回键名
     */
    @Nonnull
    public static String getName(@Nonnull Entity entity) {
        Preconditions.checkArgument(entity != null, "实体不能为空");

        return EntityTypeHelper.getName(entity.getType());
    }
}
