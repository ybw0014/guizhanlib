package net.guizhanss.minecraft.chineselib.slimefun.core;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Slimefun - 基础金属
 * 共 9 种
 * @author ybw0014
 */
public enum BasicMetals {
    IRON("Iron", "铁"),
    GOLD("Gold", "金"),
    TIN("Tin", "锡"),
    COPPER("Copper", "铜"),
    SILVER("Silver", "银"),
    ALUMINUM("Aluminum", "铝"),
    LEAD("Lead", "铅"),
    ZINC("Zinc", "锌"),
    MAGNESIUM("Magnesium", "镁");

    private final @Getter String english;
    private final @Getter String chinese;

    @ParametersAreNonnullByDefault
    BasicMetals(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    @Override
    public String toString() {
        return this.getChinese();
    }

    /**
     * 根据英文返回对应的枚举
     * @param english {@link String} 提供的英文
     * @return 对应的枚举
     */
    public static @Nullable BasicMetals fromEnglish(@Nonnull String english) {
        Validate.notNull(english, "英文不能为空");

        String humanized = StringUtil.humanize(english);
        for (BasicMetals metal : BasicMetals.values()) {
            if (metal.getEnglish().equals(humanized)) {
                return metal;
            }
        }
        return null;
    }

    /**
     * 根据中文返回对应的枚举
     * @param chinese {@link String} 提供的中文
     * @return 对应的枚举
     */
    public static @Nullable BasicMetals fromChinese(@Nonnull String chinese) {
        Validate.notNull(chinese, "中文不能为空");

        for (BasicMetals metal : BasicMetals.values()) {
            if (metal.getChinese().equals(chinese)) {
                return metal;
            }
        }
        return null;
    }
}
