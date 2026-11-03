package com.web3d.utils;



import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    //过期时间
    private static final long EXPIRE_TIME = 30 * 1000 * 60 * 1;
    //1000 * 60 * 60 * 24 * 30
    private static final long BIG_EXPIRE_TIME = 1000 * 60 * 60 * 24;

    //jwt密钥
    private static final String SECRET = "anna";

    private static final String REFRESH_SECRET  = "Sanctuary";

    //生成签名,5分钟后过期
    public static String sign(String userId,boolean isRefresh){
        try {

            long time = isRefresh ? BIG_EXPIRE_TIME : EXPIRE_TIME;
            String secret = isRefresh ? REFRESH_SECRET : SECRET;

            Date date = new Date(System.currentTimeMillis() + time);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    //将object保存到token里面
                    .withAudience(userId)
                    //五分钟后过期
                    .withExpiresAt(date)
                    //token密钥
                    .sign(algorithm);

        }catch (Exception e){
            return "jwt错误";
        }
    }

    //根据token获取userId
    public static String getPayload(String token){
        try {
            String result = JWT.decode(token).getAudience().get(0);
            return result;
        }catch (Exception e){
            return null;
        }
    }

    //检验jwt
    public static DecodedJWT checkSign(String token,boolean isRefresh) {
        String secret = isRefresh ? REFRESH_SECRET : SECRET;

        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                // .withClaim("username", username)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt;

//        catch (JWTVerificationException exception) {
//            throw new RuntimeException("token 无效，请重新获取");
//        }
    }
}
