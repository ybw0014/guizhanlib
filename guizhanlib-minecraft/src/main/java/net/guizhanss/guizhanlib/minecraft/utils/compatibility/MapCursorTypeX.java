package net.guizhanss.guizhanlib.minecraft.utils.compatibility;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;
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

    static {
        boolean isPost205 = MinecraftVersionUtil.isAtLeast(1, 20, 5);

        TARGET_POINT = isPost205 ? MapCursor.Type.TARGET_POINT : getField("RED_MARKER");
        PLAYER = isPost205 ? MapCursor.Type.PLAYER : getField("WHITE_POINTER");
        FRAME = isPost205 ? MapCursor.Type.FRAME : getField("GREEN_POINTER");
        RED_MARKER = isPost205 ? MapCursor.Type.RED_MARKER : getField("RED_POINTER");
        BLUE_MARKER = isPost205 ? MapCursor.Type.BLUE_MARKER : getField("BLUE_POINTER");
        TARGET_X = isPost205 ? MapCursor.Type.TARGET_X : getField("WHITE_CROSS");
        PLAYER_OFF_MAP = isPost205 ? MapCursor.Type.PLAYER_OFF_MAP : getField("WHITE_CIRCLE");
        PLAYER_OFF_LIMITS = isPost205 ? MapCursor.Type.PLAYER_OFF_LIMITS : getField("SMALL_WHITE_CIRCLE");
        MONUMENT = isPost205 ? MapCursor.Type.MONUMENT : getField("TEMPLE");
        VILLAGE_DESERT = isPost205 ? MapCursor.Type.VILLAGE_DESERT : getField("DESERT_VILLAGE");
        VILLAGE_PLAINS = isPost205 ? MapCursor.Type.VILLAGE_PLAINS : getField("PLAINS_VILLAGE");
        VILLAGE_SAVANNA = isPost205 ? MapCursor.Type.VILLAGE_SAVANNA : getField("SAVANNA_VILLAGE");
        VILLAGE_SNOWY = isPost205 ? MapCursor.Type.VILLAGE_SNOWY : getField("SNOWY_VILLAGE");
        VILLAGE_TAIGA = isPost205 ? MapCursor.Type.VILLAGE_TAIGA : getField("TAIGA_VILLAGE");
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
