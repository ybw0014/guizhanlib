package net.guizhanss.guizhanlib.slimefun.core;

import lombok.Getter;
import net.guizhanss.guizhanlib.utils.StringUtil;

import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Slimefun - 高级金属(合金)
 * @author ybw0014
 */
public enum AdvancedMetals {
    STEEL("Steel", "钢"),
    BRONZE_INGOT("Bronze", "青铜"),
    DURALUMIN("Duralumin", "硬铝"),
    BILLON("Billon", "银铜合金"),
    BRASS("Brass", "黄铜"),
    ALUMINUM_BRASS("Aluminum Brass", "铝黄铜"),
    ALUMINUM_BRONZE("Aluminum Bronze", "铝青铜"),
    CORINTHIAN_BRONZE("Corinthian Bronze", "科林斯青铜"),
    SOLDER("Solder", "焊锡"),
    DAMASCUS_STEEL("Damascus Steel", "大马士革钢"),
    HARDENED_METAL("Hardened Metal", "硬化金属"),
    REINFORCED_ALLOY("Reinforced Alloy", "强化合金"),
    FERROSILICON("Ferrosilicon", "硅铁"),
    GILDED_IRON("Gilded Iron", "镀金铁"),
    REDSTONE_ALLOY("Redstone Alloy", "红石合金"),
    NICKEL("Nickel", "镍"),
    COBALT("Cobalt", "钴");

    private final @Getter String english;
    private final @Getter String chinese;

    @ParametersAreNonnullByDefault
    AdvancedMetals(String english, String chinese) {
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
    public static @Nullable AdvancedMetals fromEnglish(@Nonnull String english) {
        Validate.notNull(english, "英文不能为空");

        String humanized = StringUtil.humanize(english);
        for (AdvancedMetals metal : AdvancedMetals.values()) {
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
    public static @Nullable AdvancedMetals fromChinese(@Nonnull String chinese) {
        Validate.notNull(chinese, "中文不能为空");

        for (AdvancedMetals metal : AdvancedMetals.values()) {
            if (metal.getChinese().equals(chinese)) {
                return metal;
            }
        }
        return null;
    }
}
