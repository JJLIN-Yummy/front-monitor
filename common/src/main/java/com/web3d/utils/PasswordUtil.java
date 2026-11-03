package com.web3d.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密解密工具类 (基于 BCrypt)
 * 注意：BCrypt 是不可逆加密，只能加密和校验，不能解密！
 */
public class PasswordUtil {

    // 创建一个全局唯一的 BCrypt 实例，底层是线程安全的
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 加密密码（用于注册、修改密码时存入数据库）
     * @param rawPassword 明文密码
     * @return 加密后的密文
     */
    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 校验密码（用于登录时比对）
     * @param rawPassword 前端传来的明文密码
     * @param encodedPassword 数据库里存的密文密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || rawPassword.isBlank() || encodedPassword == null) {
            return false;
        }
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}