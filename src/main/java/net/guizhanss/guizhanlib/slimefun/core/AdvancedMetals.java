package net.guizhanss.guizhanlib.slimefun.core;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.guizhanss.guizhanlib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Slimefun - 高级金属(合金)
 *
 * @author ybw0014
 */
public enum AdvancedMetals {
    /**
     * 钢
     */
    STEEL("Steel", "钢"),
    /**
     * 青铜
     */
    BRONZE("Bronze", "青铜"),
    /**
     * 硬铝
     */
    DURALUMIN("Duralumin", "硬铝"),
    /**
     * 银铜合金
     */
    BILLON("Billon", "银铜合金"),
    /**
     * 黄铜
     */
    BRASS("Brass", "黄铜"),
    /**
     * 铝黄铜
     */
    ALUMINUM_BRASS("Aluminum Brass", "铝黄铜"),
    /**
     * 铝青铜
     */
    ALUMINUM_BRONZE("Aluminum Bronze", "铝青铜"),
    /**
     * 科林斯青铜
     */
    CORINTHIAN_BRONZE("Corinthian Bronze", "科林斯青铜"),
    /**
     * 焊锡
     */
    SOLDER("Solder", "焊锡"),
    /**
     * 大马士革钢
     */
    DAMASCUS_STEEL("Damascus Steel", "大马士革钢"),
    /**
     * 硬化金属
     */
    HARDENED_METAL("Hardened Metal", "硬化金属"),
    /**
     * 强化合金
     */
    REINFORCED_ALLOY("Reinforced Alloy", "强化合金"),
    /**
     * 硅铁
     */
    FERROSILICON("Ferrosilicon", "硅铁", "FERROSILICON"),
    /**
     * 镀金铁
     */
    GILDED_IRON("Gilded Iron", "镀金铁", "GILDED_IRON"),
    /**
     * 红石合金
     */
    REDSTONE_ALLOY("Redstone Alloy", "红石合金", "REDSTONE_ALLOY"),
    /**
     * 镍
     */
    NICKEL("Nickel", "镍"),
    /**
     * 钴
     */
    COBALT("Cobalt", "钴");

    @Getter
    private final String english;
    @Getter
    private final String chinese;
    /**
     * 该合金锭的ID
     */
    @Getter
    private final String slimefunId;

    @ParametersAreNonnullByDefault
    AdvancedMetals(String english, String chinese, String slimefunId) {
        this.english = english;
        this.chinese = chinese;
        this.slimefunId = slimefunId;
    }

    @ParametersAreNonnullByDefault
    AdvancedMetals(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
        this.slimefunId = StringUtil.dehumanize(english) + "_INGOT";
    }

    @Nonnull
    @Override
    public String toString() {
        return this.getChinese();
    }

    /**
     * 根据英文返回对应的枚举
     *
     * @param english {@link String} 提供的英文
     * @return 对应的枚举
     */
    @Nullable
    public static AdvancedMetals fromEnglish(@Nonnull String english) {
        Preconditions.checkNotNull(english, "英文不能为空");

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
     *
     * @param chinese {@link String} 提供的中文
     * @return 对应的枚举
     */
    @Nullable
    public static AdvancedMetals fromChinese(@Nonnull String chinese) {
        Preconditions.checkNotNull(chinese, "中文不能为空");

        for (AdvancedMetals metal : AdvancedMetals.values()) {
            if (metal.getChinese().equals(chinese)) {
                return metal;
            }
        }
        return null;
    }
}
