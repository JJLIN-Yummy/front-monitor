package com.web3d.pattern.strategy.rateLimitCheck.enums;


public enum RateLimitCheckerNameEnum {

    REDIS_COUNT_CHECKER("redisCountChecker"),
    REDIS_WINDOW_CHECKER("redisWindowChecker");

    private final String name;

    RateLimitCheckerNameEnum(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
