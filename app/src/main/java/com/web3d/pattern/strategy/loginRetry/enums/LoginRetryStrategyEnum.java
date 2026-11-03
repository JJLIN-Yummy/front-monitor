package com.web3d.pattern.strategy.loginRetry.enums;

public enum LoginRetryStrategyEnum {

    REDIS_COUNT_STRATEGY("redis_count_strategy");

    private final String name;
    LoginRetryStrategyEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
