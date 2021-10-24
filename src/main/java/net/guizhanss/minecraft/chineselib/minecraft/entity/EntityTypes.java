package net.guizhanss.minecraft.chineselib.minecraft.entity;

import lombok.Getter;
import net.guizhanss.minecraft.chineselib.utils.StringUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 所有实体类型
 * @author ybw0014
 */
public enum EntityTypes {
    AREA_EFFECT_CLOUD(EntityType.AREA_EFFECT_CLOUD, "Area Effect Cloud", "范围效果云"),
    ARMOR_STAND(EntityType.ARMOR_STAND, "Armor Stand", "盔甲架"),
    ARROW(EntityType.ARROW, "Arrow", "箭"),
    AXOLOTL(EntityType.AXOLOTL, "Axolotl", "美西螈"),
    BAT(EntityType.BAT, "Bat", "蝙蝠"),
    BEE(EntityType.BEE, "Bee", "蜜蜂"),
    BLAZE(EntityType.BLAZE, "Blaze", "烈焰人"),
    BOAT(EntityType.BOAT, "Boat", "船"),
    CAT(EntityType.CAT, "Cat", "猫"),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, "Cave Spider", "洞穴蜘蛛"),
    CHICKEN(EntityType.CHICKEN, "Chicken", "鸡"),
    COD(EntityType.COD, "Cod", "鳕鱼"),
    COW(EntityType.COW, "Cow", "牛"),
    CREEPER(EntityType.CREEPER, "Creeper", "苦力怕"),
    DOLPHIN(EntityType.DOLPHIN, "Dolphin", "海豚"),
    DONKEY(EntityType.DONKEY, "Donkey", "驴"),
    DRAGON_FIREBALL(EntityType.DRAGON_FIREBALL, "Dragon Fireball", "末影龙火球"),
    DROPPED_ITEM(EntityType.DROPPED_ITEM, "Dropped Item", "掉落的物品"),
    DROWNED(EntityType.DROWNED, "Drowned", "溺尸"),
    EGG(EntityType.EGG, "Egg", "蛋"),
    ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, "Elder Guardian", "远古守卫者"),
    ENDER_CRYSTAL(EntityType.ENDER_CRYSTAL, "Ender Crystal", "末影水晶"),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, "Ender Dragon", "末影龙"),
    ENDER_PEARL(EntityType.ENDER_PEARL, "Ender Pearl", "末影珍珠"),
    ENDER_SIGNAL(EntityType.ENDER_SIGNAL, "Ender Signal", "末影之眼"),
    ENDERMAN(EntityType.ENDERMAN, "Enderman", "末影人"),
    ENDERMITE(EntityType.ENDERMITE, "Endermite", "末影螨"),
    EVOKER(EntityType.EVOKER, "Evoker", "唤魔者"),
    EVOKER_FANGS(EntityType.EVOKER_FANGS, "Evoker Fangs", "唤魔者尖牙"),
    EXPERIENCE_ORB(EntityType.EXPERIENCE_ORB, "Experience Orb", "经验球"),
    FALLING_BLOCK(EntityType.FALLING_BLOCK, "Falling Block", "掉落的方块"),
    FIREBALL(EntityType.FIREBALL, "Fireball", "火球"),
    FIREWORK(EntityType.FIREWORK, "Firework", "烟花"),
    FISHING_HOOK(EntityType.FISHING_HOOK, "Fishing Hook", "鱼钩"),
    FOX(EntityType.FOX, "Fox", "狐狸"),
    GHAST(EntityType.GHAST, "Ghast", "恶魂"),
    GIANT(EntityType.GIANT, "Giant", "巨人"),
    GLOW_ITEM_FRAME(EntityType.GLOW_ITEM_FRAME, "Glow Item Frame", "荧光物品展示框"),
    GLOW_SQUID(EntityType.GLOW_SQUID, "Glow Squid", "发光鱿鱼"),
    GOAT(EntityType.GOAT, "Goat", "山羊"),
    GUARDIAN(EntityType.GUARDIAN, "Guardian", "守卫者"),
    HOGLIN(EntityType.HOGLIN, "Hoglin", "疣猪兽"),
    HORSE(EntityType.HORSE, "Horse", "马"),
    HUSK(EntityType.HUSK, "Husk", "尸壳"),
    ILLUSIONER(EntityType.ILLUSIONER, "Illusioner", "幻术师"),
    IRON_GOLEM(EntityType.IRON_GOLEM, "Iron Golem", "铁傀儡"),
    ITEM_FRAME(EntityType.ITEM_FRAME, "Item Frame", "物品展示框"),
    LEASH_HITCH(EntityType.LEASH_HITCH, "Leash Hitch", "栅栏上的拴绳"),
    LIGHTNING(EntityType.LIGHTNING, "Lightning", "闪电"),
    LLAMA(EntityType.LLAMA, "Llama", "羊驼"),
    LLAMA_SPIT(EntityType.LLAMA_SPIT, "Llama Spit", "羊驼唾沫"),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, "Magma Cube", "岩浆怪"),
    MARKER(EntityType.MARKER, "Marker", "标记"),
    MINECART(EntityType.MINECART, "Minecart", "矿车"),
    MINECART_CHEST(EntityType.MINECART_CHEST, "Minecart with Chest", "运输矿车"),
    MINECART_COMMAND(EntityType.MINECART_COMMAND, "Minecart with Command Block", "命令方块矿车"),
    MINECART_FURNACE(EntityType.MINECART_FURNACE, "Minecart with Furnace", "动力矿车"),
    MINECART_HOPPER(EntityType.MINECART_HOPPER, "Minecart with Hopper", "漏斗矿车"),
    MINECART_MOB_SPAWNER(EntityType.MINECART_MOB_SPAWNER, "Minecart with Spawner", "刷怪笼矿车"),
    MINECART_TNT(EntityType.MINECART_TNT, "Minecart with TNT", "TNT矿车"),
    MULE(EntityType.MULE, "Mule", "骡"),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, "Mooshroom", "哞菇"),
    OCELOT(EntityType.OCELOT, "Ocelot", "豹猫"),
    PAINTING(EntityType.PAINTING, "Painting", "画"),
    PANDA(EntityType.PANDA, "Panda", "熊猫"),
    PARROT(EntityType.PARROT, "Parrot", "鹦鹉"),
    PHANTOM(EntityType.PHANTOM, "Phantom", "幻翼"),
    PIG(EntityType.PIG, "Pig", "猪"),
    PIGLIN(EntityType.PIGLIN, "Piglin", "猪灵"),
    PIGLIN_BRUTE(EntityType.PIGLIN_BRUTE, "Piglin Brute", "猪灵蛮兵"),
    PILLAGER(EntityType.PILLAGER, "Pillager", "掠夺者"),
    PLAYER(EntityType.PLAYER, "Player", "玩家"),
    POLAR_BEAR(EntityType.POLAR_BEAR, "Polar Bear", "北极熊"),
    PRIMED_TNT(EntityType.PRIMED_TNT, "Primed TNT", "引爆的TNT"),
    PUFFERFISH(EntityType.PUFFERFISH, "Pufferfish", "河豚"),
    RABBIT(EntityType.RABBIT, "Rabbit", "兔子"),
    RAVAGER(EntityType.RAVAGER, "Ravager", "劫掠兽"),
    SALMON(EntityType.SALMON, "Salmon", "鲑鱼"),
    SHEEP(EntityType.SHEEP, "Sheep", "绵羊"),
    SHULKER(EntityType.SHULKER, "Shulker", "潜影贝"),
    SHULKER_BULLET(EntityType.SHULKER_BULLET, "Shulker Bullet", "潜影贝导弹"),
    SILVERFISH(EntityType.SILVERFISH, "Silverfish", "蠹虫"),
    SKELETON(EntityType.SKELETON, "Skeleton", "骷髅"),
    SKELETON_HORSE(EntityType.SKELETON_HORSE, "Skeleton Horse", "骷髅马"),
    SLIME(EntityType.SLIME, "Slime", "史莱姆"),
    SMALL_FIREBALL(EntityType.SMALL_FIREBALL, "Small Fireball", "小型火球"),
    SNOWBALL(EntityType.SNOWBALL, "Snowball", "雪球"),
    SNOWMAN(EntityType.SNOWMAN, "Snow Golem", "雪傀儡"),
    SPECTRAL_ARROW(EntityType.SPECTRAL_ARROW, "Spectral Arrow", "光灵箭"),
    SPIDER(EntityType.SPIDER, "Spider", "蜘蛛"),
    SPLASH_POTION(EntityType.SPLASH_POTION, "Splash Potion", "喷溅药水"),
    SQUID(EntityType.SQUID, "Squid", "鱿鱼"),
    STRAY(EntityType.STRAY, "Stray", "流浪者"),
    STRIDER(EntityType.STRIDER, "Strider", "炽足兽"),
    THROWN_EXP_BOTTLE(EntityType.THROWN_EXP_BOTTLE, "Thrown Bottle o' Enchanting", "丢出的附魔之瓶"),
    TRADER_LLAMA(EntityType.TRADER_LLAMA, "Trader Llama", "行商羊驼"),
    TRIDENT(EntityType.TRIDENT, "Trident", "三叉戟"),
    TROPICAL_FISH(EntityType.TROPICAL_FISH, "Tropical Fish", "热带鱼"),
    TURTLE(EntityType.TURTLE, "Turtle", "海龟"),
    UNKNOWN(EntityType.UNKNOWN, "Unknown", "未知"),
    VEX(EntityType.VEX, "Vex", "恼鬼"),
    VILLAGER(EntityType.VILLAGER, "Villager", "村民"),
    VINDICATOR(EntityType.VINDICATOR, "Vindicator", "卫道士"),
    WANDERING_TRADER(EntityType.WANDERING_TRADER, "Wandering Trader", "流浪商人"),
    WITCH(EntityType.WITCH, "Witch", "女巫"),
    WITHER(EntityType.WITHER, "Wither", "凋灵"),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, "Wither Skeleton", "凋灵骷髅"),
    WITHER_SKULL(EntityType.WITHER_SKULL, "Wither Skull", "凋灵之首"),
    WOLF(EntityType.WOLF, "Wolf", "狼"),
    ZOGLIN(EntityType.ZOGLIN, "Zoglin", "僵尸疣猪兽"),
    ZOMBIE(EntityType.ZOMBIE, "Zombie", "僵尸"),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, "Zombie Horse", "僵尸马"),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, "Zombie Villager", "僵尸村民"),
    ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN, "Zombified Piglin", "僵尸猪灵");

    private final @Getter EntityType type;
    private final @Getter String english;
    private final @Getter String chinese;

    @ParametersAreNonnullByDefault
    EntityTypes(EntityType type, String english, String chinese) {
        this.type = type;
        this.english = english;
        this.chinese = chinese;
    }

    @Override
    public String toString() {
        return this.getChinese();
    }

    /**
     * 根据实体类型返回对应的枚举
     * @param entityType {@link EntityType} 提供的实体类型
     * @return 对应的枚举
     */
    public static @Nonnull EntityTypes fromEntityType(@Nonnull EntityType entityType) {
        Validate.notNull(entityType, "实体类型不能为空");

        for (EntityTypes type : EntityTypes.values()) {
            if (type.getType() == entityType) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的实体类型");
    }

    /**
     * 根据英文返回对应的枚举
     * @param english {@link String} 提供的英文
     * @return 对应的枚举
     */
    public static @Nullable EntityTypes fromEnglish(@Nonnull String english) {
        Validate.notNull(english, "英文不能为空");

        String humanized = StringUtil.humanize(english);
        for (EntityTypes type : EntityTypes.values()) {
            if (type.getEnglish().equals(humanized)) {
                return type;
            }
        }
        return null;
    }
}
