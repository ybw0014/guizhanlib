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
    AREA_EFFECT_CLOUD("AREA_EFFECT_CLOUD", "Area Effect Cloud", "范围效果云"),
    ARMOR_STAND("ARMOR_STAND", "Armor Stand", "盔甲架"),
    ARROW("ARROW", "Arrow", "箭"),
    AXOLOTL("AXOLOTL", "Axolotl", "美西螈"),
    BAT("BAT", "Bat", "蝙蝠"),
    BEE("BEE", "Bee", "蜜蜂"),
    BLAZE("BLAZE", "Blaze", "烈焰人"),
    BOAT("BOAT", "Boat", "船"),
    CAT("CAT", "Cat", "猫"),
    CAVE_SPIDER("CAVE_SPIDER", "Cave Spider", "洞穴蜘蛛"),
    CHICKEN("CHICKEN", "Chicken", "鸡"),
    COD("COD", "Cod", "鳕鱼"),
    COW("COW", "Cow", "牛"),
    CREEPER("CREEPER", "Creeper", "苦力怕"),
    DOLPHIN("DOLPHIN", "Dolphin", "海豚"),
    DONKEY("DONKEY", "Donkey", "驴"),
    DRAGON_FIREBALL("DRAGON_FIREBALL", "Dragon Fireball", "末影龙火球"),
    DROPPED_ITEM("DROPPED_ITEM", "Dropped Item", "掉落的物品"),
    DROWNED("DROWNED", "Drowned", "溺尸"),
    EGG("EGG", "Egg", "蛋"),
    ELDER_GUARDIAN("ELDER_GUARDIAN", "Elder Guardian", "远古守卫者"),
    ENDER_CRYSTAL("ENDER_CRYSTAL", "Ender Crystal", "末影水晶"),
    ENDER_DRAGON("ENDER_DRAGON", "Ender Dragon", "末影龙"),
    ENDER_PEARL("ENDER_PEARL", "Ender Pearl", "末影珍珠"),
    ENDER_SIGNAL("ENDER_SIGNAL", "Ender Signal", "末影之眼"),
    ENDERMAN("ENDERMAN", "Enderman", "末影人"),
    ENDERMITE("ENDERMITE", "Endermite", "末影螨"),
    EVOKER("EVOKER", "Evoker", "唤魔者"),
    EVOKER_FANGS("EVOKER_FANGS", "Evoker Fangs", "唤魔者尖牙"),
    EXPERIENCE_ORB("EXPERIENCE_ORB", "Experience Orb", "经验球"),
    FALLING_BLOCK("FALLING_BLOCK", "Falling Block", "掉落的方块"),
    FIREBALL("FIREBALL", "Fireball", "火球"),
    FIREWORK("FIREWORK", "Firework", "烟花"),
    FISHING_HOOK("FISHING_HOOK", "Fishing Hook", "鱼钩"),
    FOX("FOX", "Fox", "狐狸"),
    GHAST("GHAST", "Ghast", "恶魂"),
    GIANT("GIANT", "Giant", "巨人"),
    GLOW_ITEM_FRAME("GLOW_ITEM_FRAME", "Glow Item Frame", "荧光物品展示框"),
    GLOW_SQUID("GLOW_SQUID", "Glow Squid", "发光鱿鱼"),
    GOAT("GOAT", "Goat", "山羊"),
    GUARDIAN("GUARDIAN", "Guardian", "守卫者"),
    HOGLIN("HOGLIN", "Hoglin", "疣猪兽"),
    HORSE("HORSE", "Horse", "马"),
    HUSK("HUSK", "Husk", "尸壳"),
    ILLUSIONER("ILLUSIONER", "Illusioner", "幻术师"),
    IRON_GOLEM("IRON_GOLEM", "Iron Golem", "铁傀儡"),
    ITEM_FRAME("ITEM_FRAME", "Item Frame", "物品展示框"),
    LEASH_HITCH("LEASH_HITCH", "Leash Hitch", "栅栏上的拴绳"),
    LIGHTNING("LIGHTNING", "Lightning", "闪电"),
    LLAMA("LLAMA", "Llama", "羊驼"),
    LLAMA_SPIT("LLAMA_SPIT", "Llama Spit", "羊驼唾沫"),
    MAGMA_CUBE("MAGMA_CUBE", "Magma Cube", "岩浆怪"),
    MARKER("MARKER", "Marker", "标记"),
    MINECART("MINECART", "Minecart", "矿车"),
    MINECART_CHEST("MINECART_CHEST", "Minecart with Chest", "运输矿车"),
    MINECART_COMMAND("MINECART_COMMAND", "Minecart with Command Block", "命令方块矿车"),
    MINECART_FURNACE("MINECART_FURNACE", "Minecart with Furnace", "动力矿车"),
    MINECART_HOPPER("MINECART_HOPPER", "Minecart with Hopper", "漏斗矿车"),
    MINECART_MOB_SPAWNER("MINECART_MOB_SPAWNER", "Minecart with Spawner", "刷怪笼矿车"),
    MINECART_TNT("MINECART_TNT", "Minecart with TNT", "TNT矿车"),
    MULE("MULE", "Mule", "骡"),
    MUSHROOM_COW("MUSHROOM_COW", "Mooshroom", "哞菇"),
    OCELOT("OCELOT", "Ocelot", "豹猫"),
    PAINTING("PAINTING", "Painting", "画"),
    PANDA("PANDA", "Panda", "熊猫"),
    PARROT("PARROT", "Parrot", "鹦鹉"),
    PHANTOM("PHANTOM", "Phantom", "幻翼"),
    PIG("PIG", "Pig", "猪"),
    PIG_ZOMBIE("PIG_ZOMBIE", "Pig Zombie", "僵尸猪人"),
    PIGLIN("PIGLIN", "Piglin", "猪灵"),
    PIGLIN_BRUTE("PIGLIN_BRUTE", "Piglin Brute", "猪灵蛮兵"),
    PILLAGER("PILLAGER", "Pillager", "掠夺者"),
    PLAYER("PLAYER", "Player", "玩家"),
    POLAR_BEAR("POLAR_BEAR", "Polar Bear", "北极熊"),
    PRIMED_TNT("PRIMED_TNT", "Primed TNT", "引爆的TNT"),
    PUFFERFISH("PUFFERFISH", "Pufferfish", "河豚"),
    RABBIT("RABBIT", "Rabbit", "兔子"),
    RAVAGER("RAVAGER", "Ravager", "劫掠兽"),
    SALMON("SALMON", "Salmon", "鲑鱼"),
    SHEEP("SHEEP", "Sheep", "绵羊"),
    SHULKER("SHULKER", "Shulker", "潜影贝"),
    SHULKER_BULLET("SHULKER_BULLET", "Shulker Bullet", "潜影贝导弹"),
    SILVERFISH("SILVERFISH", "Silverfish", "蠹虫"),
    SKELETON("SKELETON", "Skeleton", "骷髅"),
    SKELETON_HORSE("SKELETON_HORSE", "Skeleton Horse", "骷髅马"),
    SLIME("SLIME", "Slime", "史莱姆"),
    SMALL_FIREBALL("SMALL_FIREBALL", "Small Fireball", "小型火球"),
    SNOWBALL("SNOWBALL", "Snowball", "雪球"),
    SNOWMAN("SNOWMAN", "Snow Golem", "雪傀儡"),
    SPECTRAL_ARROW("SPECTRAL_ARROW", "Spectral Arrow", "光灵箭"),
    SPIDER("SPIDER", "Spider", "蜘蛛"),
    SPLASH_POTION("SPLASH_POTION", "Splash Potion", "喷溅药水"),
    SQUID("SQUID", "Squid", "鱿鱼"),
    STRAY("STRAY", "Stray", "流浪者"),
    STRIDER("STRIDER", "Strider", "炽足兽"),
    THROWN_EXP_BOTTLE("THROWN_EXP_BOTTLE", "Thrown Bottle o' Enchanting", "丢出的附魔之瓶"),
    TRADER_LLAMA("TRADER_LLAMA", "Trader Llama", "行商羊驼"),
    TRIDENT("TRIDENT", "Trident", "三叉戟"),
    TROPICAL_FISH("TROPICAL_FISH", "Tropical Fish", "热带鱼"),
    TURTLE("TURTLE", "Turtle", "海龟"),
    UNKNOWN("UNKNOWN", "Unknown", "未知"),
    VEX("VEX", "Vex", "恼鬼"),
    VILLAGER("VILLAGER", "Villager", "村民"),
    VINDICATOR("VINDICATOR", "Vindicator", "卫道士"),
    WANDERING_TRADER("WANDERING_TRADER", "Wandering Trader", "流浪商人"),
    WITCH("WITCH", "Witch", "女巫"),
    WITHER("WITHER", "Wither", "凋灵"),
    WITHER_SKELETON("WITHER_SKELETON", "Wither Skeleton", "凋灵骷髅"),
    WITHER_SKULL("WITHER_SKULL", "Wither Skull", "凋灵之首"),
    WOLF("WOLF", "Wolf", "狼"),
    ZOGLIN("ZOGLIN", "Zoglin", "僵尸疣猪兽"),
    ZOMBIE("ZOMBIE", "Zombie", "僵尸"),
    ZOMBIE_HORSE("ZOMBIE_HORSE", "Zombie Horse", "僵尸马"),
    ZOMBIE_VILLAGER("ZOMBIE_VILLAGER", "Zombie Villager", "僵尸村民"),
    ZOMBIFIED_PIGLIN("ZOMBIFIED_PIGLIN", "Zombified Piglin", "僵尸猪灵");

    private final @Getter String type;
    private final @Getter String english;
    private final @Getter String chinese;

    @ParametersAreNonnullByDefault
    EntityTypes(String type, String english, String chinese) {
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
            try {
                if (EntityType.valueOf(type.getType()) == entityType)
                    return type;

            } catch (IllegalArgumentException | NoSuchFieldError ignored) {}

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
