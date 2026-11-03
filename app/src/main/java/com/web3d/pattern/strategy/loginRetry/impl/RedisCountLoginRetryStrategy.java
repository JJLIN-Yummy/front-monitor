package com.web3d.pattern.strategy.loginRetry.impl;

import com.web3d.entity.dto.LoginDto;
import com.web3d.pattern.strategy.loginRetry.inter.ValidStrategy;

public class RedisCountLoginRetryStrategy implements ValidStrategy {
    @Override
    public boolean validator(LoginDto loginDto) {
        return false;
    }
}
