package net.guizhanss.guizhanlib.common;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides cooldown feature, based on keys.
 *
 * @param <K> The type of key.
 * @author ybw0014
 */
public final class Cooldown<K> {

    private final Map<K, Long> useMap = new HashMap<>(); // record the last use time of the key
    private final Map<K, Long> timeMap = new HashMap<>(); // record the cooldown duration of the key

    /**
     * Query if the key can be used.
     *
     * @param key The key.
     * @return Whether the key can be used.
     */
    public boolean check(@Nonnull K key) {
        Preconditions.checkArgument(key != null, "key should not be null");

        Long lastUse = useMap.get(key);
        Long cdTime = timeMap.get(key);

        // Lack of record, this should not happen.
        if (lastUse == null || cdTime == null) {
            return true;
        }

        return System.nanoTime() - lastUse >= cdTime;
    }

    /**
     * Set cooldown of key.
     *
     * @param key  The key.
     * @param time The cooldown in milliseconds.
     */
    public void set(@Nonnull K key, long time) {
        Preconditions.checkArgument(key != null, "key should not be null");

        useMap.put(key, System.nanoTime());
        timeMap.put(key, time * 1_000_000L);
    }
}
