package com.web3d.utils.cacheDelayEvictUtil.inter;

//操作缓存接口
public interface RedisDelayTemplate {
    Boolean delete(String key);
}
