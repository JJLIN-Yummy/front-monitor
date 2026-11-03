package com.web3d.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // ================== 1. 纯字符串模板（用来跑限流 Lua 脚本） ==================
    @Bean
    public ReactiveRedisTemplate<String, String> stringReactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer stringSer = new StringRedisSerializer();
        RedisSerializationContext<String, String> ctx = RedisSerializationContext
                .<String, String>newSerializationContext()
                .key(RedisSerializationContext.SerializationPair.fromSerializer(stringSer))
                .value(RedisSerializationContext.SerializationPair.fromSerializer(stringSer))
                .hashKey(RedisSerializationContext.SerializationPair.fromSerializer(stringSer))
                .hashValue(RedisSerializationContext.SerializationPair.fromSerializer(stringSer))
                .build();
        return new ReactiveRedisTemplate<>(factory, ctx);
    }

    // ================== 2. 对象模板（用来存业务数据，比如 User 对象） ==================
    @Bean
    @Primary // 默认注入这个
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        // Key 用 String
        StringRedisSerializer stringSer = new StringRedisSerializer();

        // Value 用 JSON
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer<Object> jsonSer = new Jackson2JsonRedisSerializer<>(om, Object.class);

        RedisSerializationContext<String, Object> ctx = RedisSerializationContext
                .<String, Object>newSerializationContext()
                .key(RedisSerializationContext.SerializationPair.fromSerializer(stringSer))
                .value(RedisSerializationContext.SerializationPair.fromSerializer(jsonSer))
                .hashKey(RedisSerializationContext.SerializationPair.fromSerializer(stringSer))
                .hashValue(RedisSerializationContext.SerializationPair.fromSerializer(jsonSer))
                .build();

        return new ReactiveRedisTemplate<>(factory, ctx);
    }
}