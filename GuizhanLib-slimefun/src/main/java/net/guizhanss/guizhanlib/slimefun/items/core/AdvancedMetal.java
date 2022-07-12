package net.guizhanss.guizhanlib.slimefun.items.core;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.guizhanss.guizhanlib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

/**
 * Slimefun - 高级金属(合金)
 *
 * @author ybw0014
 */
@SuppressWarnings("ConstantConditions")
public enum AdvancedMetal {
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

    private static final AdvancedMetal[] valuesCache = values();
    private static final Map<String, AdvancedMetal> englishLookup = new HashMap<>();
    private static final Map<String, AdvancedMetal> chineseLookup = new HashMap<>();
    private static final Map<String, AdvancedMetal> slimefunIdLookup = new HashMap<>();

    static {
        for (AdvancedMetal metal : valuesCache) {
            englishLookup.put(metal.getEnglish(), metal);
            chineseLookup.put(metal.getChinese(), metal);
            slimefunIdLookup.put(metal.getSlimefunId(), metal);
        }
    }

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
    AdvancedMetal(String english, String chinese, String slimefunId) {
        this.english = english;
        this.chinese = chinese;
        this.slimefunId = slimefunId;
    }

    @ParametersAreNonnullByDefault
    AdvancedMetal(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
        this.slimefunId = StringUtil.dehumanize(english) + "_INGOT";
    }

    /**
     * 根据英文返回对应的枚举
     *
     * @param english {@link String} 提供的英文
     *
     * @return 对应的枚举
     */
    @Nullable
    public static AdvancedMetal fromEnglish(@Nonnull String english) {
        Preconditions.checkArgument(english != null, "英文不能为空");

        return englishLookup.get(StringUtil.humanize(english));
    }

    /**
     * 根据中文返回对应的枚举
     *
     * @param chinese {@link String} 提供的中文
     *
     * @return 对应的枚举
     */
    @Nullable
    public static AdvancedMetal fromChinese(@Nonnull String chinese) {
        Preconditions.checkArgument(chinese != null, "中文不能为空");

        return chineseLookup.get(chinese);
    }

    /**
     * 根据粘液物品ID返回对应的枚举
     *
     * @param id 粘液物品ID
     *
     * @return 对应的枚举
     */
    @Nullable
    public static AdvancedMetal fromSlimefunId(@Nonnull String id) {
        Preconditions.checkArgument(id != null, "ID不能为空");

        return slimefunIdLookup.get(id);
    }

    @Nonnull
    @Override
    public String toString() {
        return this.getChinese();
    }
}
