package com.web3d.pattern.strategy.rateLimitCheck.rateLimitChecker.impl;

import com.web3d.pattern.strategy.rateLimitCheck.rateLimitChecker.RateLimitChecker;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class RedisWindowChecker implements RateLimitChecker {

    @Resource
    @Qualifier("stringReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private final DefaultRedisScript<Long> limitScript;

    private static final int MAX_COUNT = 10;
    private static final int WINDOW_SECONDS = 60;

    public RedisWindowChecker() {
        String script = """
                -- KEYS[1]: 限流的 Key (比如 limit:ip:127.0.0.1)
                -- ARGV[1]: 最大请求数 (比如 100)
                -- ARGV[2]: 时间窗口大小，单位毫秒 (比如 60000 代表 60 秒)
                -- ARGV[3]: 当前时间戳，单位毫秒
                -- ARGV[4]: 本次请求的唯一标识 (比如 UUID，防止同一毫秒并发冲突)
                               
                local key = KEYS[1]
                local maxCount = tonumber(ARGV[1])
                local windowSize = tonumber(ARGV[2])
                local now = tonumber(ARGV[3])
                local uuid = ARGV[4]
                               
                -- 1. 计算时间窗口的起始时间（当前时间 - 窗口大小）
                local windowStart = now - windowSize
                               
                -- 2. 删除窗口之外（过期）的旧数据
                redis.call('ZREMRANGEBYSCORE', key, 0, windowStart)
                               
                -- 3. 统计当前窗口内的请求数量
                local currentCount = redis.call('ZCARD', key)
                               
                -- 4. 判断是否超过限流阈值
                if currentCount < maxCount then
                    -- 没有超限：把当前请求的时间戳插入 ZSet
                    redis.call('ZADD', key, now, uuid)
                    -- 给整个 ZSet 设置一个过期时间（窗口大小 + 1秒），防止冷数据一直占内存
                    redis.call('PEXPIRE', key, windowSize + 1000)
                    return 0 -- 返回 0 代表放行
                else
                    return 1 -- 返回 1 代表被限流
                end
                """;
        limitScript = new DefaultRedisScript<>(script, Long.class);
    }

    @Override
    public Mono<Boolean> check(String key) {

        long now = System.currentTimeMillis();
        // 用 UUID 保证同一毫秒进来的多个请求，Member 不一样，不会被覆盖
        String uuid = UUID.randomUUID().toString();

        Mono<Long> hitMono = reactiveRedisTemplate.execute(
                limitScript,
                List.of(key),
                List.of(MAX_COUNT, WINDOW_SECONDS*1000,now, uuid)
        ).next();

        return hitMono
                .map(hit -> {
                    if (hit != null && hit == 1) {
//                        sendEvent(new LimitEvent(ip, userId, path, LocalDateTime.now()));
                        return false;
                    }
                    return true;
                });
    }
}
