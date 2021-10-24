package net.guizhanss.minecraft.chineselib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.TropicalFish;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Minecraft - 热带鱼
 * @author ybw0014
 */
public class TropicalFishes extends Fishes {
    /**
     * 所有热带鱼的样式
     */
    public enum Pattern {
        BETTY(TropicalFish.Pattern.BETTY, "Betty", "背蒂类"),
        BLOCKFISH(TropicalFish.Pattern.BLOCKFISH, "Blockfish", "方身类"),
        BRINELY(TropicalFish.Pattern.BRINELY, "Brinely", "咸水类"),
        CLAYFISH(TropicalFish.Pattern.CLAYFISH, "Clayfish", "陶鱼类"),
        DASHER(TropicalFish.Pattern.DASHER, "Dasher", "速跃类"),
        FLOPPER(TropicalFish.Pattern.FLOPPER, "Flopper", "飞翼类"),
        GLITTER(TropicalFish.Pattern.GLITTER, "Glitter", "闪鳞类"),
        KOB(TropicalFish.Pattern.KOB, "Kob", "石首类"),
        SNOOPER(TropicalFish.Pattern.SNOOPER, "Snooper", "窥伺类"),
        SPOTTY(TropicalFish.Pattern.SPOTTY, "Spotty", "多斑类"),
        STRIPEY(TropicalFish.Pattern.STRIPEY, "Stripey", "条纹类"),
        SUNSTREAK(TropicalFish.Pattern.SUNSTREAK, "Sunstreak", "日纹类");

        private final @Getter TropicalFish.Pattern pattern;
        private final @Getter String english;
        private final @Getter String chinese;

        Pattern(TropicalFish.Pattern pattern, String english, String chinese) {
            this.pattern = pattern;
            this.english = english;
            this.chinese = chinese;
        }

        @Override
        public String toString() {
            return this.getChinese();
        }

        /**
         * 根据热带鱼样式返回对应的枚举
         * @param tropicalFishPattern {@link TropicalFish.Pattern} 热带鱼样式
         * @return 对应的枚举
         */
        public static @Nonnull Pattern fromPattern(@Nonnull TropicalFish.Pattern tropicalFishPattern) {
            Validate.notNull(tropicalFishPattern, "热带鱼样式不能为空");

            for (Pattern pattern : Pattern.values()) {
                if (pattern.getPattern() == tropicalFishPattern) {
                    return pattern;
                }
            }
            throw new IllegalArgumentException("无效的热带鱼样式");
        }

        /**
         * 根据英文返回对应的枚举
         * @param english {@link String} 提供的英文
         * @return 对应的枚举
         */
        public static @Nullable Pattern fromEnglish(@Nonnull String english) {
            Validate.notNull(english, "英文不能为空");

            String humanized = StringUtil.humanize(english);
            for (Pattern pattern : Pattern.values()) {
                if (pattern.getEnglish().equals(humanized)) {
                    return pattern;
                }
            }
            return null;
        }
    }
}
