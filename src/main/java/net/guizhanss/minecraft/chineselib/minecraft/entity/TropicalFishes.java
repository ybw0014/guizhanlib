package net.guizhanss.minecraft.chineselib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;

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
        BETTY("Betty", "背蒂类"),
        BLOCKFISH("Blockfish", "方身类"),
        BRINELY("Brinely", "咸水类"),
        CLAYFISH("Clayfish", "陶鱼类"),
        DASHER("Dasher", "速跃类"),
        FLOPPER("Flopper", "飞翼类"),
        GLITTER("Glitter", "闪鳞类"),
        KOB("Kob", "石首类"),
        SNOOPER("Snooper", "窥伺类"),
        SPOTTY("Spotty", "多斑类"),
        STRIPEY("Stripey", "条纹类"),
        SUNSTREAK("Sunstreak", "日纹类");

        private @Getter String english;
        private @Getter String chinese;

        Pattern(@Nonnull String english, @Nonnull String chinese) {
            this.english = english;
            this.chinese = chinese;
        }

        @Override
        public String toString() {
            return this.chinese;
        }

        /**
         * 根据英文返回对应的枚举类型
         * @param english 提供的英文类型
         * @return 对应的枚举类型
         */
        @Nullable
        public static Pattern fromEnglish(String english) {
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
