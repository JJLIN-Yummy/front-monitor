package com.web3d.config;

import com.web3d.interceptor.TimeFillInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {
    @Bean
    public TimeFillInterceptor timeFillInterceptor() {
        return new TimeFillInterceptor();
    }
}