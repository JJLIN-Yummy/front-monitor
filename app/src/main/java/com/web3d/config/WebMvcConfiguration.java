package com.web3d.config;

import com.web3d.interceptor.JwtInterceptor;
import com.web3d.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor; //jwt拦截器

    @Autowired
    private JwtProperties jwtProperties;

    //拦截器注册
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtInterceptor).excludePathPatterns(jwtProperties.getNoAuthUrls())
                .addPathPatterns("/**");
//                .excludePathPatterns(
//                        "/admin/system/index/login",
//                        "/admin/system/index/generateValidateCode"
//                )

    }


    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
