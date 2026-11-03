package com.web3d.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "环境枚举") // 🎯 给整个枚举加描述
public enum EnvEnum {
    DEV("dev"),
    PROD("prod"),
    test("test");

    private final String name;

    EnvEnum(String name) {
        this.name = name;
    }

    /**
     * 🎯 校验传入的字符串是否合法
     *
     * @param name 前端传入的字符串（如 "prod"）
     * @return 是否合法
     */
    public static boolean isValid(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        for (EnvEnum env : values()) {
            if (env.getName().equalsIgnoreCase(name)) { // equalsIgnoreCase 忽略大小写，容错率更高
                return true;
            }
        }
        return false;
    }
}
