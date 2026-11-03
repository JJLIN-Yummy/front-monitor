package com.web3d.pattern.strategy.rateLimitCheck.rateLimitChecker;

import reactor.core.publisher.Mono;

public interface RateLimitChecker {
     Mono<Boolean> check(String key);
}
