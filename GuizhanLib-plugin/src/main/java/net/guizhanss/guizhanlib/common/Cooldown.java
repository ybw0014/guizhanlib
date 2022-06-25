package net.guizhanss.guizhanlib.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 该类提供冷却时间相关功能
 *
 * @param <K> 冷却键的类型
 * @author ybw0014
 */
public class Cooldown<K> {
    /**
     * 记录冷却结束时间
     */
    private final Map<K, Long> cdMap = new HashMap<>();

    /**
     * 查询是否仍在冷却中
     *
     * @param key 冷却键
     * @return 是否在冷却中
     */
    public boolean has(K key) {
        Optional<Long> resetTime = Optional.ofNullable(cdMap.get(key));
        return resetTime.filter(time -> System.currentTimeMillis() < time).isPresent();
    }

    /**
     * 设置冷却
     *
     * @param key  冷却键
     * @param time 冷却时间(毫秒)
     */
    public void set(K key, long time) {
        cdMap.put(key, System.currentTimeMillis() + time);
    }
}
