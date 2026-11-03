package com.web3d.pattern.strategy.rateLimitCheck.rateLimitChecker.impl;

import com.web3d.pattern.strategy.rateLimitCheck.rateLimitChecker.RateLimitChecker;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RedisCountChecker implements RateLimitChecker {

    @Resource
    @Qualifier("stringReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private final DefaultRedisScript<Long> limitScript;

    private static final int MAX_COUNT = 10;
    private static final int WINDOW_SECONDS = 60;

    public RedisCountChecker() {
        String script = """
                local key = KEYS[1]
                local max = tonumber(ARGV[1]) 
                local ttl = tonumber(ARGV[2])
                local cnt = redis.call('INCR', key)
                if cnt == 1 then
                    redis.call('EXPIRE', key, ttl)
                end
                if cnt > max then
                    return 1
                else
                    return 0
                end
                """;
        limitScript = new DefaultRedisScript<>(script, Long.class);
    }

    @Override
    public Mono<Boolean> check(String key) {

        // 响应式执行lua，非阻塞
        Mono<Long> hitMono = reactiveRedisTemplate.execute(
                limitScript,
                List.of(key),
                List.of(MAX_COUNT, WINDOW_SECONDS)
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
