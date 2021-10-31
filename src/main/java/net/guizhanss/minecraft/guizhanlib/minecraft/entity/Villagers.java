package net.guizhanss.minecraft.guizhanlib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.guizhanlib.utils.StringUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Villager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Minecraft - 村民
 * @author ybw0014
 */
public class Villagers {
    /**
     * 所有村民的职业
     */
    public enum Profession {
        ARMORER(Villager.Profession.ARMORER, "Armorer", "盔甲匠"),
        BUTCHER(Villager.Profession.BUTCHER, "Butcher", "屠夫"),
        CARTOGRAPHER(Villager.Profession.CARTOGRAPHER, "Cartographer", "制图师"),
        CLERIC(Villager.Profession.CLERIC, "Cleric", "牧师"),
        FARMER(Villager.Profession.FARMER, "Farmer", "农民"),
        FISHERMAN(Villager.Profession.FISHERMAN, "Fisherman", "渔夫"),
        FLETCHER(Villager.Profession.FLETCHER, "Fletcher", "制箭师"),
        LEATHERWORKER(Villager.Profession.LEATHERWORKER, "Leatherworker", "制箭师"),
        LIBRARIAN(Villager.Profession.LIBRARIAN, "Librarian", "图书管理员"),
        MASON(Villager.Profession.MASON, "Mason", "石匠"),
        NITWIT(Villager.Profession.NITWIT, "Nitwit", "傻子"),
        NONE(Villager.Profession.NONE, "None", "无"),
        SHEPHERD(Villager.Profession.SHEPHERD, "Shepherd", "牧羊人"),
        TOOLSMITH(Villager.Profession.TOOLSMITH, "Toolsmith", "工具匠"),
        WEAPONSMITH(Villager.Profession.WEAPONSMITH, "Weaponsmith", "武器匠");

        private final @Getter Villager.Profession profession;
        private final @Getter String english;
        private final @Getter String chinese;

        @ParametersAreNonnullByDefault
        Profession(Villager.Profession profession, String english, String chinese) {
            this.profession = profession;
            this.english = english;
            this.chinese = chinese;
        }

        @Override
        public String toString() {
            return this.getChinese();
        }

        /**
         * 根据村民职业对应的枚举
         * @param villagerProfession {@link Villager.Profession} 村民职业
         * @return 对应的枚举
         */
        public static @Nonnull Profession fromProfession(@Nonnull Villager.Profession villagerProfession) {
            Validate.notNull(villagerProfession, "村民职业不能为空");

            for (Profession profession : Profession.values()) {
                if (profession.getProfession() == villagerProfession) {
                    return profession;
                }
            }
            throw new IllegalArgumentException("无效的村民职业");
        }

        /**
         * 根据英文返回对应的枚举
         * @param english {@link String} 提供的英文
         * @return 对应的枚举
         */
        public static @Nullable Profession fromEnglish(@Nonnull String english) {
            Validate.notNull(english, "英文不能为空");

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
