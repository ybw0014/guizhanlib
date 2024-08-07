package net.guizhanss.guizhanlib.common;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides cooldown feature, based on keys.
 *
 * @param <K> The type of key.
 * @author ybw0014
 */
public final class Cooldown<K> {
    /**
     * This map records when key is used.
     */
    private final Map<K, Long> useMap = new HashMap<>();
    /**
     * This map records the cooldown time/
     */
    private final Map<K, Long> timeMap = new HashMap<>();

    /**
     * Query if the key can be used.
     *
     * @param key The key.
     * @return Whether the key can be used.
     */
    public boolean check(K key) {
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
    public void set(K key, long time) {
        useMap.put(key, System.nanoTime());
        timeMap.put(key, time * 1_000_000L);
    }
}
