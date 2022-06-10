package net.guizhanss.guizhanlib.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 该类提供频率限制相关功能
 *
 * @param <K> 频率键的类型
 * @author ybw0014
 */
public class RateLimit<K> {
    private final long limitTime;
    private final long limitVisits;
    private final Map<K, Long> timeMap; // 重置时间
    private final Map<K, Long> visitMap; // 访问次数

    /**
     * 构建函数
     *
     * @param time   时间(毫秒)
     * @param visits 访问次数
     */
    public RateLimit(long time, long visits) {
        this.limitTime = time;
        this.limitVisits = visits;
        this.timeMap = new HashMap<>();
        this.visitMap = new HashMap<>();
    }

    /**
     * 获取总限制次数
     *
     * @return 总限制次数
     */
    public long getLimit() {
        return this.limitVisits;
    }

    /**
     * 获取已用次数
     *
     * @param key 查询键
     * @return 已用次数
     */
    public long getUsed(K key) {
        Optional<Long> time = Optional.ofNullable(this.timeMap.get(key));
        if (time.isPresent()) {
            if (time.get() > System.currentTimeMillis()) {
                return 0;
            }

            Optional<Long> visits = Optional.ofNullable(this.visitMap.get(key));

            if (visits.isPresent()) {
                return visits.get();
            }
        }
        return 0;
    }

    /**
     * 获取剩余次数
     *
     * @param key 查询键
     * @return 剩余次数
     */
    public long getRemaining(K key) {
        return this.limitVisits - this.getUsed(key);
    }

    /**
     * 增加访问次数
     *
     * @param key    查询键
     * @param visits 访问次数
     * @return 是否成功增加访问次数
     */
    public boolean add(K key, long visits) {
        Optional<Long> time = Optional.ofNullable(this.timeMap.get(key));
        if (time.isPresent()) {
            if (time.get() < System.currentTimeMillis()) {
                this.reset(key);
                this.timeMap.put(key, System.currentTimeMillis() + this.limitTime);
            }

            Optional<Long> pVisits = Optional.ofNullable(this.visitMap.get(key));
            if (pVisits.isPresent()) {
                if (pVisits.get() + visits > this.getLimit()) {
                    return false;
                }
                this.visitMap.put(key, visits + pVisits.get());
            } else {
                this.visitMap.put(key, visits);
            }
        } else {
            this.timeMap.put(key, System.currentTimeMillis() + this.limitTime);
            this.visitMap.put(key, visits);
        }

        return true;
    }

    /**
     * 增加1次访问次数
     *
     * @param key 查询键
     * @return 是否成功增加访问次数
     */
    public boolean add(K key) {
        return this.add(key, 1);
    }

    /**
     * 重置频率限制
     *
     * @param key 查询键
     */
    public void reset(K key) {
        this.timeMap.remove(key);
        this.visitMap.remove(key);
    }
}
