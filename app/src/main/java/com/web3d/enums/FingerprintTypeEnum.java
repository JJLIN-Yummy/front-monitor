package com.web3d.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "指纹类型枚举") // 🎯 给整个枚举加描述
public enum FingerprintTypeEnum {

    // ---------------- 设备指纹 (用于识别匿名用户) ----------------
    WEB_CANVAS("WEB_CANVAS", "Web Canvas 浏览器指纹"),
    WEB_LOCAL_STORAGE("WEB_LOCAL_STORAGE", "Web 本地缓存 UUID"),
    ANDROID_OAID("ANDROID_OAID", "Android 匿名设备标识符"),
    ANDROID_ID("ANDROID_ID", "Android 系统 ID"),
    IOS_IDFV("IOS_IDFV", "iOS 供应商标识符"),
    IOS_IDFA("IOS_IDFA", "iOS 广告标识符"),
    WECHAT_OPENID("WECHAT_OPENID", "微信小程序 openid"),

    // ---------------- 错误指纹 (用于将重复报错聚合成 1 个 Issue) ----------------
    ERROR_STACK_HASH("ERROR_STACK_HASH", "错误堆栈 MD5 聚合指纹"),

    // ---------------- 兜底 ----------------
    UNKNOWN("UNKNOWN", "未知类型");

    private final String code;
    private final String desc;

    FingerprintTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 安全转换：根据 code 获取枚举对象
     */
    public static FingerprintTypeEnum getByCode(String code) {
        if (code == null) return UNKNOWN;
        for (FingerprintTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
