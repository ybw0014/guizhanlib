package net.guizhanss.guizhanlib.common;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * This package provides rate limit feature.
 *
 * @param <K> The type of key.
 * @author ybw0014
 */
public final class RateLimit<K> {

    private final long limitTime;
    private final int limitVisits;
    private final Map<K, Long> timeMap = new HashMap<>(); // the first visit time
    private final Map<K, Integer> visitMap = new HashMap<>(); // time of visits

    /**
     * Constructor.
     *
     * @param time   Time period in milliseconds.
     * @param visits The limit times of visits within the period.
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
     * @param key The query key.
     * @return Used times of visits.
     */
    public int getUsed(@Nonnull K key) {
        Preconditions.checkArgument(key != null, "key should not be null");

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
     * @param key The query key.
     * @return Remaining times of visits.
     */
    public int getRemaining(@Nonnull K key) {
        Preconditions.checkArgument(key != null, "key should not be null");

        return getLimit() - getUsed(key);
    }

    /**
     * Increases the times of visits.
     *
     * @param key    The query key.
     * @param visits The times of visits
     * @return Whether the action succeeds.
     */
    public boolean add(@Nonnull K key, int visits) {
        Preconditions.checkArgument(key != null, "key should not be null");

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
     * @param key The query key.
     * @return Whether the action succeeds.
     */
    public boolean add(@Nonnull K key) {
        Preconditions.checkArgument(key != null, "key should not be null");

        return add(key, 1);
    }

    /**
     * Reset rate limit.
     *
     * @param key The query key.
     */
    public void reset(@Nonnull K key) {
        Preconditions.checkArgument(key != null, "key should not be null");

        timeMap.remove(key);
        visitMap.remove(key);
    }
}
