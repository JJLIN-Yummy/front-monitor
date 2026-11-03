package com.web3d.utils.cacheDelayEvictUtil;

import com.web3d.exception.BusinessException;
import com.web3d.model.ResultCodeEnum;
import com.web3d.utils.cacheDelayEvictUtil.inter.RedisDelayTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheDelayEvictUtil {

    // 单例的定时调度线程池（防止每次调用都创建新线程）
    private static final ScheduledExecutorService SCHEDULER =
            Executors.newScheduledThreadPool(2);

    public static void delayDoubleDelete(CacheDelayEvictObject cacheDelayEvictObject){
        try{
            boolean deleteSuccess = cacheDelayEvictObject.deleteKey();
            if(!deleteSuccess){
                return;
            }
            cacheDelayEvictObject.updateToDb();
            CacheDelayEvictUtil.asyncDelayDelete(cacheDelayEvictObject);
        }catch (Exception e){
            throw new BusinessException(ResultCodeEnum.SERVER_ERROR);
        }

    }

    /**
     * 异步延时删除缓存（通常用于延时双删的第二次删除）
     *
     */
    public static void asyncDelayDelete(CacheDelayEvictObject cacheDelayEvictObject) {
        String cacheKey = cacheDelayEvictObject.getKey();
        RedisDelayTemplate redisTemplate = cacheDelayEvictObject.getRedisDelayTemplate();
        long delayMs = cacheDelayEvictObject.getDelayMs();

        // 防止 key 为空导致无意义的线程调度
        if (cacheKey == null || cacheKey.trim().isEmpty()) {
            return;
        }

        SCHEDULER.schedule(() -> {
            try {
                Boolean deleted = redisTemplate.delete(cacheKey);
                if (Boolean.TRUE.equals(deleted)) {
                    log.info("异步删除缓存Key = {}", cacheKey);
                } else {
                    log.info("异步删除缓存Key：缓存可能已过期或不存在，无需删除，Key = {}", cacheKey);
                }
            } catch (Exception e) {
                // 🎯 极其重要：这里的异常必须吞掉，绝不能抛出影响主线程
                // 因为主流程的业务（数据库更新）已经成功了，缓存删失败可以通过 TTL 兜底
                log.error("异步删除缓存Key：删除缓存失败，Key = {}", cacheKey, e);
            }
        }, delayMs, TimeUnit.MILLISECONDS);
    }
}
