package com.web3d.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;

public class GatewayIpUtil {
    public static String getRealIp(ServerHttpRequest request) {
        String xff = request.getHeaders().getFirst("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        String realIp = request.getHeaders().getFirst("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddress().getAddress().getHostAddress();
    }
}
