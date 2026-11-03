package com.web3d.pattern.strategy.loginRetry.inter;

import com.web3d.entity.dto.LoginDto;

public interface ValidStrategy {
    boolean validator(LoginDto loginDto);
}
