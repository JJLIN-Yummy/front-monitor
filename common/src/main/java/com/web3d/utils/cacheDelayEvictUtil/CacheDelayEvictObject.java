package com.web3d.utils.cacheDelayEvictUtil;

import com.web3d.utils.cacheDelayEvictUtil.inter.EntityTemplate;
import com.web3d.utils.cacheDelayEvictUtil.inter.RedisDelayTemplate;
import lombok.Data;

@Data
public class CacheDelayEvictObject {
    private RedisDelayTemplate redisDelayTemplate;
    private EntityTemplate entityTemplate;

    private String key;
    private long delayMs;

    public CacheDelayEvictObject(RedisDelayTemplate redisDelayTemplate, EntityTemplate entityTemplate, String key, long delayMs) {
        this.redisDelayTemplate = redisDelayTemplate;
        this.entityTemplate = entityTemplate;
        this.key = key;
        this.delayMs = delayMs;
    }

    public boolean deleteKey(){
        return this.redisDelayTemplate.delete(this.key);
    }

    public void updateToDb(){
        this.entityTemplate.update();
    }
}
