package net.guizhanss.minecraft.guizhanlib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.guizhanlib.utils.StringUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Panda;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Minecraft - 熊猫
 * @author ybw0014
 */
public class Pandas {
    /**
     * 所有熊猫基因
     */
    public enum Gene {
        AGGRESSIVE(Panda.Gene.AGGRESSIVE, "Aggressive", "好斗"),
        BROWN(Panda.Gene.BROWN, "Brown", "棕色"),
        LAZY(Panda.Gene.LAZY, "Lazy", "懒惰"),
        NORMAL(Panda.Gene.NORMAL, "Normal", "普通"),
        PLAYFUL(Panda.Gene.PLAYFUL, "Playful", "顽皮"),
        WEAK(Panda.Gene.WEAK, "Weak", "虚弱"),
        WORRIED(Panda.Gene.WORRIED, "Worried", "发愁");

        private final @Getter Panda.Gene gene;
        private final @Getter String english;
        private final @Getter String chinese;

        @ParametersAreNonnullByDefault
        Gene(Panda.Gene gene, String english, String chinese) {
            this.gene = gene;
            this.english = english;
            this.chinese = chinese;
        }

        @Override
        public String toString() {
            return this.getChinese();
        }

        /**
         * 根据熊猫基因返回对应的枚举
         * @param pandaGene {@link Panda.Gene} 熊猫基因
         * @return 对应的枚举
         */
        public static @Nonnull Gene fromGene(@Nonnull Panda.Gene pandaGene) {
            Validate.notNull(pandaGene, "熊猫基因不能为空");

            for (Gene gene : Gene.values()) {
                if (gene.getGene() == pandaGene) {
                    return gene;
                }
            }
            throw new IllegalArgumentException("无效的熊猫基因");
        }

        /**
         * 根据英文返回对应的枚举
         * @param english {@link String} 提供的英文
         * @return 对应的枚举
         */
        public static @Nullable Gene fromEnglish(@Nonnull String english) {
            Validate.notNull(english, "英文不能为空");

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
