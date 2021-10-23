package net.guizhanss.minecraft.chineselib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Minecraft - 猫
 * @author ybw0014
 */
public class Cats {
    /**
     * 所有猫的类型
     */
    public enum Type {
        ALL_BLACK("Black", "黑猫"),
        BLACK("Tuxedo", "西服猫"),
        BRITISH_SHORTHAIR("British Shorthair", "英国短毛猫"),
        CALICO("Calico", "花猫"),
        JELLIE("Jellie", "Jellie"),
        PERSIAN("Persian", "波斯猫"),
        RAGDOLL("Ragdoll", "布偶猫"),
        RED("Red", "红虎斑猫"),
        SIAMESE("Siamese", "暹罗猫"),
        TABBY("Tabby", "虎斑猫"),
        WHITE("Calico", "白猫");

        private @Getter String english;
        private @Getter String chinese;

        Type(@Nonnull String english, @Nonnull String chinese) {
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
        public static Type fromEnglish(String english) {
            String humanized = StringUtil.humanize(english);
            for (Type type : Type.values()) {
                if (type.getEnglish().equals(humanized)) {
                    return type;
                }
            }
            return null;
        }
    }
}
