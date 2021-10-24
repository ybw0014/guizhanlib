package net.guizhanss.minecraft.chineselib.minecraft.entity;

import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEntityTypes {
    @Test
    void testFromEnglish() {
        assertEquals(EntityTypes.AXOLOTL, EntityTypes.fromEnglish("Axolotl"));
        assertEquals(EntityTypes.CREEPER, EntityTypes.fromEnglish("creeper"));
        assertEquals(EntityTypes.PLAYER, EntityTypes.fromEnglish("PLAYER"));
    }

    @Test
    void testFromEntityType() {
        assertEquals(EntityTypes.AXOLOTL, EntityTypes.fromEntityType(EntityType.AXOLOTL));
        assertEquals(EntityTypes.CREEPER, EntityTypes.fromEntityType(EntityType.CREEPER));
        assertEquals(EntityTypes.PLAYER, EntityTypes.fromEntityType(EntityType.PLAYER));
    }
}
