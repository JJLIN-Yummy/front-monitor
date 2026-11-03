package com.web3d.filter;


import com.web3d.pattern.strategy.rateLimitCheck.RateLimitCheckStrategy;
import com.web3d.pattern.strategy.rateLimitCheck.enums.RateLimitCheckerNameEnum;
import com.web3d.properties.GatewayLimitProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.web3d.utils.GatewayIpUtil;
import com.web3d.utils.LimitEvent;

import java.nio.charset.StandardCharsets;

@Component
@Order(-100)
public class RateLimitFilter implements GlobalFilter {


    @Autowired
    private RateLimitCheckStrategy rateLimitCheckStrategy;

    // Gateway原生匹配器，单例，不要new多次
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private final GatewayLimitProperties limitProperties;

    public RateLimitFilter(GatewayLimitProperties limitProperties) {
        this.limitProperties = limitProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getPath().value();

        // 遍历白名单，任意一个pattern匹配成功 → 跳过限流
        boolean isSkip = limitProperties.getSkipPaths().stream()
                .anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, path));
        // 白名单跳过限流
        if (isSkip) {
            return chain.filter(exchange);
        }

        String userId = request.getHeaders().getFirst("X-User-Id");
        String ip = GatewayIpUtil.getRealIp(request);
        String limitKey;
        if (userId != null && !userId.isBlank()) {
            limitKey = "gateway:limit:uid:" + userId;
        } else {
            limitKey = "gateway:limit:ip:" + ip;
        }



        return rateLimitCheckStrategy.check(RateLimitCheckerNameEnum.REDIS_WINDOW_CHECKER, limitKey).flatMap(pass->{
            if(pass) return chain.filter(exchange);
            return buildLimitResponse(response);
        });
    }

    private Mono<Void> buildLimitResponse(ServerHttpResponse response) {
        // 返回 429 + json
        String json = "{\"code\":429,\"msg\":\"请求过于频繁，请稍后再试\",\"needCaptcha\":true}";
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }


    private void sendEvent(LimitEvent event) {
        // TODO 投递消息队列
    }
}
