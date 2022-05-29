package net.guizhanss.guizhanlib.minecraft;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * This enum contains some useful tags that
 * {@link org.bukkit.Tag} does not have
 */
public enum MinecraftTag {
    /**
     * This includes all types of helmets
     */
    HELMET {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.LEATHER_HELMET
                || type == Material.CHAINMAIL_HELMET
                || type == Material.IRON_HELMET
                || type == Material.GOLDEN_HELMET
                || type == Material.DIAMOND_HELMET
                || type == Material.NETHERITE_HELMET
                || type == Material.TURTLE_HELMET;
        }
    },

    /**
     * This includes all types of chestplates
     */
    CHESTPLATE {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.LEATHER_CHESTPLATE
                || type == Material.CHAINMAIL_CHESTPLATE
                || type == Material.IRON_CHESTPLATE
                || type == Material.GOLDEN_CHESTPLATE
                || type == Material.DIAMOND_CHESTPLATE
                || type == Material.NETHERITE_CHESTPLATE
                || type == Material.ELYTRA;
        }
    },

    /**
     * This includes all types of leggings
     */
    LEGGINGS {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.LEATHER_LEGGINGS
                || type == Material.CHAINMAIL_LEGGINGS
                || type == Material.IRON_LEGGINGS
                || type == Material.GOLDEN_LEGGINGS
                || type == Material.DIAMOND_LEGGINGS
                || type == Material.NETHERITE_LEGGINGS;
        }
    },

    /**
     * This include all types of boots
     */
    BOOTS {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.LEATHER_BOOTS
                || type == Material.CHAINMAIL_BOOTS
                || type == Material.IRON_BOOTS
                || type == Material.GOLDEN_BOOTS
                || type == Material.DIAMOND_BOOTS
                || type == Material.NETHERITE_BOOTS;
        }
    },

    /**
     * This includes any armor
     */
    ARMOR {
        @Override
        public boolean isTagged(Material type) {
            return HELMET.isTagged(type)
                || CHESTPLATE.isTagged(type)
                || LEGGINGS.isTagged(type)
                || BOOTS.isTagged(type);
        }
    },

    /**
     * This includes all types of swords
     */
    SWORD {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.WOODEN_SWORD
                || type == Material.STONE_SWORD
                || type == Material.IRON_SWORD
                || type == Material.GOLDEN_SWORD
                || type == Material.DIAMOND_SWORD
                || type == Material.NETHERITE_SWORD
                || type == Material.TRIDENT;
        }
    },

    /**
     * This includes all types of axes
     */
    AXE {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.WOODEN_AXE
                || type == Material.STONE_AXE
                || type == Material.IRON_AXE
                || type == Material.GOLDEN_AXE
                || type == Material.DIAMOND_AXE
                || type == Material.NETHERITE_AXE;
        }
    },

    /**
     * This includes all types of pickaxes
     */
    PICKAXE {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.WOODEN_PICKAXE
                || type == Material.STONE_PICKAXE
                || type == Material.IRON_PICKAXE
                || type == Material.GOLDEN_PICKAXE
                || type == Material.DIAMOND_PICKAXE
                || type == Material.NETHERITE_PICKAXE;
        }
    },

    /**
     * This includes all types of shovel
     */
    SHOVEL {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.WOODEN_SHOVEL
                || type == Material.STONE_SHOVEL
                || type == Material.IRON_SHOVEL
                || type == Material.GOLDEN_SHOVEL
                || type == Material.DIAMOND_SHOVEL
                || type == Material.NETHERITE_SHOVEL;
        }
    },

    /**
     * This includes all types of hoe
     */
    HOE {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.WOODEN_HOE
                || type == Material.STONE_HOE
                || type == Material.IRON_HOE
                || type == Material.GOLDEN_HOE
                || type == Material.DIAMOND_HOE
                || type == Material.NETHERITE_HOE;
        }
    },
    
    /**
     * This includes all types of skull
     */
    SKULL {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.SKELETON_SKULL
                || type == Material.WITHER_SKELETON_SKULL
                || type == Material.ZOMBIE_HEAD
                || type == Material.PLAYER_HEAD
                || type == Material.CREEPER_HEAD
                || type == Material.DRAGON_HEAD
                || type == Material.CARVED_PUMPKIN;
        }
    },
    
     /**
     * This includes all types of horse armor
     */
    HORSE_ARMOR {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.LEATHER_HORSE_ARMOR
                || type == Material.IRON_HORSE_ARMOR
                || type == Material.GOLDEN_HORSE_ARMOR
                || type == Material.DIAMOND_HORSE_ARMOR;
        }
    },
    
    /**
     * This includes all types of bow
     */
    BOW {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.BOW
                || type == Material.CROSSBOW;
        }
    },
    
    /**
     * This includes all types of fishing rod
     */
    
    FISHING_ROD {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.FISHING_ROD
                || type == Material.CARROT_ON_A_STICK
                || type == Material.WARPED_FUNGUS_ON_A_STICK;
        }
    },
    
    /**
     * This includes all types of shears
     */
    
    OFF_HAND_TOOL {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.SHEARS
            || type == Material.SHIELD;

        }
    },
 
    /**
     * This includes all types of potions
     */
    POTION {
        @Override
        public boolean isTagged(Material type) {
            return type == Material.POTION
                || type == Material.SPLASH_POTION
                || type == Material.LINGERING_POTION;
        }
    },

    /**
     * This includes all types of potions and tipped arrow
     */
    POTION_WITH_TIPPED_ARROW {
        @Override
        public boolean isTagged(Material type) {
            return POTION.isTagged(type)
                || type == Material.TIPPED_ARROW;
        }
    };

    /**
     * This method returns if given {@link Material} is tagged.
     *
     * @param type the {@link Material} to be determined
     *
     * @return if given {@link Material} is tagged
     */
    public abstract boolean isTagged(Material type);

    /**
     * This method returns if given {@link ItemStack} is tagged.
     *
     * @param itemStack the {@link ItemStack} to be determined
     *
     * @return if given {@link ItemStack} is tagged
     */
    public boolean isTagged(ItemStack itemStack) {
        return isTagged(itemStack.getType());
    }
}
