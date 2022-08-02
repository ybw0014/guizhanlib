package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.entity.Fox;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;

/**
 * 狐狸({@link Fox})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings({"unused", "ConstantConditions"})
public final class FoxHelper {
    /**
     * 获取狐狸的类型({@link Fox.Type})的中文
     *
     * @param type {@link Fox.Type} 狐狸的类型
     *
     * @return 狐狸的类型的中文
     */
    @Nonnull
    public static String getType(@Nonnull Fox.Type type) {
        return Type.fromType(type).getChinese();
    }

    /**
     * 获取狐狸的类型({@link Fox.Type})的中文
     *
     * @param type {@link String} 狐狸的类型
     *
     * @return 狐狸的类型的中文
     */
    @Nonnull
    public static String getType(@Nonnull String type) {
        Preconditions.checkArgument(type != null, "狐狸类型不能为空");

        try {
            Fox.Type foxType = Fox.Type.valueOf(type);
            return Type.fromType(foxType).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(type);
        }
    }

    /**
     * 所有狐狸的类型
     */
    public enum Type {
        /**
         * 红狐
         */
        RED(Fox.Type.RED, "红狐"),
        /**
         * 雪狐
         */
        SNOW(Fox.Type.SNOW, "雪狐");

        private static final Type[] cachedValues = values();
        private static final Map<Fox.Type, Type> typeLookup = new EnumMap<>(Fox.Type.class);

        static {
            for (Type type : cachedValues) {
                typeLookup.put(type.getType(), type);
            }
        }

        @Getter
        private final Fox.Type type;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Type(Fox.Type type, String chinese) {
            this.type = type;
            this.chinese = chinese;
        }

        /**
         * 获取狐狸的类型
         *
         * @param foxType {@link Fox.Type} 狐狸的类型
         *
         * @return 狐狸的类型
         */
        @Nonnull
        public static Type fromType(@Nonnull Fox.Type foxType) {
            Preconditions.checkArgument(foxType != null, "狐狸类型不能为空");

            return typeLookup.get(foxType);
        }

        @Nonnull
        @Override
        public String toString() {
            return this.getChinese();
        }
    }
}
