package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.map.MapCursor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * This class holds {@link MapCursor.Type} that are renamed in 1.20.5.
 */
@SuppressWarnings("unused")
@UtilityClass
public class MapCursorTypeX {

    public static final MapCursor.Type TARGET_POINT;
    public static final MapCursor.Type PLAYER;
    public static final MapCursor.Type FRAME;
    public static final MapCursor.Type RED_MARKER;
    public static final MapCursor.Type BLUE_MARKER;
    public static final MapCursor.Type TARGET_X;
    public static final MapCursor.Type PLAYER_OFF_MAP;
    public static final MapCursor.Type PLAYER_OFF_LIMITS;
    public static final MapCursor.Type MONUMENT;
    public static final MapCursor.Type VILLAGE_DESERT;
    public static final MapCursor.Type VILLAGE_PLAINS;
    public static final MapCursor.Type VILLAGE_SAVANNA;
    public static final MapCursor.Type VILLAGE_SNOWY;
    public static final MapCursor.Type VILLAGE_TAIGA;

    // .change("RED_MARKER", "TARGET_POINT")
    //            .forAllVersions()
    //            .change("WHITE_POINTER", "PLAYER")
    //            .change("GREEN_POINTER", "FRAME")
    //            .change("RED_POINTER", "RED_MARKER")
    //            .change("BLUE_POINTER", "BLUE_MARKER")
    //            .change("WHITE_CROSS", "TARGET_X")
    //            .change("WHITE_CIRCLE", "PLAYER_OFF_MAP")
    //            .change("SMALL_WHITE_CIRCLE", "PLAYER_OFF_LIMITS")
    //            .change("TEMPLE", "MONUMENT")
    //            .change("DESERT_VILLAGE", "VILLAGE_DESERT")
    //            .change("PLAINS_VILLAGE", "VILLAGE_PLAINS")
    //            .change("SAVANNA_VILLAGE", "VILLAGE_SAVANNA")
    //            .change("SNOWY_VILLAGE", "VILLAGE_SNOWY")
    //            .change("TAIGA_VILLAGE", "VILLAGE_TAIGA")

    static {
        boolean isAtLeast1_20_5 = MinecraftVersionUtil.isAtLeast(20, 5);

        TARGET_POINT = isAtLeast1_20_5 ? MapCursor.Type.TARGET_POINT : getField("RED_MARKER");
        PLAYER = isAtLeast1_20_5 ? MapCursor.Type.PLAYER : getField("WHITE_POINTER");
        FRAME = isAtLeast1_20_5 ? MapCursor.Type.FRAME : getField("GREEN_POINTER");
        RED_MARKER = isAtLeast1_20_5 ? MapCursor.Type.RED_MARKER : getField("RED_POINTER");
        BLUE_MARKER = isAtLeast1_20_5 ? MapCursor.Type.BLUE_MARKER : getField("BLUE_POINTER");
        TARGET_X = isAtLeast1_20_5 ? MapCursor.Type.TARGET_X : getField("WHITE_CROSS");
        PLAYER_OFF_MAP = isAtLeast1_20_5 ? MapCursor.Type.PLAYER_OFF_MAP : getField("WHITE_CIRCLE");
        PLAYER_OFF_LIMITS = isAtLeast1_20_5 ? MapCursor.Type.PLAYER_OFF_LIMITS : getField("SMALL_WHITE_CIRCLE");
        MONUMENT = isAtLeast1_20_5 ? MapCursor.Type.MONUMENT : getField("TEMPLE");
        VILLAGE_DESERT = isAtLeast1_20_5 ? MapCursor.Type.VILLAGE_DESERT : getField("DESERT_VILLAGE");
        VILLAGE_PLAINS = isAtLeast1_20_5 ? MapCursor.Type.VILLAGE_PLAINS : getField("PLAINS_VILLAGE");
        VILLAGE_SAVANNA = isAtLeast1_20_5 ? MapCursor.Type.VILLAGE_SAVANNA : getField("SAVANNA_VILLAGE");
        VILLAGE_SNOWY = isAtLeast1_20_5 ? MapCursor.Type.VILLAGE_SNOWY : getField("SNOWY_VILLAGE");
        VILLAGE_TAIGA = isAtLeast1_20_5 ? MapCursor.Type.VILLAGE_TAIGA : getField("TAIGA_VILLAGE");
    }

    @Nullable
    private static MapCursor.Type getField(@Nonnull String key) {
        try {
            Field field = MapCursor.Type.class.getDeclaredField(key);
            return (MapCursor.Type) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
