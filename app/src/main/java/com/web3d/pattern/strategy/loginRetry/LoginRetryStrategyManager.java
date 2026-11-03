package com.web3d.pattern.strategy.loginRetry;

import com.web3d.pattern.strategy.loginRetry.enums.LoginRetryStrategyEnum;
import com.web3d.pattern.strategy.loginRetry.impl.RedisCountLoginRetryStrategy;
import com.web3d.pattern.strategy.loginRetry.inter.ValidStrategy;

import java.util.HashMap;

public class LoginRetryStrategyManager {

    public HashMap<LoginRetryStrategyEnum, ValidStrategy> strategyMap = new HashMap<>();

    public LoginRetryStrategyManager() {
       this.strategyMap.put(LoginRetryStrategyEnum.REDIS_COUNT_STRATEGY, new RedisCountLoginRetryStrategy());
    }
}
