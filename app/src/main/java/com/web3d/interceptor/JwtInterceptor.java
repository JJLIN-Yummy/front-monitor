package com.web3d.interceptor;


import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.web3d.annotation.JwtToken;
import com.web3d.exception.BusinessException;
import com.web3d.model.ResultCodeEnum;
import com.web3d.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Pattern BEARER_PATTERN =
            Pattern.compile("^Bearer\\s+(\\S+)$", Pattern.CASE_INSENSITIVE);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //http请求头中拿出token
        String authHeader = request.getHeader("Authorization");

        //如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod =  (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();


        //检查有没有需要用户权限的注解
        if(method.isAnnotationPresent(JwtToken.class)){
            Matcher matcher = BEARER_PATTERN.matcher(authHeader);
            if(!matcher.matches()){
                throw new BusinessException(ResultCodeEnum.INVALID_TOKEN);
            }
            String token = matcher.group(1);

            JwtToken jwtToken = method.getAnnotation(JwtToken.class);
            if(jwtToken.required()){
                //执行认证
                if(token == null){
                    throw new BusinessException(ResultCodeEnum.NO_TOKEN);
                }
                //获取token的userId
                String payload = JwtUtil.getPayload(token);
                log.info("用户Id:{}",payload);
//                System.out.println("用户id: " + payload);

                //验证token
               try {
                   JwtUtil.checkSign(token,false);
               }catch (TokenExpiredException e){
                   throw new BusinessException(ResultCodeEnum.EXPIRE_TOKEN);
               }catch (SignatureVerificationException e){
                   throw new BusinessException(ResultCodeEnum.INVALID_TOKEN);
               }catch (AlgorithmMismatchException e){
                   throw new BusinessException(ResultCodeEnum.ALGORITHMMISMATCH_TOKEN);
               }catch (Exception e){
                   throw new BusinessException(ResultCodeEnum.INVALID_TOKEN);
               }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
