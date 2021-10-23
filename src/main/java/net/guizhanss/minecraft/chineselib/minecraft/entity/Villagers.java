package net.guizhanss.minecraft.chineselib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Minecraft - 村民
 * @author ybw0014
 */
public class Villagers {
    /**
     * 所有村民的职业
     */
    public enum Profession {
        ARMORER("Armorer", "盔甲匠"),
        BUTCHER("Butcher", "屠夫"),
        CARTOGRAPHER("Cartographer", "制图师"),
        CLERIC("Cleric", "牧师"),
        FARMER("Farmer", "农民"),
        FISHERMAN("Fisherman", "渔夫"),
        FLETCHER("Fletcher", "制箭师"),
        LEATHERWORKER("Leatherworker", "制箭师"),
        LIBRARIAN("Librarian", "图书管理员"),
        MASON("Mason", "石匠"),
        NITWIT("Nitwit", "傻子"),
        NONE("None", "无"),
        SHEPHERD("Shepherd", "牧羊人"),
        TOOLSMITH("Toolsmith", "工具匠"),
        WEAPONSMITH("Weaponsmith", "武器匠");

        private @Getter String english;
        private @Getter String chinese;

        Profession(@Nonnull String english, @Nonnull String chinese) {
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
        public static Profession fromEnglish(String english) {
            String humanized = StringUtil.humanize(english);
            for (Profession profession : Profession.values()) {
                if (profession.getEnglish().equals(humanized)) {
                    return profession;
                }
            }
            return null;
        }
    }
}
