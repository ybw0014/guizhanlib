package net.guizhanss.guizhanlib.common;

import java.util.HashMap;
import java.util.Map;

/**
 * This package provides rate limit feature.
 *
 * @param <K>
 *     The type of key.
 *
 * @author ybw0014
 */
public final class RateLimit<K> {
    private final long limitTime;
    private final int limitVisits;
    /**
     * This map records the first visit time.
     */
    private final Map<K, Long> timeMap = new HashMap<>();
    /**
     * This map records the times of visits.
     */
    private final Map<K, Integer> visitMap = new HashMap<>();

    /**
     * Constructor.
     *
     * @param time
     *     Time period in milliseconds.
     * @param visits
     *     The limit times of visits.
     */
    public RateLimit(long time, int visits) {
        this.limitTime = time;
        this.limitVisits = visits;
    }

    /**
     * Get the overall limit of visits.
     *
     * @return The overall limit of visits.
     */
    public int getLimit() {
        return this.limitVisits;
    }

    /**
     * Get the used visits.
     *
     * @param key
     *     The query key.
     *
     * @return Used times of visits.
     */
    public int getUsed(K key) {
        Long time = timeMap.get(key);
        Integer visits = visitMap.get(key);

        // Lack of record, this should not happen
        if (time == null || visits == null) {
            return 0;
        }

        // Time out
        if (System.nanoTime() - time >= limitTime) {
            return 0;
        }

        return visits;
    }

    /**
     * Get remaining times of visits
     *
     * @param key
     *     The query key.
     *
     * @return Remaining times of visits
     */
    public int getRemaining(K key) {
        return getLimit() - getUsed(key);
    }

    /**
     * Increases the times of visits.
     *
     * @param key
     *     The query key.
     * @param visits
     *     The times of visits
     *
     * @return Whether the action succeeds.
     */
    public boolean add(K key, int visits) {
        Long time = timeMap.get(key);
        Integer visited = visitMap.get(key);

        // Lack of record, just init
        if (time == null || visited == null) {
            reset(key);
            timeMap.put(key, System.nanoTime());
            visitMap.put(key, visits);
            return true;
        }

        if (getRemaining(key) < visits) {
            return false;
        } else {
            visitMap.put(key, visited + visits);
            return true;
        }
    }

    /**
     * Increases the times of visits by 1.
     *
     * @param key
     *     The query key.
     *
     * @return Whether the action succeeds.
     */
    public boolean add(K key) {
        return add(key, 1);
    }

    /**
     * Reset rate limit
     *
     * @param key
     *     The query key.
     */
    public void reset(K key) {
        timeMap.remove(key);
        visitMap.remove(key);
    }
}
