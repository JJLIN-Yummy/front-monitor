package com.web3d.pattern.strategy.rateLimitCheck;

import com.web3d.pattern.strategy.rateLimitCheck.enums.RateLimitCheckerNameEnum;
import com.web3d.pattern.strategy.rateLimitCheck.rateLimitChecker.RateLimitChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class RateLimitCheckStrategy {

    @Autowired
    private Map<String, RateLimitChecker> rateLimitCheckerMap;

    public Mono<Boolean> check(RateLimitCheckerNameEnum rateLimitCheckerNameEnum, String key){

        return this.rateLimitCheckerMap.get(rateLimitCheckerNameEnum.getName()).check(key);
    }
}
