package com.web3d.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "gateway.limit")
public class GatewayLimitProperties {
    /**
     * 限流跳过白名单，支持Ant通配符
     * 示例：/api/health, /api/captcha/**, /static/**
     */
    private List<String> skipPaths = new ArrayList<>();

    public List<String> getSkipPaths() {
        return skipPaths;
    }

    public void setSkipPaths(List<String> skipPaths) {
        this.skipPaths = skipPaths;
    }
}
