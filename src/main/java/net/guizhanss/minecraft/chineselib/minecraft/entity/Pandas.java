package net.guizhanss.minecraft.chineselib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Minecraft - 熊猫
 * @author ybw0014
 */
public class Pandas {
    /**
     * 所有熊猫基因
     */
    public enum Gene {
        AGGRESSIVE("Aggressive", "好斗"),
        BROWN("Brown", "棕色"),
        LAZY("Lazy", "懒惰"),
        NORMAL("Normal", "普通"),
        PLAYFUL("Playful", "顽皮"),
        WEAK("Weak", "虚弱"),
        WORRIED("Worried", "发愁");

        private @Getter String english;
        private @Getter String chinese;

        Gene(@Nonnull String english, @Nonnull String chinese) {
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
        public static Gene fromEnglish(String english) {
            String humanized = StringUtil.humanize(english);
            for (Gene gene : Gene.values()) {
                if (gene.getEnglish().equals(humanized)) {
                    return gene;
                }
            }
            return null;
        }
    }
}
