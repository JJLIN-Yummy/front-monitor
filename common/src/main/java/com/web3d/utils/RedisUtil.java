package com.web3d.utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class RedisUtil {

    public RedisUtil() {

    }

     public static boolean setString(RedisTemplate redisTemplate,String key,String value,int time,TimeUnit timeUnit){
        try {
            ValueOperations ops = redisTemplate.opsForValue();
            ops.set(key,value,time, timeUnit);
        }catch (Exception e){
            throw e;
        }
        return true;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getCacheObject(RedisTemplate redisTemplate, final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    public boolean hasKey(RedisTemplate redisTemplate, final String key){

        return redisTemplate.hasKey(key) && redisTemplate.getExpire(key) > 0;
    }


    // ================== 新增的方法 ==================
    /**
     * 执行 Lua 脚本 (防腐层：避免业务层直接依赖 Spring Data Redis 的底层 API)
     *
     * @param script Lua 脚本对象
     * @param key    KEYS[1] 的 key
     * @param args   ARGV 的参数
     * @return 脚本执行结果
     */
    public static Long execute(RedisTemplate redisTemplate, DefaultRedisScript<Long> script, List key, Object... args) {
        return (Long) redisTemplate.execute(script, Collections.singletonList(key), args);
    }


    /**
     * 极简通用缓存：先查 Redis，没有就查库，然后写回 Redis
     *
     * @param redisTemplate 必须传入配置了【通用 JSON 序列化器】的 RedisTemplate（泛型必须是 String, Object）
     * @param key 缓存键
     * @param dbFallback 查库逻辑
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 任意类型的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getWithCache(
            RedisTemplate<String, Object> redisTemplate,
            String key,
            Supplier<T> dbFallback,
            long timeout,
            TimeUnit unit) {
        try {
            // 1. 先查缓存 (此时 RedisTemplate 内部配好序列化器，直接返回真实的 Java 对象)
            Object cacheValue = redisTemplate.opsForValue().get(key);
            if (cacheValue != null) {
                log.info("缓存命中: {}", key);
                return (T) cacheValue; // 直接强转，不管你是 User 还是 List，全都能接住
            }

            // 2. 缓存没有，执行查库逻辑
            log.info("缓存未命中，查库: {}", key);
            T result = dbFallback.get();

            // 3. 查到了就写回缓存
            if (result != null) {
                redisTemplate.opsForValue().set(key, result, timeout, unit);
                log.info("查库并回写缓存成功: {}", key);
            }

            return result;

        } catch (Exception e) {
            // 4. 只要 Redis 挂了、报错了，直接降级查库，绝不耽误业务
            log.error("缓存操作异常，降级查库: {}", key, e);
            return dbFallback.get();
        }
    }



}
