package net.guizhanss.guizhanlib.minecraft.helper.entity;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.utils.StringUtil;
import org.bukkit.entity.Panda;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;

/**
 * 熊猫({@link Panda})
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings({"unused", "ConstantConditions"})
public final class PandaHelper {
    /**
     * 获取熊猫基因({@link Panda.Gene})的中文
     *
     * @param gene {@link Panda.Gene} 熊猫基因
     *
     * @return 熊猫基因的中文
     */
    @Nonnull
    public static String getGene(@Nonnull Panda.Gene gene) {
        return Gene.fromGene(gene).getChinese();
    }

    /**
     * 获取熊猫基因({@link Panda.Gene})的中文
     *
     * @param gene {@link String} 熊猫基因
     *
     * @return 熊猫基因的中文
     */
    @Nonnull
    public static String getGene(@Nonnull String gene) {
        Preconditions.checkArgument(gene != null, "熊猫基因不能为空");

        try {
            Panda.Gene pandaGene = Panda.Gene.valueOf(gene);
            return Gene.fromGene(pandaGene).getChinese();
        } catch (IllegalArgumentException ex) {
            return StringUtil.humanize(gene);
        }
    }

    /**
     * 所有熊猫基因
     */
    public enum Gene {
        /**
         * 好斗
         */
        AGGRESSIVE(Panda.Gene.AGGRESSIVE, "好斗"),
        /**
         * 棕色
         */
        BROWN(Panda.Gene.BROWN, "棕色"),
        /**
         * 懒惰
         */
        LAZY(Panda.Gene.LAZY, "懒惰"),
        /**
         * 普通
         */
        NORMAL(Panda.Gene.NORMAL, "普通"),
        /**
         * 顽皮
         */
        PLAYFUL(Panda.Gene.PLAYFUL, "顽皮"),
        /**
         * 虚弱
         */
        WEAK(Panda.Gene.WEAK, "虚弱"),
        /**
         * 发愁
         */
        WORRIED(Panda.Gene.WORRIED, "发愁");

        private static final Gene[] cachedValues = values();
        private static final Map<Panda.Gene, Gene> geneLookup = new EnumMap<>(Panda.Gene.class);

        static {
            for (Gene gene : cachedValues) {
                geneLookup.put(gene.getGene(), gene);
            }
        }

        @Getter
        private final Panda.Gene gene;
        @Getter
        private final String chinese;

        @ParametersAreNonnullByDefault
        Gene(Panda.Gene gene, String chinese) {
            this.gene = gene;
            this.chinese = chinese;
        }

        /**
         * 获取熊猫基因
         *
         * @param pandaGene {@link Panda.Gene} 熊猫基因
         *
         * @return 熊猫基因
         */
        @Nonnull
        public static Gene fromGene(@Nonnull Panda.Gene pandaGene) {
            Preconditions.checkArgument(pandaGene != null, "熊猫基因不能为空");

            for (Gene gene : Gene.values()) {
                if (gene.getGene() == pandaGene) {
                    return gene;
                }
            }
            throw new IllegalArgumentException("无效的熊猫基因");
        }

        @Nonnull
        @Override
        public String toString() {
            return this.getChinese();
        }
    }
}
