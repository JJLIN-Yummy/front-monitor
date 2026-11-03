package com.web3d.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "jwt.auth")
public class JwtProperties {
    private List<String> noAuthUrls;
}
