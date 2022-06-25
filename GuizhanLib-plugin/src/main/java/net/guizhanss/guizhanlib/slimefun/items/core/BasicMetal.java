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
 * Slimefun - 基础金属
 * <p>
 * 共9种
 *
 * @author ybw0014
 */
@SuppressWarnings("ConstantConditions")
public enum BasicMetal {
    /**
     * 铁
     */
    IRON("Iron", "铁"),
    /**
     * 金
     */
    GOLD("Gold", "金"),
    /**
     * 锡
     */
    TIN("Tin", "锡"),
    /**
     * 铜
     */
    COPPER("Copper", "铜"),
    /**
     * 银
     */
    SILVER("Silver", "银"),
    /**
     * 铝
     */
    ALUMINUM("Aluminum", "铝"),
    /**
     * 铅
     */
    LEAD("Lead", "铅"),
    /**
     * 锌
     */
    ZINC("Zinc", "锌"),
    /**
     * 镁
     */
    MAGNESIUM("Magnesium", "镁");

    private static final BasicMetal[] valuesCache = values();
    private static final Map<String, BasicMetal> englishLookup = new HashMap<>();
    private static final Map<String, BasicMetal> chineseLookup = new HashMap<>();

    static {
        for (BasicMetal metal : valuesCache) {
            englishLookup.put(metal.getEnglish(), metal);
            chineseLookup.put(metal.getChinese(), metal);
        }
    }

    @Getter
    private final String english;
    @Getter
    private final String chinese;

    @ParametersAreNonnullByDefault
    BasicMetal(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    /**
     * 根据英文返回对应的枚举
     *
     * @param english {@link String} 提供的英文
     * @return 对应的枚举
     */
    @Nullable
    public static BasicMetal fromEnglish(@Nonnull String english) {
        Preconditions.checkArgument(english != null, "英文不能为空");

        return englishLookup.get(StringUtil.humanize(english));
    }

    /**
     * 根据中文返回对应的枚举
     *
     * @param chinese {@link String} 提供的中文
     * @return 对应的枚举
     */
    @Nullable
    public static BasicMetal fromChinese(@Nonnull String chinese) {
        Preconditions.checkArgument(chinese != null, "中文不能为空");

        return chineseLookup.get(chinese);
    }

    @Nonnull
    @Override
    public String toString() {
        return this.getChinese();
    }
}
